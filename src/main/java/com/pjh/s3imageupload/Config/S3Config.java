package com.pjh.s3imageupload.Config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3Client(){
        // accessKey, secretKey를 Credentials에 저장

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        /*  해당 BasicAWSCredenitals의 AllArgumentConstructor :
            public BasicAWSCredentials(String accessKey, String secretKey) {
            if (accessKey == null) {
                throw new IllegalArgumentException("Access key cannot be null.");
            }
            if (secretKey == null) {
                throw new IllegalArgumentException("Secret key cannot be null.");
            }

            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }*/

        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
