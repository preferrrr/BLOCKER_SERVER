package com.blocker.blocker_server.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchUserResponse {
    private String email;
    private String name;
    private String picture;

    @Builder
    private SearchUserResponse(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public static SearchUserResponse of(String email, String name, String picture) {
        return SearchUserResponse.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .build();
    }
}
