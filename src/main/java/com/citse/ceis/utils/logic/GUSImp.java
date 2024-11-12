package com.citse.ceis.utils.logic;

import com.citse.ceis.entity.User;
import com.citse.ceis.exceptions.GUSException;
import com.citse.ceis.models.GUSResponse;
import com.citse.ceis.repository.UserDao;
import com.citse.ceis.utils.contracts.GUSMethods;
import com.citse.ceis.utils.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class GUSImp implements GUSMethods {

    @Autowired
    private UserService userService;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ012345678987654321abcdefghijklmnñopqrstuvxyz";
    private static final SecureRandom sr = new SecureRandom();
    private final Random random  = SecureRandom.getInstanceStrong();

    public GUSImp() throws NoSuchAlgorithmException {
    }


    @Override
    public String genSecureCode(String acronym) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int index = sr.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(index));
        }
        return acronym+getCurrentYear()+builder.toString();
    }

    @Override
    public String genCodeWorkshop() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int index = sr.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(index));
        }
        return "C3I5"+builder.toString();
    }

    @Override
    public String randNumber() {
        return getCurrentYear()+"-"+random.nextInt(99999);
    }

    @Override
    public String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return Integer.toString(year).substring(2);
    }

    @Override
    public Map<String, String> getSpecificHeaders(HttpServletRequest request) {
        Map<String, String> specificHeaders = new HashMap<>();
        specificHeaders.put("connection", request.getHeader("connection"));
        specificHeaders.put("sec-ch-ua", request.getHeader("sec-ch-ua"));
        specificHeaders.put("sec-ch-ua-platform", request.getHeader("sec-ch-ua-platform"));
        specificHeaders.put("hook_info","/info");
        return specificHeaders;
    }

    @Override
    public Object OAuthAccountLoginCredential(String dni) {
        LocalDateTime today = LocalDateTime.now();
        User user_credential = userService.findByDni(dni);
        if(user_credential==null)
            throw new GUSException("GUS", "dni does not exist ",HttpStatus.NOT_FOUND);
        user_credential.setDate(today);
        user_credential.setIsOpened(true);
        userService.save(user_credential);
        return user_credential;
    }

    @Override
    public GUSResponse getResponse(HttpServletRequest url, String className, Object data, HttpStatus status) {
        return new GUSResponse(url.getRequestURI(),className,data,status.name());
    }
}
