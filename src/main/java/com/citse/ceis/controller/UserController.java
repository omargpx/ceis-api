package com.citse.ceis.controller;

import com.citse.ceis.entity.User;
import com.citse.ceis.exceptions.GUSException;
import com.citse.ceis.utils.contracts.GUSMethods;
import com.citse.ceis.utils.contracts.RecordService;
import com.citse.ceis.utils.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private RecordService recordService;
    @Autowired
    private GUSMethods gus;
    private static final String service_name = "USER_SERVICE";
    @Autowired
    private Environment env;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam(name = "dni")String dni,
                                 @RequestParam(name = "token")String TOKEN,
                                 HttpServletRequest request){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(gus.getResponse(request,service_name,service.save(dni),HttpStatus.OK));
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@RequestParam(name = "dni")String dni,
                                        @RequestBody User user, HttpServletRequest request,
                                        @RequestParam(name = "token")String TOKEN){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        User credentials = service.findByDni(dni);
        credentials.setAvatar(user.getAvatar()!=null? user.getAvatar() : credentials.getAvatar());
        credentials.setName(user.getName()!=null? user.getName() : credentials.getName());
        credentials.setNickname(user.getNickname()!=null? user.getNickname() : credentials.getNickname());
        credentials.setEntity(user.getEntity()!=null? user.getEntity() : credentials.getEntity());
        credentials.setGender(user.getGender()!=null? user.getGender() : credentials.getGender());
        return ResponseEntity.ok(gus.getResponse(request,service_name,service.save(credentials),HttpStatus.OK));
    }

    @GetMapping("/filters")
    public ResponseEntity<?> getAll(@RequestParam(name = "dni",required = false)String dni,
                                    @RequestParam(name = "name",required = false)String name,
                                    @RequestParam(name = "nickname",required = false)String nickname,
                                    HttpServletRequest request, @RequestParam(name = "token")String TOKEN){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        if(null!=dni)
            return ResponseEntity.ok(gus.getResponse(request,service_name,service.findByDni(dni),HttpStatus.OK));
        if(null!=name)
            return ResponseEntity.ok(gus.getResponse(request,service_name,service.findByName(name),HttpStatus.OK));
        if(null!=nickname)
            return ResponseEntity.ok(gus.getResponse(request,service_name,service.findByNickname(nickname),HttpStatus.OK));
        return ResponseEntity.ok(gus.getResponse(request,service_name,service.getAll(),HttpStatus.OK));
    }

    @GetMapping("/{dni}/get-records")
    public ResponseEntity<?> getRecordsByUser(@RequestParam(name = "day",required = false)Integer day,
                                              @PathVariable Long dni, HttpServletRequest request){
        if(null!=day)
            return ResponseEntity.ok(gus.getResponse(request,service_name,recordService.getRecordsByUserAndDay(String.valueOf(dni),day),HttpStatus.OK));
        return ResponseEntity.ok(gus.getResponse(request,service_name,recordService.getRecordsByUser(String.valueOf(dni)),HttpStatus.OK));
    }
}
