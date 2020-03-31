package com.bphan.ChemicalEquationBalancerApi.common.amazon;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.amazonaws.services.s3.model.S3Object;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AwsS3Client {

    private AmazonS3 s3Client;

    private Logger logger = Logger.getLogger(AwsS3Client.class.getName());

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
        File file = generateImageFileFromBase64String(s3ImageFileName, base64EncodedImage);

        fileUrl = uploadFile(s3ImageFileName, file);

        return fileUrl;
    }

    public String uploadImage(String s3ImageFileName, BufferedImage image) {
        String fileUrl = "";
        File file = convertTofile(s3ImageFileName, image);
        
        fileUrl = uploadFile(s3ImageFileName, file);

        return fileUrl;
    }

    public String uploadFile(String filename, File file) {
        String fileUrl = "";

        try {
            fileUrl = endpointUrl + "/" + bucketName + "/" + filename + ".png";
            uploadTos3Bucket(filename, file);

            logger.log(Level.INFO, "Uploaded file " + filename + " (" + bucketName + ")");
        } catch (Exception e) {
            logger.warning(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return fileUrl;
    }

    public String deleteImageByName(String s3ImageFileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, s3ImageFileName + ".png"));
            logger.log(Level.INFO, "Deleted image " + s3ImageFileName + ".png (" + bucketName + ")");
            return "success";
        } catch (Exception e) {
            String errorMessage = e.getLocalizedMessage();
            logger.warning(e.getLocalizedMessage());
            return errorMessage;
        }
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
            logger.warning(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return imageFile;
    }

    private File convertTofile(String fileName, BufferedImage image) {
        File outputFile = new File(fileName);

        try {
            ImageIO.write(image, "png", outputFile);
        } catch (Exception e) {
            logger.warning(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return outputFile;
    }

    private String base64EncodeImageFile(File imageFile) {
        String base64EncodedImage = "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(ImageIO.read(imageFile), ".png", byteArrayOutputStream);
            base64EncodedImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            byteArrayOutputStream.close();
        } catch (Exception e) {
            logger.warning(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return base64EncodedImage;
    }

    private void uploadTos3Bucket(String filename, File file) {
        Thread uploaderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                s3Client.putObject(new PutObjectRequest(bucketName, filename + ".png", file)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                file.delete();
            }
        });
        uploaderThread.start();
    }

    public S3Object getObjectFromS3Bucket(String filename) {
        return s3Client.getObject(bucketName, filename);
    }
}