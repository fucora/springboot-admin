package com.mm.common.validator;

import com.mm.common.exception.GException;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据校验
 *
 * @author lwl
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new GException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new GException(message);
        }
    }
}
