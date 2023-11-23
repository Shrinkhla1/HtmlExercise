package com.example.demo.helper;

import com.example.demo.model.Binder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class DownloadWriteHelper {
    public void downloadPdf(String pdfUrl, String destinationPath, String fileName)  {
        try {
            URL url = new URL(pdfUrl);
            Path combinedPath = Paths.get(destinationPath, fileName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try (InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, combinedPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void writeBinderToJsFile(Binder binder) {

            String binderResult=convertBinderToString(binder);
            String directoryPath = "C:\\Users\\U6075110\\OneDrive - Clarivate Analytics\\Documents\\bots";
            String fileName = binder.getDecisions().getReference() + ".js";
            writeContentToSystem(directoryPath,fileName,binderResult);
      }

    public void writeContentToSystem(String directoryPath, String fileName, String content) {
        try
        {
        Path combinedPath = Paths.get(directoryPath, fileName);
        final File outputFile = combinedPath.toFile();
        Path path = Paths.get(outputFile.toURI());
        Files.write(path, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertBinderToString(Binder binder) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(binder);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
