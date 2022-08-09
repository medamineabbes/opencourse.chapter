package com.opencourse.chapter.repos;
import org.springframework.data.jpa.repository.JpaRepository;
import com.opencourse.chapter.entities.Element;
public interface ElementRepo extends JpaRepository<Element,Long>{
    
}
