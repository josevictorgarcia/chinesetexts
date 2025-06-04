package com.tesseract.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tesseract.demo.Model.Collection;
import com.tesseract.demo.Repository.CollectionRepository;

public class CollectionService {
    
    @Autowired
    private CollectionRepository collectionRepository;

    public Collection save(Collection collection){
        return collectionRepository.save(collection);
    }

    public List<Collection> getAllCollections(){
        return collectionRepository.findAllByOrderByDateDesc();
    }
}
