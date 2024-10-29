package com.fill_form.controller;

import com.fill_form.models.dto.InfoPerson;
import com.fill_form.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@RestController
@RequestMapping("/document")
public class documentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/readDocxFile")
    public String readDocxFile(@RequestBody() InfoPerson InfoPerson) throws IOException{
        System.out.println("Endpoint /document/readDocxFile fue llamado.");

        documentService.readDocxFile(InfoPerson);

        return "Documento modificado y guardado como DOCX correctamente.";
    }

    @GetMapping("/test")
    public String test() {
        return "La aplicación está funcionando.";
    }
}
