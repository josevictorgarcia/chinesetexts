package com.tesseract.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.Collection;
import com.tesseract.demo.Repository.CollectionRepository;
import com.tesseract.demo.dto.CollectionDTO;
import com.tesseract.demo.dto.CollectionMapper;

@Service
public class CollectionService {
    
    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionMapper collectionMapper;

    public Collection save(Collection collection){
        return collectionRepository.save(collection);
    }

    public List<CollectionDTO> getAllCollections(){
        return toDTOs(collectionRepository.findAllByOrderByDateDesc());
    }

    private List<CollectionDTO> toDTOs(List<Collection> collections){
        return collectionMapper.toDTO(collections);
    }
}
