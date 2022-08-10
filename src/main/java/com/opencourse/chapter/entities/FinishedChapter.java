package com.opencourse.chapter.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class FinishedChapter {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    
    @ManyToOne
    private Chapter chapter;
}
