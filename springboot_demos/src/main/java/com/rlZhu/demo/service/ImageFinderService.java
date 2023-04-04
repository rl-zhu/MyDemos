package com.rlZhu.demo.service;


import com.rlZhu.demo.entity.Photo;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;

public interface ImageFinderService {

    // get all image url from the input url
    Set<String> getPageAllImagesLinks(String url) throws IOException;

    // set data types, headers
    List<ResponseEntity<byte[]>> setPageAllImagesData();

    // convert image url into byte[]
    Set<String> savePageImagesData(String url) throws IOException;

    // convert image url into byte[] save into repo
    Set<String> saveAllPageImagesData(String url) throws Exception;

    // save photo data into repo
    void getPhotoData(String imageUrl) throws IOException;

    Photo saveImage(String fileName, String contentType, byte[] data);

    // find all links from the page and its subpages
    Set<String> findLinksRecursive(String baseUrl) throws Exception;

    Set<String> findLinksRecursiveHelper(String baseUrl, String url, Set<String> links)
            throws Exception;


}
