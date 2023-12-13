package com.pjh.s3imageupload.Controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class S3FileUploadController {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3FileUploadController(AmazonS3 amazonS3){
        this.amazonS3 = amazonS3;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file){
        try {
            String fileOriginalName = file.getOriginalFilename();
            // 파일을 중복해서 올리면 s3에 올라가지 않음, 그러므로 uuid를 추가해서
            // 해당 이미지 파일을 엄청 낮은 확률이 아닌이상 중복을 제거
            UUID uuid = UUID.randomUUID();
            String uuidFileName = uuid+"_"+fileOriginalName;

            String url = "http://"+bucket+"/test"+fileOriginalName;
            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(file.getContentType());
            metaData.setContentLength(file.getSize());
            amazonS3.putObject(bucket, uuidFileName, file.getInputStream(), metaData);
            return ResponseEntity.ok(url);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
