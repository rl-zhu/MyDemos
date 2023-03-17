package com.rlZhu.demo.repository;

import com.rlZhu.demo.module.Photo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class Photorepo {
    private Map<Integer, Photo> mapDb = new HashMap<>();

    public Photo save(String fileName, String contentType, byte[] data) {
        Photo photo = new Photo();
        photo.setContentType(contentType);
        Integer id = mapDb.size() + 1;
        photo.setId(id);
        photo.setPhotoName(fileName);
        photo.setData(data);
        mapDb.put(id, photo);
        return photo;
    }

    public List<Photo> getAll() {
        Iterable<Photo> photos = mapDb.values();
        List<Photo> photoList = StreamSupport.stream(photos.spliterator(), false)
                .collect(Collectors.toList());
        return photoList;
    }

    public Integer getDBSize() {
        return mapDb.size();
    }

    public Boolean clearDB() {
        mapDb = new HashMap<>();
        return mapDb.size() == 0;
    }

}
