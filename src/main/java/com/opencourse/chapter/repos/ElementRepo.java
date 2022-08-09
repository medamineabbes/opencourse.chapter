package com.opencourse.chapter.repos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opencourse.chapter.entities.Element;

@Repository
public interface ElementRepo extends JpaRepository<Element,Long>{
    
}
