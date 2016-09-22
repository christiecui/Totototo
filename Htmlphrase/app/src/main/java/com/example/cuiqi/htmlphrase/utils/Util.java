package com.example.cuiqi.htmlphrase.utils;

/**
 * Created by cuiqi on 16/7/6.
 */

public final class Util {
    public static boolean isNullOrNil(final String object) {
        if ((object == null) || (object.length() <= 0)) {
            return true;
        }
        return false;
    }
}
