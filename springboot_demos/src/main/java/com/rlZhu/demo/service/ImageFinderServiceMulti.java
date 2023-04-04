package com.rlZhu.demo.service;


import com.rlZhu.demo.dao.ImageFinderRepoMulti;
import io.micrometer.common.util.StringUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class ImageFinderServiceMulti {

    @Autowired
    private ImageFinderRepoMulti photorepo;


    // convert image url into byte[]
    //    @Async
    public Set<String> savePageImagesData(String url) throws IOException {
        Set<String> imageUrls = getPageAllImagesLinks(url);
        return imageUrls;
    }

    // search subpages convert image url into byte[] save into repo
    //    @Async
    public Set<String> saveAllPageImagesData(String url) throws Exception {
        Set<String> imageUrls = new HashSet<>();
        Set<String> subPages = findLinksRecursive(url);
        subPages.add(url);
        for (String pageLink : subPages) {
            imageUrls.addAll(getPageAllImagesLinks(pageLink));
        }

        return imageUrls;
    }


    // get all image url from the input url
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


    // get image data from urls and save photo data into repo
    @Async
    public CompletableFuture<ResponseEntity<byte[]>> getPhotoData(String imageUrl)
            throws IOException {
        Connection.Response response = Jsoup.connect(imageUrl).ignoreContentType(true).execute();
        byte[] imageBytes = response.bodyAsBytes();
        String contentType = response.contentType();
        String fileName = "image" + photorepo.getDBSize() + ".jpg";

        byte[] data = imageBytes;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(contentType));
        ContentDisposition build = ContentDisposition.builder("attachment").filename((fileName))
                .build();
        headers.setContentDisposition(build);
        ResponseEntity<byte[]> entityNew = new ResponseEntity<>(data, headers, HttpStatus.OK);
        entityNew = photorepo.save(entityNew);
        return CompletableFuture.completedFuture(entityNew);
    }


    // get all urls  from subpages
    public List<ResponseEntity<byte[]>> getPageAllImagesData() {
        List<ResponseEntity<byte[]>> photos = photorepo.getAll();
        return photos;
    }


    // find all links from the page and its subpages
    public Set<String> findLinksRecursive(String baseUrl) throws Exception {
        Set<String> links = new HashSet<>();
        return findLinksRecursiveHelper(baseUrl, baseUrl, links);
    }

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




