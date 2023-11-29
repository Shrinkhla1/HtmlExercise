package com.example.demo.helper;

import com.example.demo.model.Binder;
import com.example.demo.model.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class DownloadWriteHelper {

    private static final String DIRECTORY_PATH = "C:\\Users\\U6075110\\OneDrive - Clarivate Analytics\\Documents\\bots";
    private static String convertBinderToString(Binder binder) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(binder);
        } catch (JsonProcessingException e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_04);
        }
    }

    public void downloadPdf(String pdfUrl, String destinationPath, String fileName) {
        try {
            URL url = new URL(pdfUrl);
            Path combinedPath = Paths.get(destinationPath, fileName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try (InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, combinedPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_05);
        }
    }

    public void writeBinderToJsFile(Binder binder) {

        String binderResult = convertBinderToString(binder);
        String fileName = binder.getDecisions().getReference() + ".js";
        writeContentToSystem(DIRECTORY_PATH, fileName, binderResult);
    }

    public void writeContentToSystem(String directoryPath, String fileName, String content) {
        try {
            Path combinedPath = Paths.get(directoryPath, fileName);
            final File outputFile = combinedPath.toFile();
            Path path = Paths.get(outputFile.toURI());
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_06);
        }
    }

}
