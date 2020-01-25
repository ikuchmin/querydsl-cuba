package ru.udya.querydsl.cuba.core;

import com.haulmont.cuba.core.global.LoadContext;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.dsl.Param;

import java.util.Map;


/**
 * Based on JPAUtil
 */
public class CubaUtil {
    public static void setConstants(LoadContext.Query query, Map<Object, String> constants, Map<ParamExpression<?>, Object> params) {

        for (Map.Entry<Object, String> entry : constants.entrySet()) {

            String key = entry.getValue();

            Object val = entry.getKey();

            if (val instanceof Param) {
                val = params.get(val);
                if (val == null) {
                    throw new ParamNotSetException((Param<?>) entry.getKey());
                }
            }

            query.setParameter(CubaJpqlSerializer.QUERY_PARAM_HOLDER + key, val);
        }
    }
}
