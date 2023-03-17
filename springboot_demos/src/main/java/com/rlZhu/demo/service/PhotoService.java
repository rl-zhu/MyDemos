package com.rlZhu.demo.service;

import com.rlZhu.demo.module.Photo;
import com.rlZhu.demo.repository.Photorepo;
import io.micrometer.common.util.StringUtils;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhotoService {
    private static final String URL = "https://images.pexels.com/photos/545063/pexels-photo-545063.jpeg?auto=compress&format=tiny"; // Replace with the URL of the web page to crawl
    private static final String SAVE_DIR = "images"; // Replace with the directory to save the images


    @Autowired
    private Photorepo photorepo;

    public static void main(String[] args) throws Exception {
        PhotoService sv = new PhotoService();
        String url = "https://union.ufl.edu/";
        Set<String> allLinks =  sv.findLinksRecursive(url);

        for (String link : allLinks) {
            System.out.println(link);
        }
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
        } catch (UnsupportedMimeTypeException e){
            imageUrls.add(url);
        } catch (HttpStatusException e )  {
            System.out.println("HttpStatusException ");
            System.out.println(e);
        }
        return imageUrls;
    }

    public List<ResponseEntity<byte[]>> getPageAllImagesData() {
        List<Photo> photos = photorepo.getAll();
        List<ResponseEntity<byte[]>> responses = new ArrayList<>();
        for (Photo photo : photos) {
            byte[] data = photo.getData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(photo.getContentType()));
            ContentDisposition build = ContentDisposition.builder("attachment")
                    .filename((photo.getPhotoName()))
                    .build();
            headers.setContentDisposition(build);
            ResponseEntity<byte[]> response = new ResponseEntity<>(data, headers, HttpStatus.OK);
            responses.add(response);
        }
        return responses;
    }


    // convert image url into byte[]
    public void savePageImagesData(String url) throws IOException {
        Set<String> imageUrls = getPageAllImagesLinks(url);

        for (String urlSingle : imageUrls) {
            getPhotoData(urlSingle);

        }
    }

    // convert image url into byte[] save into repo
    public void saveAllPageImagesData(String url) throws Exception {
        Set<String> imageUrls = new HashSet<>();
        imageUrls.add(url);
        Set<String> subPages = findLinksRecursive(url);
        for (String pageLink : subPages) {
            imageUrls.addAll(getPageAllImagesLinks(pageLink));
        }

        for (String urlSingle : imageUrls) {
            getPhotoData(urlSingle);
        }
    }


    // save photo data into repo
    private void getPhotoData(String imageUrl) throws IOException {
        Connection.Response response = Jsoup.connect(imageUrl).ignoreContentType(true).execute();
        byte[] imageBytes = response.bodyAsBytes();
        String contentType =  response.contentType();
        String fileName = "image.jpg";
        saveImage(fileName, contentType, imageBytes);
    }


    public Photo saveImage(String fileName, String contentType, byte[] data) {
        return  photorepo.save( fileName, contentType, data);
    }

    // return all links other than image links from a webpage (subpage not included)
    public static Set<String> findLinks(String url) throws Exception {
        Set<String> links = new HashSet<>();
        URLConnection connection = new URL(url).openConnection();
        connection.connect();
        try {
            Document doc = Jsoup.parse(connection.getInputStream(), null, url);
            Elements elements = doc.select("a[href], link[href]");
            for (Element element : elements) {
                String href = element.attr("href");
                if (href.startsWith(url) && url != href && !links.contains(href)) {
                    links.add(href);
                } else if (href.startsWith("//") && !href.equals("/") && !links.contains(href)) {
                    href = url+ href.substring(2);
                    links.add(href);
                } else if (href.startsWith("/") && !href.equals("/") && !links.contains(href)) {
                    href = url+ href.substring(1);
                    links.add(href);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return links;

    }

    // find all links from the page and its subpages
    public Set<String> findLinksRecursive(String baseUrl) throws Exception {
        Set<String> links = new HashSet<>();
       return findLinksRecursiveHelper(baseUrl,baseUrl,links);
    }

    public  Set<String> findLinksRecursiveHelper(String baseUrl, String url, Set<String> links) throws Exception {
        URLConnection connection = new URL(url).openConnection();
        connection.connect();
        try {
            Document doc = Jsoup.parse(connection.getInputStream(), null, url);
            Elements elements = doc.select("a[href], link[href]");
            for (Element element : elements) {
                String href = element.attr("href");
                if (href.startsWith(url) && url != href && !links.contains(href)) {
                    links.add(href);
                    links.addAll(findLinksRecursiveHelper(baseUrl,href, links));
                } else if (href.startsWith("//") && !href.equals("/") ) {
                    href = baseUrl+ href.substring(2);
                    if (links.add(href)) {
                        links.addAll(findLinksRecursiveHelper(baseUrl, href, links));
                    }
                } else if (baseUrl.startsWith("/") && !href.equals("/") && !links.contains(href)) {
                    href = url+ href.substring(1);
                    if (links.add(href)) {
                        links.addAll(findLinksRecursiveHelper(baseUrl, href, links));
                    }
                }
            }
            return  links;
        } catch (FileNotFoundException e ) {
        } catch ( IOException e) {
        }
        return new HashSet<>();
    }


    // get the image from the url and save to local
    private void crawlWebPageSingle() throws IOException {
        String imageUrl = "https://driversprep.com/wp-content/uploads/2017/02/Get-your-documents-ready.jpg";
        Connection.Response response = Jsoup.connect(imageUrl).ignoreContentType(true).execute();
        byte[] imageBytes = response.bodyAsBytes();
        String contentType =  response.contentType();
        String url = response.url().toString();
        String fileName = "image.jpg";
        if (contentType.startsWith("image/") && !url.equals(imageUrl)) {
            System.out.println(url);
        }
        System.out.println(response.headers());

        writeImage( fileName, imageBytes);
    }

    private static void writeImage(String fileName,byte[] imageBytes) throws IOException {
        try (InputStream in = new ByteArrayInputStream(imageBytes);
             OutputStream out = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
}
