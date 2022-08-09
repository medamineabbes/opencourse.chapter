package com.opencourse.chapter.repos;

import com.opencourse.chapter.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ChapterRepo extends JpaRepository<Chapter,Long>{
    
}
