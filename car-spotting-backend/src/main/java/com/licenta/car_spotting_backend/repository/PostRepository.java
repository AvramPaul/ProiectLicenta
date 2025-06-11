package com.licenta.car_spotting_backend.repository;

import com.licenta.car_spotting_backend.dto.PostDetailsDTO;
import com.licenta.car_spotting_backend.dto.UserPostWithReactionsDTO;
import com.licenta.car_spotting_backend.enums.ReactionType;
import com.licenta.car_spotting_backend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT new com.licenta.car_spotting_backend.dto.PostDetailsDTO(p.id, u.username, c.make, c.model, c.year, c.imagePath, p.score, r.reactionType) " +
            "FROM Post p " +
            "JOIN p.user u " +
            "JOIN p.car c " +
            "LEFT JOIN PostReaction r ON r.post = p AND r.user.id = :userId "
            )
    Page<PostDetailsDTO> findAllPostDetailsForUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.car WHERE p.user.id = :userId")
    List<Post> findPostsByUserId(@Param("userId") Long userId);

    @Query("SELECT pr.user.username FROM PostReaction pr WHERE pr.post.id = :postId AND pr.reactionType = :reactionType")
    List<String> findUsernamesByPostIdAndReactionType(@Param("postId") Long postId, @Param("reactionType") ReactionType reactionType);

}
