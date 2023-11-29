package com.example.demo.controllers;

import com.example.demo.service.WebsiteReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website")
public class WebsiteReaderController {

    private final WebsiteReaderService websiteReaderService;

    @Autowired
    public WebsiteReaderController(WebsiteReaderService websiteReaderService) {
        this.websiteReaderService = websiteReaderService;
    }

    @GetMapping("/reader")
    public ResponseEntity<Map<String, List<String>>> getDropDownData() {
        Map<String, List<String>> dropDownList = websiteReaderService.collectInformation();
        if (dropDownList != null && !dropDownList.isEmpty()) {
            return ResponseEntity.ok(dropDownList);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}