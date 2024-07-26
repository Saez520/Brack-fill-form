package com.fill_form.service.impl;

import com.fill_form.models.dto.InfoPerson;
import com.fill_form.service.DocumentService;
import com.fill_form.models.dto.VariableDTO;
import com.fill_form.utils.enums.JUtils;
import com.fill_form.utils.enums.TipoAfiliacionSalud;
import com.fill_form.utils.enums.TypeFirm;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final String MarcacionX = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAUCAYAAAB1aeb6AAAABHNCSVQICAgIfAhkiAAAAVZJREFUSIljFFC1/89Ad/D/x4fbhziZ6G8xAwMDAyMHAwMDAwuMy69iR3MrP945hMIfIJ8PNcvdLDUZ7mxuYHAxV4eLsbIwM2zsT2NY3Z1MW8v3n77FcPXOM4b6dC8Gfh5OBgYGBgZHUzUGDUUJhvyuNbS1/PefvwzVUzcziAjyMIS6GjJwcbAxdOT5MWw8cInh2euPJFvOQlgJKrhy5znDzDVHGErjXRiUZEQYvn7/xdA6ZyfJFjMwkJngZq09yvD1+0+GKE8ThoWbTzJ8/PKdfpZTC5BleWe+P8PdJ28YJizdz1Aa78IgJcpPH8vdLDUZPG20GWauOcIwdeUhhhv3XzBMLAuhveWsLMwM2WG2DOdvPGbYc/Imw+8/fxmmrjrMYKghi5L3aWJ5drgdRp7GlveJBYywKpXeFcuH2wcZBzS1wwsZ9OqOHmDo5XPKwf8ZDAwMDAD39mt+abQLcAAAAABJRU5ErkJggg==";
    private static final String MarcacionVacia = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAUCAYAAAB1aeb6AAAABHNCSVQICAgIfAhkiAAAAFVJREFUSIljFFC1/89Ad/D/x4fbhziZ6G8xAwMDAyMHAwMDAwuMy69iR3MrP945hMIfIJ+PWj5q+ajlo5aPWj5qOQ0AvEpFr+7oAUZisP+fwcDAwAAAkdQNW0qOLYcAAAAASUVORK5CYII=";
    private static final String urlForms = "\\Saez\\Proyects\\fill-form\\Back\\resorces\\";

    @Override
    public String readDocxFile(InfoPerson infoPerson) throws IOException {
        String modifiedDocxPath = new StringBuilder(urlForms).append("\\Formularios\\").append(infoPerson.getName()).append(".docx").toString();

        List<VariableDTO> variablesList = infoPerson.getVariable();
        try (FileInputStream fis = new FileInputStream(validateFormType(infoPerson.getTypeForm()));
             XWPFDocument document = new XWPFDocument(fis)) {

            // Modificar el contenido del documento
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                for (XWPFRun run : para.getRuns()) {
                    String text = run.getText(0);
                    if (text != null) {
                        for (VariableDTO var : variablesList) {
                            if (text.contains(var.getName())) {
                                if (checkIfImage(run, var, text)) {
                                    text = text.replace(var.getName(), "");
                                    run.setText(text, 0);
                                } else {
                                    text = text.replace(var.getName(), var.getValue());
                                    run.setText(text, 0);
                                }
                            }
                        }
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(modifiedDocxPath)) {
                document.write(fos);
            }


            return "Documento modificado y guardado como DOCX correctamente.";
        } catch (IOException e) {
            throw new IOException("Error reading or writing file", e);
        }
    }

    private String validateFormType(String typeForm) {
        if (typeForm.equals("adulto")){
            return new StringBuilder(urlForms).append("PlantillaFormularioAdultos.docx").toString();
        } else if (InfoPerson.getTypeForm().equals("menor")){
            return new StringBuilder(urlForms).append("PlantillaFormularioMenores.docx").toString();
        } else {
            throw new IllegalArgumentException("Tipo de formulario no válido");
        }
    }

    private boolean checkIfImage(XWPFRun run, VariableDTO var, String text) throws IOException{
        if (TypeFirm.valueOfName(var.getName())) {
            String firmPadre;
            String firmaMadre;
            if (TypeFirm.FIRMA_PADRE.varFirma().equals(var.getName())) firmPadre = var.getValue(); return true;
            if (TypeFirm.FIRMA_MADRE.varFirma().equals(var.getName())) firmaMadre = var.getValue(); return true;
            if (JUtils.isEmptyNull(firmPadre) && JUtils.isEmptyNull(firmaMadre) {
                addImagesInOneLine(run, var.getValue(), 180, 80);
            }
            replaceTextWithImage(run, var.getValue(), 180, 80);
            return true;
        }

        if (TipoAfiliacionSalud.valueOfName(var.getName())) {
            replaceTextWithImage(run, var.getValue().equals("X") ? MarcacionX : MarcacionVacia, 18,13);
            return true;
        }
        return false;
    }

    public static void replaceTextWithImage(XWPFRun run, String base64Image, Integer widthEmu, Integer heigthEmu) throws IOException {
        // Verificar y eliminar el prefijo de la cadena base64 si existe
        if (base64Image.startsWith("data:image/png;base64,")) {
            base64Image = base64Image.substring("data:image/png;base64,".length());
        }

        try {
            // Decodificar el string base64 a bytes
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Crear un archivo temporal para la imagen
            File tempFile = File.createTempFile("image", ".png");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(imageBytes);
            }

            // Insertar la imagen
            try (FileInputStream is = new FileInputStream(tempFile)) {
                run.addPicture(is, Document.PICTURE_TYPE_PNG, tempFile.getName(), Units.toEMU(widthEmu), Units.toEMU(heigthEmu));
            }

            // Eliminar el archivo temporal
            tempFile.delete();
        } catch (IllegalArgumentException | InvalidFormatException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addImagesInOneLine(XWPFDocument doc, String[] base64Images, Integer widthEmu, Integer heightEmu) throws IOException {
        XWPFParagraph paragraph = doc.createParagraph();
        for (String base64Image : base64Images) {
            XWPFRun run = paragraph.createRun();
            replaceTextWithImage(run, base64Image, widthEmu, heightEmu);
            run.addTab(); // Añade un tabulador entre imágenes para asegurarse de que estén alineadas horizontalmente
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

