package com.rlZhu.demo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ImageFinderRepoMulti {

    private Map<Integer, ResponseEntity<byte[]>> mapDb = new HashMap<>();

    public ResponseEntity<byte[]> save(ResponseEntity<byte[]> r) {

        Integer id = mapDb.size() + 1;
        mapDb.put(id, r);
        return r;
    }

    public List<ResponseEntity<byte[]>> getAll() {
        Iterable<ResponseEntity<byte[]>> photos = mapDb.values();
        List<ResponseEntity<byte[]>> photoList = StreamSupport.stream(photos.spliterator(), false)
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
