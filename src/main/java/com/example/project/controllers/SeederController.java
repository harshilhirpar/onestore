package com.example.project.controllers;

import com.example.project.services.SeederService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seed")
public class SeederController {
    private final SeederService seederService;

    public SeederController(SeederService seederService) {
        this.seederService = seederService;
    }

//    TODO: ONLY HIT THIS WHEN YOU DO CREATE-DROP FOR RELATIONSHIP
    @GetMapping()
    public void doSeed(){
        seederService.loadData();
    }
}
