package com.fill_form.utils.enums;

public class JUtils {
    public static boolean isEmptyNull(String valor) {
        return valor == null || valor.isEmpty() || valor.trim().isEmpty();
    }
}
