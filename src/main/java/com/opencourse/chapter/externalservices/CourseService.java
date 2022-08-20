package com.opencourse.chapter.externalservices;

import org.springframework.stereotype.Service;

@Service
public class CourseService{

    public boolean userHasAccessToSection(Long sectionId,Long userId){
        //call course service rest template
        return false;
    }
    public boolean userCreatedSection(Long sectionId,Long userId){
        //call course service rest template
        return false;
    }
}