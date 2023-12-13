package com.pjh.s3imageupload.Controller;

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

@RestController
@RequestMapping("/upload")
public class S3FileUploadController {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3FileUploadController(AmazonS3Client amazonS3Client){
        this.amazonS3Client = amazonS3Client;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file){
        try {
            String fileOriginalName = file.getOriginalFilename();
            // url = "http://pjhaws3uploadImage/test/myfile.png"
            String url = "http://"+bucket+"/test"+fileOriginalName;
            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(file.getContentType());
            metaData.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, fileOriginalName, file.getInputStream(), metaData);
            return ResponseEntity.ok(url);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
