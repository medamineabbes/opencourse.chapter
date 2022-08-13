package com.opencourse.chapter.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="title is mandatory")
    @Size(max=50,message="max number of characters is 50")
    private String title;

    @NotBlank(message="video is mandatory")
    private String videoUrl;

    @NotBlank(message="description is mandatory")
    @Size(max=300,message="max number of characters is 300")
    private String description;
    
    @NotNull
    private Long sectionId;

    @OneToMany(mappedBy="chapter",cascade=CascadeType.REMOVE)
    private List<Element> elements;

    @OneToMany(mappedBy="chapter")
    private List<FinishedChapter> finishedChapters;
}
