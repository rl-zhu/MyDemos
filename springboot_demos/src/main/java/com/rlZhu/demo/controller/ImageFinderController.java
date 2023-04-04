package com.rlZhu.demo.controller;


import com.rlZhu.demo.controller.utils.R_ImageFinder;
import com.rlZhu.demo.dao.ImageFinderRepo;
import com.rlZhu.demo.entity.ReqUrl;
import com.rlZhu.demo.service.ImageFinderService;
import java.util.List;
import java.util.Set;
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

@RequestMapping("/project")
@RestController
public class ImageFinderController {

    @Autowired
    private ImageFinderService photoService;

    @Autowired
    private ImageFinderRepo photorepo;

    Logger logger = LoggerFactory.getLogger(ImageFinderController.class);

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/images")
    public ResponseEntity getImages(@RequestBody ReqUrl reqUrl) throws Exception {
        Set<String> imageUrls = photoService.savePageImagesData(reqUrl.getRequrl());
        long start = System.currentTimeMillis();

        for (String urlSingle : imageUrls) {
            photoService.getPhotoData(urlSingle);
        }
        long end = System.currentTimeMillis();
        logger.info("##### Get all photos in total time {}", (end - start));
        List<ResponseEntity<byte[]>> pageAllImagesData = photoService.setPageAllImagesData();

        R_ImageFinder r = new R_ImageFinder(photorepo.getDBSize() != 0, pageAllImagesData,
                end - start, photorepo.getDBSize());
        photorepo.clearDB();
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/cleardb")
    public Boolean getClear() {
        photorepo.clearDB();
        return photorepo.getDBSize() == 0;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/images/all")
    public ResponseEntity getAllImages(@RequestBody ReqUrl reqUrl) throws Exception {
        Set<String> imageUrls = photoService.saveAllPageImagesData(reqUrl.getRequrl());
        long start = System.currentTimeMillis();
        for (String urlSingle : imageUrls) {
            photoService.getPhotoData(urlSingle);
        }
        long end = System.currentTimeMillis();
        List<ResponseEntity<byte[]>> pageAllImagesData = photoService.setPageAllImagesData();
        logger.info("##### Get all photos in total time {}", (end - start));
        R_ImageFinder r = new R_ImageFinder(photorepo.getDBSize() != 0, pageAllImagesData,
                end - start, photorepo.getDBSize());
        photorepo.clearDB();

        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

}
