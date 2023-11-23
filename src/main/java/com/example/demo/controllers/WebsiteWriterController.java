package com.example.demo.controllers;

import com.example.demo.service.WebsiteWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/website")
public class WebsiteWriterController {

    private final WebsiteWriterService websiteWriterService;
    @Autowired
    public WebsiteWriterController(WebsiteWriterService websiteWriterService) {
        this.websiteWriterService = websiteWriterService;
    }
    @GetMapping("/writer")
    public ResponseEntity<String> getResult() {
        String outputAfterExecution = websiteWriterService.getOutputAfterExecution();
        if(outputAfterExecution!=null && !outputAfterExecution.isEmpty())
            return ResponseEntity.ok(outputAfterExecution);
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
