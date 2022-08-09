package com.opencourse.chapter.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.List;

import lombok.Data;

@Data
@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String videoUrl;
    private String description;
    private Long sectionId;

    @OneToMany(mappedBy="chapter")
    private List<Element> elements;

    @OneToMany(mappedBy="chapter")
    private List<FinishedChapter> finishedChapters;
}
