package ru.udya.querydsl.cuba.core;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.jpa.TransformingIterator;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultSetType;
import org.eclipse.persistence.queries.Cursor;

import javax.persistence.Query;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

/**
 * {@code CubaHandler} is the {@link QueryHandler} implementation for CUBA.platform
 *
 * Based on {@link com.querydsl.jpa.EclipseLinkHandler}
 */
public class CubaHandler implements QueryHandler {

    @Override
    public void addEntity(Query query, String alias, Class<?> type) {
        // do nothing
    }

    @Override
    public void addScalar(Query query, String alias, Class<?> type) {
        // do nothing
    }

    @Override
    public boolean createNativeQueryTyped() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CloseableIterator<T> iterate(Query query, FactoryExpression<?> projection) {
        Iterator<T> iterator = null;
        Closeable closeable = null;
        if (query instanceof CubaQuery) {
            CubaQuery<T> elQuery = (CubaQuery<T>) query;
            elQuery.setHint(QueryHints.RESULT_SET_TYPE, ResultSetType.ForwardOnly);
            elQuery.setHint(QueryHints.SCROLLABLE_CURSOR, true);
            final Cursor cursor = null;// = elQuery.getResultCursor();
            closeable = new Closeable() {
                @Override
                public void close() throws IOException {
                    cursor.close();
                }
            };
            iterator = cursor;
        } else {
            iterator = query.getResultList().iterator();
        }
        if (projection != null) {
            return new TransformingIterator<T>(iterator, closeable, projection);
        } else {
            return new IteratorAdapter<T>(iterator, closeable);
        }
    }

    @Override
    public boolean transform(Query query, FactoryExpression<?> projection) {
        return false;
    }

    @Override
    public boolean wrapEntityProjections() {
        return false;
    }
}
