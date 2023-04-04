package com.rlZhu.demo.controller;


import com.rlZhu.demo.controller.utils.R_ImageFinder;
import com.rlZhu.demo.dao.ImageFinderRepoMulti;
import com.rlZhu.demo.entity.ReqUrl;
import com.rlZhu.demo.service.ImageFinderServiceMulti;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/project/multi")
@RestController
public class ImageFinderController_multi {

    @Autowired
    private ImageFinderServiceMulti photoService;

    @Autowired
    private ImageFinderRepoMulti photorepo;

    Logger logger = LoggerFactory.getLogger(ImageFinderController_multi.class);

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/images")
    public ResponseEntity getImages(@RequestBody ReqUrl reqUrl) throws Exception {
        Set<String> imageUrls = photoService.savePageImagesData(reqUrl.getRequrl());
        long start = System.currentTimeMillis();

        List<CompletableFuture<ResponseEntity<byte[]>>> futures = new ArrayList<>();
        for (String urlSingle : imageUrls) {
            futures.add(photoService.getPhotoData(urlSingle));
        }

        AtomicLong total = new AtomicLong();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenRun(() -> {
                    long end = System.currentTimeMillis();
                    logger.info("##### Get all photos in total time {}", (end - start));
                    total.set(end - start);
                }).join();
        R_ImageFinder r = new R_ImageFinder(photorepo.getDBSize() != 0,
                photoService.getPageAllImagesData(), total.longValue(), photorepo.getDBSize());
        photorepo.clearDB();
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/cleardb")
    public Integer getClear() {
        photorepo.clearDB();
        return photorepo.getDBSize();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/images/all")
    public ResponseEntity getAllImages(@RequestBody ReqUrl reqUrl) throws Exception {

        Set<String> imageUrls = photoService.saveAllPageImagesData(reqUrl.getRequrl());
        long start = System.currentTimeMillis();
        List<CompletableFuture<ResponseEntity<byte[]>>> futures = new ArrayList<>();
        for (String urlSingle : imageUrls) {
            futures.add(photoService.getPhotoData(urlSingle));
        }
        AtomicLong total = new AtomicLong();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenRun(() -> {
                    long end = System.currentTimeMillis();
                    logger.info("##### Get all photos in total time {}", (end - start));
                    total.set(end - start);
                }).join();
        R_ImageFinder r = new R_ImageFinder(photorepo.getDBSize() != 0,
                photoService.getPageAllImagesData(), total.longValue(), photorepo.getDBSize());
        photorepo.clearDB();
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

}
