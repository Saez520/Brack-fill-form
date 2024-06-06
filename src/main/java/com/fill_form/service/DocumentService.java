package com.fill_form.service;


import com.fill_form.models.dto.InfoPerson;
import com.fill_form.models.dto.VariableDTO;
import java.io.IOException;
import java.util.List;



public interface DocumentService {

    public String readDocxFile(InfoPerson varaiblesList) throws IOException;

    public String readTxtFile(String path) throws IOException;
}
