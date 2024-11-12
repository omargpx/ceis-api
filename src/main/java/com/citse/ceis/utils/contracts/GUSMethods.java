package com.citse.ceis.utils.contracts;

import com.citse.ceis.models.GUSResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.util.Map;

public interface GUSMethods {
    String genSecureCode(String acronym);
    String genCodeWorkshop();
    String randNumber();
    String getCurrentYear();
    Map<String, String> getSpecificHeaders(HttpServletRequest request);
    Object OAuthAccountLoginCredential(String dni);
    GUSResponse getResponse(HttpServletRequest url, String className, Object data, HttpStatus status);
}
