package com.example.car_spotting_front_end.Response;

import com.example.car_spotting_front_end.model.Post;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostsResponse {
    @SerializedName("_embedded")
    private Embedded embedded;

    @SerializedName("page")
    private PageMetadata page;

    public List<Post> getPostList() {
        return embedded != null ? embedded.getPosts() : new ArrayList<>();
    }

    public PageMetadata getPage() {
        return page;
    }

    public static class Embedded {
        @SerializedName("postDetailsDTOList")
        private List<Post> posts;

        public List<Post> getPosts() {
            return posts;
        }
    }

    public static class PageMetadata {
        private int size;
        private int totalElements;
        private int totalPages;
        private int number;

        public int getTotalPages() {
            return totalPages;
        }

        public int getNumber() {
            return number;
        }
    }
}

