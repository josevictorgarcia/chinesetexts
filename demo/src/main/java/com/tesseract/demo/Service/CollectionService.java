package com.tesseract.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.Collection;
import com.tesseract.demo.Model.User;
import com.tesseract.demo.Repository.CollectionRepository;
import com.tesseract.demo.dto.CollectionDTO;
import com.tesseract.demo.dto.CollectionMapper;

@Service
public class CollectionService {
    
    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private UserService userService;

    public Collection save(Collection collection){
        return collectionRepository.save(collection);
    }

    /*public CollectionDTO save(CollectionDTO collection){
        Collection newCollection = toDomain(collection);

    }*/

    public Collection getCollection(long id){
        Optional<Collection> collection = collectionRepository.findById(id);
        if(collection.isPresent()){
            return collection.get();
        } else {
            return null;
        }
    }

    public CollectionDTO toDTO(Collection collection){
        return collectionMapper.toDTO(collection);
    }

    public Collection toDomain(CollectionDTO collectionDTO){
        return collectionMapper.toDomain(collectionDTO);
    }

    public List<CollectionDTO> getAllCollections(){
        return toDTOs(collectionRepository.findAllByOrderByDateDesc());
    }

    public List<CollectionDTO> findByUserOrderByDateDesc(String email){
        User user = userService.findUserByEmail(email);
        return toDTOs(collectionRepository.findByUserOrderByDateDesc(user));
    }

    public void deleteCollection(long id){
        collectionRepository.deleteById(id);
    }

    private List<CollectionDTO> toDTOs(List<Collection> collections){
        return collectionMapper.toDTO(collections);
    }
}
