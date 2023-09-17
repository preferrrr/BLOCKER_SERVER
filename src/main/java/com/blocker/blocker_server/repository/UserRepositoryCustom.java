package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    public List<User> searchUsers(String keyword);
}
