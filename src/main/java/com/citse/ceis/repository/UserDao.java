package com.citse.ceis.repository;

import com.citse.ceis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User,Integer> {
    User findByDni(String dni);
    List<User> findByNameContains(String name);
    List<User> findByNicknameContains(String name);

}
