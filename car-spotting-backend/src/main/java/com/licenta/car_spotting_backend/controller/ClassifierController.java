package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.services.ClassifierService;
import com.licenta.car_spotting_backend.dto.ClassifyingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classifier")
@CrossOrigin(origins = "*")
public class ClassifierController {

    private final ClassifierService classifierService;

    @Autowired
    public ClassifierController(ClassifierService classifierService) {
        this.classifierService = classifierService;
    }


    @GetMapping("/classify")
    public ClassifyingResponse classifyImage(@RequestParam("image") String imageLocation) {
        return classifierService.runPythonScript(imageLocation);
    }
}
