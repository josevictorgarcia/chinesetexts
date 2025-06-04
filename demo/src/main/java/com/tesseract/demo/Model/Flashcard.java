package com.tesseract.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Flashcard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne
    @JoinColumn(name = "text_id")
    private Text example;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    public Flashcard() {
    }

    public Flashcard(long id, Word word, Text example) {
        this.id = id;
        this.word = word;
        this.example = example;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Text getExample() {
        return example;
    }

    public void setExample(Text example) {
        this.example = example;
    }
}
