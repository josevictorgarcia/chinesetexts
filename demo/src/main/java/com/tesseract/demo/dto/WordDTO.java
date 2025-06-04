package com.tesseract.demo.dto;

public record WordDTO (
    Long id,
    String pinyin,
    String chinese,
    String english,
    String spanish){
}
