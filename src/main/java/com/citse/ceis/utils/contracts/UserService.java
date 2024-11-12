package com.citse.ceis.utils.contracts;

import com.citse.ceis.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(int id);
    User save(User user);
    User save(String dni);
    void deleteById(int id);

    //filters
    User findByDni(String dni);
    List<User> findByName(String name);
    List<User> findByNickname(String nickname);
}
