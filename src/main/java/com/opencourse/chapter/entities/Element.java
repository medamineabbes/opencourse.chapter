package com.opencourse.chapter.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Element {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="title is mandatory")
    @Size(max=50,message="max number of characters is 50")
    private String title;
    @NotBlank
    private String markdownContent;
    
    @ManyToOne
    @NotNull
    private Chapter chapter;
}
