package org.sportstogo.backend.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.sportstogo.backend.Models.Image;
import org.sportstogo.backend.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final String s3AccessKey;
    private final String s3SecretKey;
    private final String s3BucketName;

    public ImageService(
            ImageRepository imageRepository,
            @Value("${aws.s3.access-key}") String s3AccessKey,
            @Value("${aws.s3.secret-key}") String s3SecretKey,
            @Value("${aws.s3.bucket-name}") String s3BucketName
    ) {
        this.imageRepository = imageRepository;
        this.s3AccessKey = s3AccessKey;
        this.s3SecretKey = s3SecretKey;
        this.s3BucketName = s3BucketName;
    }

    public Image saveImage(MultipartFile file) {
        validateImage(file);

        String imageUrl = saveImageToS3(file);
        Image image = new Image();
        image.setUrl(imageUrl);

        return imageRepository.save(image);
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty.");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 5MB limit.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid file type. Only image files are allowed.");
        }
    }

    private String saveImageToS3(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, file.getInputStream(), metadata);

            s3Client.putObject(putObjectRequest);

            return "https://" + s3BucketName + ".s3.amazonaws.com/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image to S3", e);
        }
    }
}
