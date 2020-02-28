package com.bphan.ChemicalEquationBalancerApi.common.amazon;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AwsS3Client {

    private AmazonS3 s3Client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_WEST_1).build();
    }

    public String uploadImage(String s3ImageFileName, String base64EncodedImage) {
        String fileUrl = "";

        try {
            File file = generateImageFileFromBase64String(s3ImageFileName, base64EncodedImage);
            fileUrl = endpointUrl + "/" + bucketName + "/" + s3ImageFileName + ".png";
            uploadTos3Bucket(s3ImageFileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileUrl;
    }

    public String deleteImageByName(String s3ImageFileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, s3ImageFileName + ".png"));
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return "success";
    }

    private File generateImageFileFromBase64String(String s3ImageFileName, String base64EncodedImage) {
        BufferedImage image = null;
        byte[] imageBytes;
        File imageFile = new File(s3ImageFileName + ".png");

        try {
            Decoder decoder = Base64.getDecoder();
            imageBytes = decoder.decode(base64EncodedImage);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            image = ImageIO.read(bis);
            bis.close();
            ImageIO.write(image, "png", imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageFile;
        // File imageFile = null;
        // byte[] imageBytes;

        // try {
        //     imageBytes = Base64.getDecoder().decode(base64EncodedImage);
        //     ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

        //     imageFile = new File(s3ImageFileName + ".png");
        //     ImageIO.write(ImageIO.read(byteArrayInputStream), ".png", imageFile);

        //     byteArrayInputStream.close();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // return imageFile;
    }

    private String base64EncodeImageFile(File imageFile) {
        String base64EncodedImage = "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(ImageIO.read(imageFile), ".png", byteArrayOutputStream);
            base64EncodedImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return base64EncodedImage;
    }

    private void uploadTos3Bucket(String filename, File file) {
        s3Client.putObject(
                new PutObjectRequest(bucketName, filename + ".png", file).withCannedAcl(CannedAccessControlList.PublicRead));
    }
}