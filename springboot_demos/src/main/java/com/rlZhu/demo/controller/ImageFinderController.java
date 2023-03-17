package com.rlZhu.demo.controller;


import com.rlZhu.demo.module.ReqUrl;
import com.rlZhu.demo.repository.Photorepo;
import com.rlZhu.demo.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ImageFinderController {
    @Autowired
    private PhotoService photoService;

    @Autowired
    private Photorepo photorepo;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/images")
    public List<ResponseEntity<byte[]>> getImages(@RequestBody ReqUrl reqUrl) throws Exception {
        photoService.savePageImagesData(reqUrl.getRequrl());
        System.out.println(photorepo.getDBSize());
        List<ResponseEntity<byte[]>> pageAllImagesData = photoService.getPageAllImagesData();
        photorepo.clearDB();
        System.out.println(photorepo.getDBSize());
        return pageAllImagesData;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/cleardb")
    public Integer getClear() {
        photorepo.clearDB();
        return photorepo.getDBSize();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/images/all")
    public List<ResponseEntity<byte[]>> getAllImages(@RequestBody ReqUrl reqUrl) throws Exception {
        photoService.saveAllPageImagesData(reqUrl.getRequrl());
        System.out.println(photorepo.getDBSize());
        List<ResponseEntity<byte[]>> pageAllImagesData = photoService.getPageAllImagesData();
        photorepo.clearDB();
        System.out.println(photorepo.getDBSize());
        return pageAllImagesData;
    }

}
