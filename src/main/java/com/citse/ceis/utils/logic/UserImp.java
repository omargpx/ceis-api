package com.citse.ceis.utils.logic;

import com.citse.ceis.entity.User;
import com.citse.ceis.exceptions.GUSException;
import com.citse.ceis.repository.UserDao;
import com.citse.ceis.utils.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserImp implements UserService {

    @Autowired
    private UserDao repo;

    @Override
    public List<User> getAll() {
        List<User> people = repo.findAll();
        if (!people.isEmpty())
            return people;
        return new ArrayList<>();
    }

    @Override
    public User getById(int id) {
        User person = repo.findById(id).orElse(null);
        if(null!=person)
            return person;
        throw new GUSException("USER_SERVICE", null, HttpStatus.NOT_FOUND);
    }

    @Override
    public User save(User user) {
        if(user.getDni()==null)
            throw new GUSException("USER_SERVICE", "dni required", HttpStatus.NOT_FOUND);
        return repo.save(user);
    }

    @Override
    public User save(String dni) {
        if(null!=repo.findByDni(dni))
            throw new GUSException("USER_SERVICE", "dni already exist", HttpStatus.NOT_FOUND);
        LocalDateTime today = LocalDateTime.now();
        User user = new User();
        user.setDni(dni);
        user.setCode(randNumber());
        user.setDate(today);
        return repo.save(user);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public User findByDni(String dni) {
        User person = repo.findByDni(dni);
        if(null!=person)
            return person;
        throw new GUSException("USER_SERVICE", "dni not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> findByName(String name) {
        List<User> users = repo.findByNameContains(name);
        if(!users.isEmpty())
            return users;
        throw new GUSException("USER_SERVICE", "user not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> findByNickname(String nickname) {
        List<User> users = repo.findByNicknameContains(nickname);
        if(!users.isEmpty())
            return users;
        throw new GUSException("USER_SERVICE", "user with nickname @"+nickname+" not found", HttpStatus.NOT_FOUND);
    }

    private String randNumber() {
        Random random = new Random();
        return 24+""+random.nextInt(99999);
    }
}
