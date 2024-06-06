package com.fill_form.service.impl;

import com.fill_form.models.dto.InfoPerson;
import com.fill_form.service.DocumentService;
import com.fill_form.models.dto.VariableDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Scanner;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Override
    public String readDocxFile(InfoPerson infoPerson) throws IOException {
        String docxPath = "D:\\Saez\\Proyects\\fill-form\\resorces\\PlantillaFormularioAdultos.docx";
        String modifiedDocxPath = new StringBuilder("D:\\Saez\\Proyects\\fill-form\\resorces\\Formularios\\").append(infoPerson.getName()).append(".docx").toString();
        List<VariableDTO> variablesList = infoPerson.getVariable();
        try (FileInputStream fis = new FileInputStream(docxPath); XWPFDocument document = new XWPFDocument(fis)) {

            // Modificar el contenido del documento
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                for (XWPFRun run : para.getRuns()) {
                    String text = run.getText(0);
                    if (text != null) {
                        for (VariableDTO var : variablesList) {
                            if (text.contains(var.getName())) {
                                if (var.getName().equals("[firma-img]")) {
                                    // Insertar la imagen
                                    text = text.replace(var.getName(), "");
                                    run.setText(text, 0);
                                    try (FileInputStream is = new FileInputStream(var.getValue())) {
                                        run.addPicture(is, Document.PICTURE_TYPE_PNG, var.getValue(), Units.toEMU(180), Units.toEMU(80));
                                    }
                                } else {
                                    text = text.replace(var.getName(), var.getValue());
                                    run.setText(text, 0);
                                }
                            }
                        }
                        run.setText(text, 0);
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(modifiedDocxPath)) {
                document.write(fos);
            }


            return "Documento modificado y guardado como DOCX correctamente.";
        } catch (IOException | InvalidFormatException e) {
            throw new IOException("Error reading or writing file", e);
        }
    }

    @Override
    public String readTxtFile(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        Scanner sc = new Scanner(fis);
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine());
        }
        fis.close();
        return sb.toString();
    }


    public void convertDocxToPdf(String docxPath, String pdfPath) throws IOException, Docx4JException {
        try {
            // Carga el documento DOCX
            FileInputStream fis = new FileInputStream(new File(docxPath));
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(fis);

            // Crea un OutputStream para guardar el PDF
            OutputStream os = new FileOutputStream(new File(pdfPath));

            // Convierte el documento a PDF
            Docx4J.toPDF(wordMLPackage, os);

            // Cierra los streams
            os.flush();
            os.close();
            fis.close();

            System.out.println("El documento fue convertido a PDF con éxito.");
        } catch (Docx4JException | IOException e) {
            e.printStackTrace();

        }

    }
}

