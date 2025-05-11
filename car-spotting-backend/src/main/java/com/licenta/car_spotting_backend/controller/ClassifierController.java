package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.classifier.ClassifierService;
import com.licenta.car_spotting_backend.model.ClassifyingResponse;
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

    // Endpoint: http://localhost:8080/api/classifier/run?image=calea/catre/00001.jpg
    @GetMapping("/classify")
    public ClassifyingResponse classifyImage(@RequestParam("image") String imageLocation) {
        return classifierService.runPythonScript(imageLocation);
    }
}
