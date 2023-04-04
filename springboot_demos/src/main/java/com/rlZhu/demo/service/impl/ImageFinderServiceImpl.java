package com.rlZhu.demo.service.impl;

import com.rlZhu.demo.dao.ImageFinderRepo;
import com.rlZhu.demo.entity.Photo;
import com.rlZhu.demo.service.ImageFinderService;
import io.micrometer.common.util.StringUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ImageFinderServiceImpl implements ImageFinderService {

    @Autowired
    private ImageFinderRepo photorepo;


    // get all image url from the input url
    @Override
    public Set<String> getPageAllImagesLinks(String url) throws IOException {
        Set<String> imageUrls = new HashSet<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]"); // Find all image tags
            for (Element image : images) {

                String imageUrl = image.absUrl("src");
                if (!StringUtils.isEmpty(imageUrl)) {
                    imageUrls.add(imageUrl);
                }
            }
        } catch (UnsupportedMimeTypeException e) {
            imageUrls.add(url);
        } catch (HttpStatusException e) {
            System.out.println("HttpStatusException ");
            System.out.println(e);
        }
        return imageUrls;
    }


    // set data types, headers
    @Override
    public List<ResponseEntity<byte[]>> setPageAllImagesData() {
        List<Photo> photos = photorepo.getAll();
        List<ResponseEntity<byte[]>> responses = new ArrayList<>();
        for (Photo photo : photos) {
            byte[] data = photo.getData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(photo.getContentType()));
            ContentDisposition build = ContentDisposition.builder("attachment")
                    .filename((photo.getPhotoName())).build();
            headers.setContentDisposition(build);
            ResponseEntity<byte[]> response = new ResponseEntity<>(data, headers, HttpStatus.OK);
            responses.add(response);
        }
        return responses;
    }


    // convert image url into byte[]
    @Override
    public Set<String> savePageImagesData(String url) throws IOException {
        Set<String> imageUrls = getPageAllImagesLinks(url);
        return imageUrls;
    }

    // convert image url into byte[] save into repo
    @Override
    public Set<String> saveAllPageImagesData(String url) throws Exception {
        Set<String> imageUrls = new HashSet<>();
        Set<String> subPages = findLinksRecursive(url);
        subPages.add(url);
        for (String pageLink : subPages) {
            imageUrls.addAll(getPageAllImagesLinks(pageLink));
        }
        return imageUrls;
    }

    // save photo data into repo
    @Override
    public void getPhotoData(String imageUrl) throws IOException {
        try {
            Connection.Response response = Jsoup.connect(imageUrl).ignoreContentType(true)
                    .execute();
            byte[] imageBytes = response.bodyAsBytes();
            String contentType = response.contentType();
            String fileName = "image.jpg";
            saveImage(fileName, contentType, imageBytes);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("getPhotoData in ImageFinderServiceImpl");
            throw e;
        }

    }


    @Override
    public Photo saveImage(String fileName, String contentType, byte[] data) {
        return photorepo.save(fileName, contentType, data);
    }


    // find all links from the page and its subpages
    @Override
    public Set<String> findLinksRecursive(String baseUrl) throws Exception {
        Set<String> links = new HashSet<>();
        return findLinksRecursiveHelper(baseUrl, baseUrl, links);
    }

    @Override
    public Set<String> findLinksRecursiveHelper(String baseUrl, String url, Set<String> links)
            throws Exception {
        URLConnection connection = new URL(url).openConnection();
        connection.connect();
        try {
            Document doc = Jsoup.parse(connection.getInputStream(), null, url);
            Elements elements = doc.select("a[href], link[href]");
            for (Element element : elements) {
                String href = element.attr("href");
                if (href.startsWith(url) && url != href && !links.contains(href)) {
                    links.add(href);
                    links.addAll(findLinksRecursiveHelper(baseUrl, href, links));
                } else if (href.startsWith("//") && !href.equals("/")) {
                    href = baseUrl + href.substring(2);
                    if (links.add(href)) {
                        links.addAll(findLinksRecursiveHelper(baseUrl, href, links));
                    }
                } else if (baseUrl.startsWith("/") && !href.equals("/") && !links.contains(href)) {
                    href = url + href.substring(1);
                    if (links.add(href)) {
                        links.addAll(findLinksRecursiveHelper(baseUrl, href, links));
                    }
                }
            }
            return links;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return new HashSet<>();
    }


}
