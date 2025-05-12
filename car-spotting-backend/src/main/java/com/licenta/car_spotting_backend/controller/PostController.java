package com.licenta.car_spotting_backend.controller;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.licenta.car_spotting_backend.dto.PostDetailsDTO;
import com.licenta.car_spotting_backend.dto.PostRequestDTO;
import com.licenta.car_spotting_backend.model.Car;
import com.licenta.car_spotting_backend.model.Post;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.PostRepository;
import com.licenta.car_spotting_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/feed")
    public Page<PostDetailsDTO> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection){
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
        Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return postRepository.findAllPostDetails(pageable);
    }


    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostRequestDTO postRequestDTO){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //aici luam din SecurityContextHolder cine e logat la momentul actual

        Car car = new Car();
        car.setMake(postRequestDTO.getMake());
        car.setModel(postRequestDTO.getModel());
        car.setYear(postRequestDTO.getYear());
        car.setImagePath(null);

        Post post = new Post();
        post.setScore(0); // Scorul începe de la 0
        post.setCar(car);
        post.setUser(user);

        postRepository.save(post);

        return ResponseEntity.status(201).body("{\"message\": \"Post created successfully!\"}");
    }

    @PutMapping("/{postID}/upvote")
    public ResponseEntity<String> upvote(@PathVariable Long postID){

        Optional<Post> optionalPost = postRepository.findById(postID); //aici verificam daca id-ul postarii din URL exista
        if(optionalPost.isPresent()){ //daca exista
            Post post = optionalPost.get(); //trebuie sa facem un obiect post nu merge cu obiectul optional
            post.setScore(post.getScore()+1); //incrementam scorul
            postRepository.save(post); //salvam in repository scorul postarii caruia i-am incremetnat scorul

            return ResponseEntity.ok("Like la postarea "+postID);
        }else{
            return ResponseEntity.status(404).body("Postarea nu a fost gasita");
        }

    }
    @PutMapping("/{postID}/downvote")
    public ResponseEntity<String> downvote(@PathVariable Long postID){

        Optional<Post> optionalPost = postRepository.findById(postID); //aici verificam daca id-ul postarii din URL exista
        if(optionalPost.isPresent()){ //daca exista
            Post post = optionalPost.get(); //trebuie sa facem un obiect post nu merge cu obiectul optional
            post.setScore(post.getScore()-1); //decrementam scorul
            postRepository.save(post); //salvam in repository scorul postarii caruia i-am incremetnat scorul

            return ResponseEntity.ok("Dislike la postarea "+postID);
        }else{
            return ResponseEntity.status(404).body("Postarea nu a fost gasita"); //Avem o problema aici ca un utilizator poate da like sau dislike de mai multe ori,
        }

    }

}
