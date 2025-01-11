package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.dto.PostDetailsDTO;
import com.licenta.car_spotting_backend.dto.PostRequestDTO;
import com.licenta.car_spotting_backend.model.Car;
import com.licenta.car_spotting_backend.model.Post;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.PostRepository;
import com.licenta.car_spotting_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/feed")
    public List<PostDetailsDTO> getAllPosts() { return postRepository.findAllPostDetails(); }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostRequestDTO postRequestDTO){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Car car = new Car();
        car.setMake(postRequestDTO.getMake());
        car.setModel(postRequestDTO.getModel());
        car.setYear(postRequestDTO.getYear());
        car.setImage(postRequestDTO.getImage());

        Post post = new Post();
        post.setScore(0); // Scorul începe de la 0
        post.setCar(car);
        post.setUser(user);

        postRepository.save(post);

        return ResponseEntity.ok("Post created succesfully !");
    }

}
