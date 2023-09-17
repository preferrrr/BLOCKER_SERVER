package com.blocker.blocker_server.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchUserResponse {
    private String email;
    private String name;
    private String picture;
}
