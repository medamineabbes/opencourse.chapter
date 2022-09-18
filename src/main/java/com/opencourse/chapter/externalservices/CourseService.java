package com.opencourse.chapter.externalservices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.Data;

@Service
public class CourseService{

    RestTemplate restTemplate;
    private String baseUrl="https://opencourse-course.herokuapp.com/api/v1";
    public CourseService(){
        restTemplate=new RestTemplate();
    }

    public boolean userHasAccessToSection(Long sectionId,Long userId){
        AccessDto dto=new AccessDto();
        dto.setSectionId(sectionId);
        dto.setUserId(userId);
        ResponseEntity<Boolean> response=restTemplate.postForEntity(baseUrl + "/subscription", dto, Boolean.class);
        return response.getBody();
    }
    
    public boolean userCreatedSection(Long sectionId,Long userId){
        AccessDto dto=new AccessDto();
        dto.setSectionId(sectionId);
        dto.setUserId(userId);
        ResponseEntity<Boolean> response=restTemplate.postForEntity(baseUrl + "/course/section/creator", dto, Boolean.class);
        return response.getBody();
    }

}

@Data
class AccessDto{
    private Long userId;
    private Long sectionId; 
}