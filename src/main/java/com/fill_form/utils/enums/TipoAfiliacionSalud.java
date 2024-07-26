package com.fill_form.utils.enums;

public enum TipoAfiliacionSalud {
    EPS("[eps]", 1),
    SISBEN("[sisben]", 2),
    PREPAGADA("[pre_pg]", 3),
    NINGUNA("[salud_nn]", 4);

    String var;
    Integer id;

    TipoAfiliacionSalud(String var, Integer id) {
        this.var = var;
        this.id = id;

    }

    public String var() {
        return var;
    }
    public Integer Id() {
        return id;
    }
    public static boolean contains(String test) {

        for (TipoAfiliacionSalud c : TipoAfiliacionSalud.values()) {
            if (c.var().equals(test)) {
                return true;
            }
        }

        return false;
    }

    public static boolean valueOfName(String name) {
        for (TipoAfiliacionSalud e : values()) {
            if (e.var().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
