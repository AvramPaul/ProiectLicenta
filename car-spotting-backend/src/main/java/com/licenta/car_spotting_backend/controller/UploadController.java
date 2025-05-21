package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.services.ClassifierService;
import com.licenta.car_spotting_backend.model.Car;
import com.licenta.car_spotting_backend.dto.ClassifyingResponse;
import com.licenta.car_spotting_backend.model.Post;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.PostRepository;
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
    private final ClassifierService classifierService;
    private final PostRepository postRepository;

    @Autowired
    public UploadController(ClassifierService classifierService, PostRepository postRepository) {
        this.classifierService = classifierService;
        this.postRepository = postRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<ClassifyingResponse> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {

        // Salvăm imaginea
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadDir = "C:\\Users\\avram\\Desktop\\Model\\photoUploads\\";
        Path path = Paths.get(uploadDir + filename);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Nu encodezi path-ul dacă nu îl trimiți prin URL. DOAR îl folosești local:
        String fullPath = uploadDir + filename;

        // Apelezi direct serviciul Spring
        ClassifyingResponse classifyingResponse = classifierService.runPythonScript(fullPath);

        // Creezi obiectele din baza de date
        Car car = new Car();
        car.setImagePath(filename);
        car.setMake(classifyingResponse.getCarMake());
        car.setModel(classifyingResponse.getCarModel());
        car.setYear(classifyingResponse.getCarYear());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = new Post();
        post.setCar(car);
        post.setUser(user);
        post.setScore(0);

        postRepository.save(post);

        return ResponseEntity.ok(classifyingResponse);
    }
}
