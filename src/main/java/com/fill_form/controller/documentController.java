package com.fill_form.controller;

import com.fill_form.models.dto.InfoPerson;
import com.fill_form.models.dto.VariableDTO;
import com.fill_form.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/document")
public class documentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/readDocxFile")
    public String readDocxFile(@RequestBody() InfoPerson InfoPerson) throws IOException{
        return documentService.readDocxFile(InfoPerson);
    }
}
