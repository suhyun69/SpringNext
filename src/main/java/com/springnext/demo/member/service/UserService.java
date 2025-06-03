package com.springnext.demo.member.service;

import com.springnext.demo.member.controller.request.UserRequest;
import com.springnext.demo.member.controller.response.UserResponse;

import java.util.List;

public interface UserService {
    public UserResponse registerUser(UserRequest request);
    public List<UserResponse> getAllUsers();
    public UserResponse getUserById(Long id);
}
