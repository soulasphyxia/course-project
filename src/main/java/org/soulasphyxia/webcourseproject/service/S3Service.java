package org.soulasphyxia.webcourseproject.service;

import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.soulasphyxia.webcourseproject.user_actions.LogType;
import org.soulasphyxia.webcourseproject.repository.S3Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Repository s3Repository;

    public List<String> getVideos() {
        return s3Repository.getVideos();
    }

    public String uploadVideo(MultipartFile multipartFile) throws IOException {
        String fileName = generateFilename(multipartFile.getOriginalFilename(), new Date());
        File file = new File(fileName);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }
        s3Repository.uploadFile(file);
        return getDownloadLink(fileName);
    }

    public String  uploadThumbnail(BufferedImage thumbnail, String key) throws IOException {
        String fileName = generateFilename(key, new Date());
        s3Repository.uploadImage(thumbnail, fileName);
        return getDownloadLink(fileName);
    }

    public void uploadLog(InputStream bytes, LogType logType) {
        String key = "logs/log-%s.%s".formatted(new LocalDateTime().toString(), logType.extension);
        s3Repository.uploadLog(bytes, key, logType);
    }

    public String deleteVideo(String filename) {
        s3Repository.deleteVideo(filename);
        return "File deleted: " + filename;
    }

    public String deleteVideos() {
        return s3Repository.deleteVideos();
    }

    private String getDownloadLink(String filename) {
        String url = "http://localhost:9000/";
        String bucketName = "corpbucket";
        return String.format(url +String.format("%s/",bucketName)+ filename);
    }


    private String generateFilename(String filename, Date date) {
        return date.getTime() + "-" + filename.replace(" ","_");
    }
}
