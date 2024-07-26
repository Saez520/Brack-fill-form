package com.fill_form.utils.enums;

import java.util.List;

public enum TypeFirm {
    FIRMA_ADULTO("[firma_adulto]"),
    FIRMA_PADRE("[firma_padre]"),
    FIRMA_MADRE("[firma_madre]");

    String varFirma;


    TypeFirm(String varFirma) {
        this.varFirma = varFirma;
    }

    public String varFirma() {
        return varFirma;
    }

    public static boolean contains(String test) {

        for (TypeFirm c : TypeFirm.values()) {
            if (c.varFirma().equals(test)) {
                return true;
            }
        }

        return false;
    }

    public static boolean valueOfName(String name) {
        for (TypeFirm e : values()) {
            if (e.varFirma().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
