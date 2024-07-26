package com.fill_form.models.dto;

import java.util.List;

public class InfoPerson {
    private String name;
    private List<VariableDTO> variable;
    private static String typeForm;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VariableDTO> getVariable() {
        return variable;
    }

    public void setVariable(List<VariableDTO> variable) {
        this.variable = variable;
    }

    public static String getTypeForm() {
        return typeForm;
    }

    public void setTypeForm(String typeForm) {
        this.typeForm = typeForm;
    }
}
