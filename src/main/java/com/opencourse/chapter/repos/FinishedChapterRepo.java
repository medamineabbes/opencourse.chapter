package com.opencourse.chapter.repos;

import com.opencourse.chapter.entities.FinishedChapter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FinishedChapterRepo extends JpaRepository<FinishedChapter,Long>{
    public Optional<FinishedChapter> findByUserIdAndChapterId(Long userId,Long chapterId);
}
