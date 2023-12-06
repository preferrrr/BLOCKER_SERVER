package com.blocker.blocker_server.user.repository;

import com.blocker.blocker_server.user.domain.User;

import java.util.List;

public interface UserRepositoryCustom {
    public List<User> searchUsers(String keyword);
}
