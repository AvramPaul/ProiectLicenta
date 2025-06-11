package com.licenta.car_spotting_backend.services;

import com.licenta.car_spotting_backend.dto.JsonResponse;
import com.licenta.car_spotting_backend.dto.PostDetailsDTO;
import com.licenta.car_spotting_backend.dto.UserPostWithReactionsDTO;
import com.licenta.car_spotting_backend.enums.ReactionType;
import com.licenta.car_spotting_backend.model.Post;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.PostRepository;
import com.licenta.car_spotting_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServices {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassifierService classifierService;
    @Autowired
    private PostReactionService postReactionService;

    public PagedModel<EntityModel<PostDetailsDTO>> getAllPosts(int page, int size, String sortBy, String sortDirection, PagedResourcesAssembler<PostDetailsDTO> pagedResourcesAssembler ){
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow( () -> new RuntimeException("User not found") );
        Long userId = user.getId();
        Page<PostDetailsDTO> postDetailsPage = postRepository.findAllPostDetailsForUser(userId, pageable);

        org.springframework.hateoas.PagedModel<EntityModel<PostDetailsDTO>> pagedModel = pagedResourcesAssembler.toModel(postDetailsPage);

        return pagedModel;
    }

    public List<UserPostWithReactionsDTO> getUserPosts(Long userId){
        List<Post> posts = postRepository.findPostsByUserId(userId);
        List<UserPostWithReactionsDTO> result = new ArrayList<>();

        for(Post post : posts){
            List<String> likes_name = postRepository.findUsernamesByPostIdAndReactionType(post.getId(), ReactionType.LIKE);
            List<String> dislikes_name = postRepository.findUsernamesByPostIdAndReactionType(post.getId(), ReactionType.DISLIKE);

            UserPostWithReactionsDTO dto = new UserPostWithReactionsDTO(
                    post.getId(),
                    post.getScore(),
                    post.getCar().getMake(),
                    post.getCar().getModel(),
                    post.getCar().getYear(),
                    post.getCar().getImagePath(),
                    likes_name,
                    dislikes_name
            );
            result.add(dto);
        }
        return result;
    }

    public JsonResponse upvotePost(Long postId){

        Optional<Post> optionalPost = postRepository.findById(postId); //aici verificam daca id-ul postarii din URL exista
        if(optionalPost.isPresent()) { //daca exista
            Post post = optionalPost.get(); //trebuie sa facem un obiect post nu merge cu obiectul optional
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (postReactionService.upvotePost(post, user)){
                return JsonResponse.create(200, "Like la postare");
            }else{
                return JsonResponse.create(202, "S-a scos like-ul de la postare");
            }
        }else{
            return JsonResponse.create(404, "Postarea nu a fost gasita");
        }
    }

    public JsonResponse downvotePost(Long postId){
        Optional<Post> optionalPost = postRepository.findById(postId); //aici verificam daca id-ul postarii din URL exista
        if(optionalPost.isPresent()){ //daca exista
            Post post = optionalPost.get(); //trebuie sa facem un obiect post nu merge cu obiectul optional
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(postReactionService.downvotePost(post, user) ){
                return JsonResponse.create(200, "Dislike la postare");
            }else{
                return JsonResponse.create(202, "S-a scos dislike-ul de la postare");
            }

        }else{
            return JsonResponse.create(404, "Postarea nu a fost gasita");
        }
    }
}
