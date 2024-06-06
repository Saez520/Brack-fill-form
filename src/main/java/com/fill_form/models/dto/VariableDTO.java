package com.fill_form.models.dto;

public class VariableDTO {
    private String name;
    private String value;

    public VariableDTO() {
    }

    public VariableDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
