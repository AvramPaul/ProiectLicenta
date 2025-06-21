package com.licenta.car_spotting_backend.repository;

import com.licenta.car_spotting_backend.model.Post;
import com.licenta.car_spotting_backend.model.PostReaction;
import com.licenta.car_spotting_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByPostAndUser(Post post, User user);
}
