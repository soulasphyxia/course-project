package org.soulasphyxia.webcourseproject.config;

import com.amazonaws.auth.AWSCredentials;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@Data
@RequiredArgsConstructor
public class AwsS3Config {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.endpoint}")
    private String endpoint;

    @Value("${aws.region}")
    private String region;

    private AWSCredentials getCredentials(){
        return new BasicAWSCredentials(accessKey,
                secretKey);
    }

    private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration(){
        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }

    @Bean
    public AmazonS3 amazonClient() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(getEndpointConfiguration())
                .build();
    }
}

