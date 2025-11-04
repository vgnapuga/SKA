package com.ska.service.contract;


import com.ska.dto.user.request.UserCreateRequest;
import com.ska.model.user.User;


public interface UserService {

    public User create(UserCreateRequest request);

    public User getById(Long id);

    public void delete(Long id);

    public User checkUserExistenceAndGet(Long userId);

}
