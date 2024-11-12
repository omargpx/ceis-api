package com.citse.ceis.controller;

import com.citse.ceis.entity.Event;
import com.citse.ceis.exceptions.GUSException;
import com.citse.ceis.models.EventType;
import com.citse.ceis.utils.contracts.EventService;
import com.citse.ceis.utils.contracts.GUSMethods;
import com.citse.ceis.utils.contracts.NodeviceService;
import com.citse.ceis.utils.contracts.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService service;
    @Autowired
    private RecordService recordService;
    @Autowired
    private NodeviceService nodeviceService;
    @Autowired
    private GUSMethods gus;
    private static final String service_name = "EVENT_SERVICE";
    @Autowired
    private Environment env;

    @GetMapping("/filters")
    public ResponseEntity<?> getAll(@RequestParam(name = "id",required = false) Integer id,
                                    @RequestParam(name = "name",required = false)String name,
                                    @RequestParam(name = "code",required = false)String code,
                                    @RequestParam(name = "day",required = false) Integer day,
                                    @RequestParam(name = "type", required = false) EventType type,
                                    @RequestParam(name = "token")String TOKEN, HttpServletRequest request){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        if(null!=id)
            return ResponseEntity.ok(gus.getResponse(request,service_name,service.getById(id), HttpStatus.OK));
        if(null!=name)
            return ResponseEntity.ok(gus.getResponse(request,service_name,service.findByName(name), HttpStatus.OK));
        if(null!=code)
            return ResponseEntity.ok(gus.getResponse(request,service_name,service.findByCode(code), HttpStatus.OK));
        if(null!=type)
            return ResponseEntity.ok(gus.getResponse(request,service_name,service.findByType(type,day), HttpStatus.OK));
        if(null!=day)
            return ResponseEntity.ok(gus.getResponse(request,service_name,service.getEventsByDay(day), HttpStatus.OK));
        return ResponseEntity.ok(gus.getResponse(request,service_name,service.getAllBySort(), HttpStatus.OK));
    }

    @PostMapping("/add")
    public ResponseEntity<?> save(@RequestBody Event event, HttpServletRequest request,
                                  @RequestParam(name = "token")String TOKEN){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(gus.getResponse(request,service_name,service.save(event), HttpStatus.OK));
    }

    @PostMapping("/booking")
    public ResponseEntity<?> reserveBooking(@RequestParam(name = "token")String TOKEN,
                                            @RequestParam(name = "dni", required = false)Long dni, HttpServletRequest request,
                                            @RequestParam(name = "code", required = false)String eventCode){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(gus.getResponse(request,service_name,recordService.save(String.valueOf(dni),eventCode), HttpStatus.OK));
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> reserveBookings(@RequestParam(name = "token")String TOKEN,
                                             @RequestParam(name = "dni", required = false)Long dni, HttpServletRequest request,
                                             @RequestBody List<Event> events){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(gus.getResponse(request,service_name,recordService.saveAll(String.valueOf(dni),events),HttpStatus.OK));
    }

    @PostMapping("/stamp")
    public ResponseEntity<?> saveAssistance(@RequestParam(name = "token")String TOKEN,
                                            @RequestParam(name = "dni", required = false)Long dni, HttpServletRequest request,
                                            @RequestParam(name = "code", required = false)String eventCode,
                                            @RequestParam(name = "codeAssistance", required = false)String genCode){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        if(null!=genCode)
            return ResponseEntity.ok(gus.getResponse(request,service_name,recordService.save(genCode), HttpStatus.OK));
        return ResponseEntity.ok(gus.getResponse(request,service_name,recordService.confirmStamp(String.valueOf(dni),eventCode), HttpStatus.OK));
    }

    @GetMapping("/gen-code-assistance")
    public ResponseEntity<?> genCodeAssistance(@RequestParam(name = "token")String TOKEN,
                                               @RequestParam(name = "dni")Long dni, HttpServletRequest request,
                                               @RequestParam(name = "code")String eventCode){
        if(!Objects.equals(TOKEN, env.getProperty("config.ceis-access.security-token-permission")))
            throw new GUSException(service_name, null, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(gus.getResponse(request,service_name,nodeviceService.save(String.valueOf(dni),eventCode), HttpStatus.OK));
    }
}
