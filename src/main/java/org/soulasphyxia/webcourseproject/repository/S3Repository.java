package org.soulasphyxia.webcourseproject.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.http.entity.ContentType;
import org.soulasphyxia.webcourseproject.user_actions.LogType;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository
public class S3Repository {

    private static final String BUCKET_NAME = "corpbucket";

    private AmazonS3 s3Client;

    public S3Repository(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }


    public String uploadFile(File file) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.length());
            objectMetadata.setContentType("video/mp4");
            PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, file.getName(), new FileInputStream(file), objectMetadata);
            s3Client.putObject(request);
            file.delete();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return String.format("Video %s uploaded successfully", file.getName());
    }

    public void uploadLog(InputStream bytes, String key, LogType logType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            objectMetadata.setContentType(ContentType.parse(logType.name()).toString());
            objectMetadata.setContentLength(bytes.available());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        s3Client.putObject(new PutObjectRequest(BUCKET_NAME, key, bytes, objectMetadata));
    }

    public String uploadImage(BufferedImage image, String key) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        byte[] buffer = os.toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpeg");
        meta.setContentLength(buffer.length);
        s3Client.putObject(new PutObjectRequest(BUCKET_NAME, key, is, meta));
        return String.format("Image %s uploaded successfully", key);
    }

    public List<String> getVideos() {
        return bucketObjects(BUCKET_NAME);
    }

    public String deleteVideos() {

        List<String> videos = getVideos();

        for (String video : videos) {
            s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, video));
        }

        return "Videos deleted successfully";
    }

    private List<String> bucketObjects(String bucketName) {
        return s3Client
                .listObjects(bucketName)
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .toList();
    }

    public void deleteVideo(String filename) {
        s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, filename));
    }
}
