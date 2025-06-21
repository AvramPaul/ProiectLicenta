package com.licenta.car_spotting_backend.model;

import com.licenta.car_spotting_backend.enums.ReactionType;
import jakarta.persistence.*;

@Entity
@Table(
        name = "post_reactions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"})
)
public class PostReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    public PostReaction() {}

    public PostReaction(Post post, User user, ReactionType reactionType) {
        this.post = post;
        this.user = user;
        this.reactionType = reactionType;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ReactionType getReactionType() { return reactionType; }
    public void setReactionType(ReactionType reactionType) { this.reactionType = reactionType; }
}

