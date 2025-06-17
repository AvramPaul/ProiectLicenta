package com.licenta.car_spotting_backend.services;

import com.licenta.car_spotting_backend.model.Post;
import com.licenta.car_spotting_backend.model.PostReaction;
import com.licenta.car_spotting_backend.model.User;
import com.licenta.car_spotting_backend.repository.PostReactionRepository;
import com.licenta.car_spotting_backend.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.licenta.car_spotting_backend.enums.ReactionType.DISLIKE;
import static com.licenta.car_spotting_backend.enums.ReactionType.LIKE;

@Service
@Transactional
public class PostReactionService {

    PostReactionRepository postReactionRepository;
    PostRepository postRepository;


    public PostReactionService(PostReactionRepository postReactionRepository, PostRepository postRepository) {
        this.postReactionRepository = postReactionRepository;
        this.postRepository = postRepository;
    }

    public boolean upvotePost(Post post, User user){  //Daca dam like, avem 3 situatii:

        Optional<PostReaction> postReaction = postReactionRepository.findByPostAndUser(post, user);
        if(postReaction.isPresent()){
            if(postReaction.get().getReactionType() == LIKE) { //Prima situatie: Am mai dat o data like, deci daca apasam iar se v-a sterge like-ul
                postReactionRepository.delete(postReaction.get());
                post.setScore(post.getScore()-1);
                postRepository.save(post);
                return false;
            }else if(postReaction.get().getReactionType() == DISLIKE){ //A doua situatie: Am dat o data dislike, deci daca apasam like se v-a sterge dislike-ul si se v-a pune like
                postReaction.get().setReactionType(LIKE);
                post.setScore(post.getScore() + 2);
                postRepository.save(post);
                return true;
            }
        }else{ //A treia situatie: Nu am dat nici like nici dislike sau s-a sters si atunci dam like-ul
            PostReaction newPostReaction = new PostReaction();
            newPostReaction.setPost(post);
            newPostReaction.setUser(user);
            newPostReaction.setReactionType(LIKE);
            postReactionRepository.save(newPostReaction);
            post.setScore(post.getScore()+1);
            postRepository.save(post);
            return true;
        }
        return false;
    }

    public boolean downvotePost(Post post, User user){
        Optional<PostReaction> postReaction = postReactionRepository.findByPostAndUser(post, user);
        if(postReaction.isPresent()){
            if(postReaction.get().getReactionType() == LIKE){
                postReaction.get().setReactionType(DISLIKE);
                post.setScore(post.getScore()-2);
                postRepository.save(post);
                return true;
            }else if(postReaction.get().getReactionType() == DISLIKE){
                postReactionRepository.delete(postReaction.get());
                post.setScore(post.getScore() + 1);
                postRepository.save(post);
                return false;
            }
        }else{
            PostReaction newPostReaction = new PostReaction();
            newPostReaction.setPost(post);
            newPostReaction.setUser(user);
            newPostReaction.setReactionType(DISLIKE);
            postReactionRepository.save(newPostReaction);
            post.setScore(post.getScore()-1);
            postRepository.save(post);
            return true;
        }
        return false;
    }
}
