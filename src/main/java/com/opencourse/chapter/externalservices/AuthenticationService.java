package com.opencourse.chapter.externalservices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.chapter.prop.ExternalServiceProp;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AuthenticationService {
    
    private final ExternalServiceProp prop;
    private RestTemplate restTemplate;

    public AuthenticationService(ExternalServiceProp prop){
        this.prop=prop;
        restTemplate=new RestTemplate();
    }

    public Boolean isValid(String token){
        log.info("token is " + token);
        ResponseEntity<Boolean> response=restTemplate.postForEntity(prop.getAuthUrl() + "?token=" + token, null, Boolean.class);
        return response.getBody();
    }

}
