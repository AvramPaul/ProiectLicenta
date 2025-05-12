package com.licenta.car_spotting_backend.repository;

import com.licenta.car_spotting_backend.dto.PostDetailsDTO;
import com.licenta.car_spotting_backend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.ui.Model;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT new com.licenta.car_spotting_backend.dto.PostDetailsDTO(p.id, u.username, c.make, c.model, c.year, c.imagePath, p.score) " +
            "FROM Post p " +
            "JOIN p.user u " +
            "JOIN p.car c")
    Page<PostDetailsDTO> findAllPostDetails(Pageable pageable);
}
