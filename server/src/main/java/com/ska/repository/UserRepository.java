package com.ska.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ska.model.user.User;
import com.ska.model.user.vo.Email;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(Email email);

}
