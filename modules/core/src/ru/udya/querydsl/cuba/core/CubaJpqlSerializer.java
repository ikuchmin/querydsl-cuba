package ru.udya.querydsl.cuba.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.SerializerBase;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Constant;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.util.MathUtils;
import com.querydsl.jpa.JPAQueryMixin;
import com.querydsl.jpa.JPQLOps;
import com.querydsl.jpa.JPQLTemplates;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CubaJpqlSerializer extends SerializerBase<CubaJpqlSerializer> {

    public static final String QUERY_PARAM_HOLDER = "qdslparam";

    private static final Set<? extends Operator> NUMERIC = Sets.immutableEnumSet(
            Ops.ADD, Ops.SUB, Ops.MULT, Ops.DIV,
            Ops.LT, Ops.LOE, Ops.GT, Ops.GOE, Ops.BETWEEN);

    private static final Set<? extends Operator> CASE_OPS = Sets.immutableEnumSet(
            Ops.CASE_EQ_ELSE, Ops.CASE_ELSE);

    private static final String COMMA = ", ";

    private static final String DELETE = "delete from ";

    private static final String FROM = "from ";

    private static final String GROUP_BY = "\ngroup by ";

    private static final String HAVING = "\nhaving ";

    private static final String ORDER_BY = "\norder by ";

    private static final String SELECT = "select ";

    private static final String SELECT_COUNT = "select count(";

    private static final String SELECT_COUNT_DISTINCT = "select count(distinct ";

    private static final String SELECT_DISTINCT = "select distinct ";

    private static final String SET = "\nset ";

    private static final String UPDATE = "update ";

    private static final String WHERE = "\nwhere ";

    private static final String WITH = " with ";

    private static final String ON = " on ";

    private static final Map<JoinType, String> joinTypes = new EnumMap<JoinType, String>(JoinType.class);

    private final JPQLTemplates templates;

    private final TransactionalDataManager txDm;

    // Don't delete this, it will be needed to resolve common case into visitPathInCollection
    private final Metadata metadata;

    private boolean inProjection = false;

    private boolean inCaseOperation = false;

    static {
        joinTypes.put(JoinType.DEFAULT, COMMA);
        joinTypes.put(JoinType.FULLJOIN, "\n  full join ");
        joinTypes.put(JoinType.INNERJOIN, "\n  inner join ");
        joinTypes.put(JoinType.JOIN, "\n  inner join ");
        joinTypes.put(JoinType.LEFTJOIN, "\n  left join ");
        joinTypes.put(JoinType.RIGHTJOIN, "\n  right join ");
    }

    private boolean wrapElements = false;

    public CubaJpqlSerializer(JPQLTemplates templates,
                              TransactionalDataManager txDm,
                              Metadata metadata) {
        super(templates);
        this.templates = templates;
        this.txDm = txDm;
        this.metadata = metadata;
    }

    private String getEntityName(Class<?> clazz) {
        final Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        if (entityAnnotation != null && entityAnnotation.name().length() > 0) {
            return entityAnnotation.name();
        } else if (clazz.getPackage() != null) {
            String pn = clazz.getPackage().getName();
            return clazz.getName().substring(pn.length() + 1);
        } else {
            return clazz.getName();
        }
    }

    private void handleJoinTarget(JoinExpression je) {
        // type specifier
        if (je.getTarget() instanceof EntityPath<?>) {
            final EntityPath<?> pe = (EntityPath<?>) je.getTarget();
            if (pe.getMetadata().isRoot()) {
                append(getEntityName(pe.getType()));
                append(" ");
            }
            handle(je.getTarget());
        } else if (je.getTarget() instanceof Operation) {
            Operation<?> op = (Operation) je.getTarget();
            if (op.getOperator() == Ops.ALIAS) {
                boolean treat = false;
                if (Collection.class.isAssignableFrom(op.getArg(0).getType())) {
                    if (op.getArg(0) instanceof CollectionExpression) {
                        Class<?> par = ((CollectionExpression) op.getArg(0)).getParameter(0);
                        treat = !par.equals(op.getArg(1).getType());
                    }
                } else if (Map.class.isAssignableFrom(op.getArg(0).getType())) {
                    if (op.getArg(0) instanceof MapExpression) {
                        Class<?> par = ((MapExpression) op.getArg(0)).getParameter(1);
                        treat = !par.equals(op.getArg(1).getType());
                    }
                } else {
                    treat = !op.getArg(0).getType().equals(op.getArg(1).getType());
                }
                if (treat) {
                    Expression<?> entityName = ConstantImpl.create(getEntityName(op.getArg(1).getType()));
                    Expression<?> t = ExpressionUtils.operation(op.getType(), JPQLOps.TREAT, op.getArg(0), entityName);
                    op = ExpressionUtils.operation(op.getType(), Ops.ALIAS, t, op.getArg(1));
                }
            }
            handle(op);
        } else {
            handle(je.getTarget());
        }
    }

    public void serialize(QueryMetadata metadata, boolean forCountRow, @Nullable String projection) {
        final Expression<?> select = metadata.getProjection();
        final List<JoinExpression> joins = metadata.getJoins();
        final Predicate where = metadata.getWhere();
        final List<? extends Expression<?>> groupBy = metadata.getGroupBy();
        final Predicate having = metadata.getHaving();
        final List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        // select
        boolean inProjectionOrig = inProjection;
        inProjection = true;
        if (projection != null) {
            append(SELECT).append(projection).append("\n");

        } else if (forCountRow) {
            if (!groupBy.isEmpty()) {
                append(SELECT_COUNT_DISTINCT);
                handle(", ", groupBy);
            } else {
                if (!metadata.isDistinct()) {
                    append(SELECT_COUNT);
                } else {
                    append(SELECT_COUNT_DISTINCT);
                }
                if (select != null) {
                    if (select instanceof FactoryExpression) {
                        handle(joins.get(0).getTarget());
                    } else {
                        // TODO : make sure this works
                        handle(select);
                    }
                } else {
                    handle(joins.get(0).getTarget());
                }
            }
            append(")\n");

        } else if (select != null || !joins.isEmpty()) {
            if (!metadata.isDistinct()) {
                append(SELECT);
            } else {
                append(SELECT_DISTINCT);
            }
            if (select != null) {
                handle(select);
            } else {
                handle(joins.get(0).getTarget());
            }
            append("\n");

        }
        inProjection = inProjectionOrig;

        // from
        if (!joins.isEmpty()) {
            append(FROM);
            serializeSources(forCountRow, joins);
        }

        // where
        if (where != null) {
            append(WHERE).handle(where);
        }

        // group by
        if (!groupBy.isEmpty() && !forCountRow) {
            append(GROUP_BY).handle(COMMA, groupBy);
        }

        // having
        if (having != null) {
            append(HAVING).handle(having);
        }

        // order by
        if (!orderBy.isEmpty() && !forCountRow) {
            append(ORDER_BY);
            boolean first = true;
            for (final OrderSpecifier<?> os : orderBy) {
                if (!first) {
                    append(COMMA);
                }
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? " asc" : " desc");
                if (os.getNullHandling() == OrderSpecifier.NullHandling.NullsFirst) {
                    append(" nulls first");
                } else if (os.getNullHandling() == OrderSpecifier.NullHandling.NullsLast) {
                    append(" nulls last");
                }
                first = false;
            }
        }
    }

    public void serializeForDelete(QueryMetadata md) {
        append(DELETE);
        handleJoinTarget(md.getJoins().get(0));
        if (md.getWhere() != null) {
            append(WHERE).handle(md.getWhere());
        }
    }

    public void serializeForUpdate(QueryMetadata md, Map<Path<?>, Expression<?>> updates) {
        append(UPDATE);
        handleJoinTarget(md.getJoins().get(0));
        append(SET);
        boolean first = true;
        for (Map.Entry<Path<?>, Expression<?>> entry : updates.entrySet()) {
            if (!first) {
                append(", ");
            }
            handle(entry.getKey());
            append(" = ");
            handle(entry.getValue());
            first = false;
        }
        if (md.getWhere() != null) {
            append(WHERE).handle(md.getWhere());
        }
    }

    private void serializeSources(boolean forCountRow, List<JoinExpression> joins) {
        for (int i = 0; i < joins.size(); i++) {
            final JoinExpression je = joins.get(i);
            if (i > 0) {
                append(joinTypes.get(je.getType()));
            }
            if (je.hasFlag(JPAQueryMixin.FETCH) && !forCountRow) {
                handle(JPAQueryMixin.FETCH);
            }
            handleJoinTarget(je);
            // XXX Hibernate specific flag
            if (je.hasFlag(JPAQueryMixin.FETCH_ALL_PROPERTIES) && !forCountRow) {
                handle(JPAQueryMixin.FETCH_ALL_PROPERTIES);
            }

            if (je.getCondition() != null) {
                append(templates.isWithForOn() ? WITH : ON);
                handle(je.getCondition());
            }
        }
    }

    @Override
    public void visitConstant(Object constant) {
        if (inCaseOperation && templates.isCaseWithLiterals()) {
            if (constant instanceof Collection) {
                append("(");
                boolean first = true;
                for (Object o : (Collection) constant) {
                    if (!first) {
                        append(", ");
                    }
                    visitLiteral(o);
                    first = false;
                }
                append(")");
            } else {
                visitLiteral(constant);
            }
        } else {
            boolean wrap = templates.wrapConstant(constant);
            if (wrap) {
                append("(");
            }
            append(":").append(QUERY_PARAM_HOLDER);
            if (!getConstantToLabel().containsKey(constant)) {
                final String constLabel = String.valueOf(getConstantToLabel().size() + 1);
                getConstantToLabel().put(constant, constLabel);
                append(constLabel);
            } else {
                append(getConstantToLabel().get(constant));
            }
            if (wrap) {
                append(")");
            }
        }
    }

    public void visitLiteral(Object constant) {
        append(templates.asLiteral(constant));
    }

    @Override
    public Void visit(ParamExpression<?> param, Void context) {
        append(":" + QUERY_PARAM_HOLDER);
        if (!getConstantToLabel().containsKey(param)) {
            final String paramLabel = String.valueOf(getConstantToLabel().size() + 1);
            getConstantToLabel().put(param, paramLabel);
            append(paramLabel);
        } else {
            append(getConstantToLabel().get(param));
        }
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> query, Void context) {
        append("(");
        serialize(query.getMetadata(), false, null);
        append(")");
        return null;
    }

    @Override
    public Void visit(Path<?> expr, Void context) {
        // only wrap a PathCollection, if it the pathType is PROPERTY
        boolean wrap = wrapElements
                && (Collection.class.isAssignableFrom(expr.getType()) || Map.class.isAssignableFrom(expr.getType()))
                && expr.getMetadata().getPathType().equals(PathType.PROPERTY);
        if (wrap) {
            append("elements(");
        }
        super.visit(expr, context);
        if (wrap) {
            append(")");
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void visitOperation(Class<?> type, Operator operator, List<? extends Expression<?>> args) {
        boolean oldInCaseOperation = inCaseOperation;
        inCaseOperation = CASE_OPS.contains(operator);
        boolean oldWrapElements = wrapElements;
        wrapElements = templates.wrapElements(operator);

        if (operator == Ops.EQ && args.get(1) instanceof Operation &&
                ((Operation) args.get(1)).getOperator() == Ops.QuantOps.ANY) {
            args = ImmutableList.<Expression<?>>of(args.get(0), ((Operation) args.get(1)).getArg(0));
            visitOperation(type, Ops.IN, args);

        } else if (operator == Ops.NE && args.get(1) instanceof Operation &&
                ((Operation) args.get(1)).getOperator() == Ops.QuantOps.ANY) {
            args = ImmutableList.<Expression<?>>of(args.get(0), ((Operation) args.get(1)).getArg(0));
            visitOperation(type, Ops.NOT_IN, args);

        } else if (operator == Ops.IN || operator == Ops.NOT_IN) {
            if (args.get(1) instanceof Path) {
                visitAnyInPath(type, operator, args);
            } else if (args.get(0) instanceof Path && args.get(1) instanceof Constant<?>) {
                visitPathInCollection(type, operator, args);
            } else {
                super.visitOperation(type, operator, args);
            }

        } else if (operator == Ops.NUMCAST) {
            visitNumCast(args);

        } else if (operator == Ops.EXISTS && args.get(0) instanceof SubQueryExpression) {
            final SubQueryExpression subQuery = (SubQueryExpression) args.get(0);
            append("exists (");
            serialize(subQuery.getMetadata(), false, templates.getExistsProjection());
            append(")");

        } else if (operator == Ops.MATCHES || operator == Ops.MATCHES_IC) {
            super.visitOperation(type, Ops.LIKE,
                    ImmutableList.of(args.get(0), ExpressionUtils.regexToLike((Expression<String>) args.get(1))));

        } else if (operator == Ops.LIKE && args.get(1) instanceof Constant<?>) {
            final String escape = String.valueOf(templates.getEscapeChar());
            final String escaped = args.get(1).toString().replace(escape, escape + escape);
            super.visitOperation(String.class, Ops.LIKE,
                    ImmutableList.of(args.get(0), ConstantImpl.create(escaped)));

        } else if (NUMERIC.contains(operator)) {
            super.visitOperation(type, operator, normalizeNumericArgs(args));

        } else if (operator == Ops.ALIAS) {
            if (args.get(1) instanceof Path && !((Path<?>) args.get(1)).getMetadata().isRoot()) {
                Path<?> path = (Path<?>) args.get(1);
                args = ImmutableList.of(args.get(0),
                        ExpressionUtils.path(path.getType(), path.getMetadata().getName()));
            }
            super.visitOperation(type, operator, args);

        } else {
            super.visitOperation(type, operator, args);
        }

        inCaseOperation = oldInCaseOperation;
        wrapElements = oldWrapElements;
    }

    private void visitNumCast(List<? extends Expression<?>> args) {
        @SuppressWarnings("unchecked") //this is the second argument's type
                Constant<Class<?>> rightArg = (Constant<Class<?>>) args.get(1);

        final Class<?> targetType = rightArg.getConstant();
        final String typeName = templates.getTypeForCast(targetType);
        visitOperation(targetType, JPQLOps.CAST, ImmutableList.of(args.get(0), ConstantImpl.create(typeName)));
    }

    private void visitPathInCollection(Class<?> type, Operator operator,
                                       List<? extends Expression<?>> args) {
        Path<?> lhs = (Path<?>) args.get(0);
        @SuppressWarnings("unchecked")
        Constant<? extends Collection<?>> rhs = (Constant<? extends Collection<?>>) args.get(1);
        if (rhs.getConstant().isEmpty()) {
            operator = operator == Ops.IN ? Ops.EQ : Ops.NE;
            args = ImmutableList.of(Expressions.ONE, Expressions.TWO);
        } else if (txDm != null && !templates.isPathInEntitiesSupported() && args.get(0).getType().isAnnotationPresent(Entity.class)) {

            Set<Object> ids = new HashSet<Object>();

            Class<?> argType = args.get(0).getType();
            if (isClassUsualForCuba(argType)) {
                lhs = ExpressionUtils.path(UUID.class, lhs, "id");

                // turn rhs into id collection
                for (Object entity : rhs.getConstant()) {
                    ids.add(((BaseUuidEntity) entity).getId());
                }

            } else {
                // do something with metadata to find id
            }

            rhs = ConstantImpl.create(ids);
            args = ImmutableList.of(lhs, rhs);
        }

        super.visitOperation(type, operator, args);
    }

    private boolean isClassUsualForCuba(Class<?> clazz) {
        return BaseUuidEntity.class.isAssignableFrom(clazz);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private SingularAttribute<?,?> getIdProperty(EntityType entity) {
        final Set<SingularAttribute> singularAttributes = entity.getSingularAttributes();
        for (final SingularAttribute singularAttribute : singularAttributes) {
            if (singularAttribute.isId()) {
                return singularAttribute;
            }
        }
        return null;
    }

    private void visitAnyInPath(Class<?> type, Operator operator, List<? extends Expression<?>> args) {
        super.visitOperation(type,
                operator == Ops.IN ? JPQLOps.MEMBER_OF : JPQLOps.NOT_MEMBER_OF,
                args);
    }

    @SuppressWarnings("unchecked")
    private List<? extends Expression<?>> normalizeNumericArgs(List<? extends Expression<?>> args) {
        //we do not yet let it produce these types
        //we verify the types with isAssignableFrom()
        @SuppressWarnings("unchecked")
        List<? extends Expression<? extends Number>> potentialArgs =
                (List<? extends Expression<? extends Number>>) args;
        boolean hasConstants = false;
        Class<? extends Number> numType = null;
        for (Expression<? extends Number> arg : potentialArgs) {
            if (Number.class.isAssignableFrom(arg.getType())) {
                if (arg instanceof Constant<?>) {
                    hasConstants = true;
                } else {
                    numType = arg.getType();
                }
            }
        }
        if (hasConstants && numType != null) {
            //now we do let the potentialArgs help us
            final List<Expression<?>> newArgs = new ArrayList<Expression<?>>(args.size());
            for (final Expression<? extends Number> arg : potentialArgs) {
                if (arg instanceof Constant<?> && Number.class.isAssignableFrom(arg.getType())
                        && !arg.getType().equals(numType)) {
                    final Number number = ((Constant<? extends Number>) arg).getConstant();
                    newArgs.add(ConstantImpl.create(MathUtils.cast(number, numType)));
                } else {
                    newArgs.add(arg);
                }
            }
            return newArgs;
        } else {
            //the types are all non-constants, or not Number expressions
            return potentialArgs;
        }
    }
}
