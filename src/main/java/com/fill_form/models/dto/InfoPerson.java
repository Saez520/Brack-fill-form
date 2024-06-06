package com.fill_form.models.dto;

import java.util.List;

public class InfoPerson {
    private String name;
    private List<VariableDTO> variable;

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
}
