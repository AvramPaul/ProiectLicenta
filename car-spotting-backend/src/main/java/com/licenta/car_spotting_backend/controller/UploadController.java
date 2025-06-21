package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.services.ClassifierService;
import com.licenta.car_spotting_backend.model.Car;
import com.licenta.car_spotting_backend.dto.ClassifyingResponse;
import com.licenta.car_spotting_backend.model.Post;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.PostRepository;
import com.licenta.car_spotting_backend.services.UploadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    UploadServices uploadServices;


    @PostMapping("/upload")
    public ResponseEntity<ClassifyingResponse> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(uploadServices.uploadImage(file));
    }
}
