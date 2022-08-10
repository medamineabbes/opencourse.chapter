package com.opencourse.chapter.repos;

import java.util.List;
import com.opencourse.chapter.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ChapterRepo extends JpaRepository<Chapter,Long>{
    List<Chapter> findBySectionId(Long sectionId);
}
