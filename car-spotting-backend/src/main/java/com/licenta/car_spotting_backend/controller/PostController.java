package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.dto.JsonResponse;
import com.licenta.car_spotting_backend.services.ClassifierService;
import com.licenta.car_spotting_backend.services.PostReactionService;
import com.licenta.car_spotting_backend.services.PostServices;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassifierService classifierService;
    @Autowired
    private PostReactionService postReactionService;
    @Autowired
    private PostServices postServices;

    public PostController(PostRepository postRepository, UserRepository userRepository, ClassifierService classifierService, PostReactionService postReactionService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.classifierService = classifierService;
        this.postReactionService = postReactionService;
    }

    @GetMapping("/feed")
    public ResponseEntity<PagedModel<EntityModel<PostDetailsDTO>>> getAllPosts(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "score") String sortBy,
                @RequestParam(defaultValue = "desc") String sortDirection,
                PagedResourcesAssembler<PostDetailsDTO> pagedResourcesAssembler ){
        /*
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
        Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow( () -> new RuntimeException("User not found") );
        Long userId = user.getId();
        Page<PostDetailsDTO> postDetailsPage = postRepository.findAllPostDetailsForUser(userId, pageable);

        org.springframework.hateoas.PagedModel<EntityModel<PostDetailsDTO>> pagedModel = pagedResourcesAssembler.toModel(postDetailsPage);
        */
        return ResponseEntity.ok().body(postServices.getAllPosts(page, size, sortBy, sortDirection, pagedResourcesAssembler));

    }
    @GetMapping("/images/{filename}")
    public ResponseEntity<org.springframework.core.io.Resource> getImage(@PathVariable String filename) throws MalformedURLException {
        Path imagePath = Paths.get("C:\\Users\\avram\\Desktop\\Model\\photoUploads\\"+filename);
        org.springframework.core.io.Resource resource = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
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
    public ResponseEntity<JsonResponse> upvote(@PathVariable Long postID){

        JsonResponse response = postServices.upvotePost(postID);
        if(response.status == 200){
            return ResponseEntity.status(200).body(response);
        }else if(response.status == 202){
            return ResponseEntity.status(202).body(response);
        }else if(response.status == 404){
            return ResponseEntity.status(404).body(response);
        }else{
            return ResponseEntity.status(500).body(response);
        }
    }
    @PutMapping("/{postID}/downvote")
    public ResponseEntity<JsonResponse> downvote(@PathVariable Long postID){

        JsonResponse response = postServices.downvotePost(postID);
        if(response.status == 200){
            return ResponseEntity.status(200).body(response);
        }else if (response.status == 202){
            return ResponseEntity.status(200).body(response);
        }else if (response.status == 404){
            return ResponseEntity.status(404).body(response);
        }else{
            return ResponseEntity.status(500).body(response);
        }
    }

}
