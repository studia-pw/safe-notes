package com.odas.safenotes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Note {

    @Id
    private Long id;

    private String title;
    private String content;

    private String hashedPassword;

    private Boolean isPublic;

    @ManyToOne
    private User owner;
}
