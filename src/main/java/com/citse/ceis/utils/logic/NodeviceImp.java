package com.citse.ceis.utils.logic;

import com.citse.ceis.entity.Event;
import com.citse.ceis.entity.Nodevice;
import com.citse.ceis.entity.User;
import com.citse.ceis.exceptions.GUSException;
import com.citse.ceis.repository.NodeviceDao;
import com.citse.ceis.utils.contracts.EventService;
import com.citse.ceis.utils.contracts.GUSMethods;
import com.citse.ceis.utils.contracts.NodeviceService;
import com.citse.ceis.utils.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class NodeviceImp implements NodeviceService {

    @Autowired
    private NodeviceDao repo;
    @Autowired
    private GUSMethods gus;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @Override
    public Nodevice save(String dni, String eventCode) {
        var event = eventService.findByCode(eventCode);
        var user = userService.findByDni(dni);
        var verify = repo.findByUserNAndEventN(user,event);
        if(verify.isPresent())
            return verify.get();
        Nodevice nodevice = new Nodevice();
        nodevice.setCode(gus.genSecureCode("NC3I5"));
        nodevice.setEventN(event);
        nodevice.setUserN(user);
        nodevice.setIsUsed(false);
        return repo.save(nodevice);
    }

    private void verifyGenCode(User user, Event event){
        if(repo.existsByUserAndEvent(user,event))
            throw new GUSException("RECORD_SERVICE", "Record already exists", HttpStatus.BAD_REQUEST);
    }
}
