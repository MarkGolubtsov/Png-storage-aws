package com.mark.task.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {
    @Bean
    public AWSCredentials awsCredentials(AWSConfigurationProperties amazonProperties) {
        return new BasicAWSCredentials(
                amazonProperties.getAccessKey(),
                amazonProperties.getSecretKey()
        );
    }

    @Bean
    public AmazonS3 s3Client(AWSConfigurationProperties amazonProperties, AWSCredentials credentials) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(amazonProperties.getRegionName()))
                .build();
    }
}
