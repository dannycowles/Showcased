package com.example.showcased.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.showcased.entity.User;
import com.example.showcased.exception.InvalidFileType;
import com.example.showcased.exception.UserNotFoundException;
import com.example.showcased.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.accessKey}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;

    private AmazonS3 s3Client;
    private final UserRepository userRepository;

    public FileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void initialize() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_WEST_2)
                .build();
    }


    public String uploadProfilePicture(MultipartFile file, HttpSession session) {
        // Retrieve user's ID and format file name/location in S3 bucket
        String id = session.getAttribute("user").toString();
        String fileName = "users/" + id;

        // Ensure that the provided file is an image
        if (!file.getContentType().startsWith("image/")) {
            throw new InvalidFileType("Only image files are allowed");
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // Check if the user already has a profile picture, if so we remove it to replace with new one
            if (s3Client.doesObjectExist(bucketName, fileName)) {
                s3Client.deleteObject(bucketName, fileName);
            }
            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);

            // Retrieve the generated url and store it in the user table
            String profilePictureUrl = s3Client.getUrl(bucketName, fileName).toString();
            User user = userRepository.findById(Long.parseLong(id)).
                    orElseThrow(() -> new UserNotFoundException(Long.parseLong(id)));
            user.setProfilePicture(profilePictureUrl);
            userRepository.save(user);

            return profilePictureUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
