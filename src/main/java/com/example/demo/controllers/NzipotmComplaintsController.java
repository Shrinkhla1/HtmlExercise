package com.example.demo.controllers;

import com.example.demo.model.Binder;
import com.example.demo.service.NzipotmComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NzipotmComplaintsController {

    private final NzipotmComplaintsService nzipotmComplaintsService;
    @Autowired
    public NzipotmComplaintsController(NzipotmComplaintsService nzipotmComplaintsService) {
        this.nzipotmComplaintsService = nzipotmComplaintsService;
    }
    @GetMapping("/robot")
    public ResponseEntity<Binder> runNzipotmComplaintsRobot() throws InterruptedException {
        Binder outputAfterExecution = nzipotmComplaintsService.runNzipotmComplaintsRobot();
        if(outputAfterExecution!=null)
            return ResponseEntity.ok(outputAfterExecution);
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

