package com.rlZhu.demo.controller.utils;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class R_ImageFinder {

    private Boolean flag;

    private List<ResponseEntity<byte[]>> data;

    private String memo;

    private long totalTime;

    private Integer totalNum;

    public R_ImageFinder(Boolean flag, List<ResponseEntity<byte[]>> data, long totalTime,
            Integer totalNum) {
        this.flag = flag;
        this.data = data;
        this.totalTime = totalTime;
        this.totalNum = totalNum;
    }
}
