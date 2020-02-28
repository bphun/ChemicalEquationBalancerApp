package com.bphan.ChemicalEquationBalancerApi.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.bphan.ChemicalEquationBalancerApi.common.amazon.AwsS3Client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AwsS3Client.class)
class AwsS3ClientTests {

    @Autowired
    private AwsS3Client s3Client;

    @Test
    public void testImageUploadAndDelete() {
        String fileName = "testImage";
        String base64EncodedImage = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=";
        
        assertNotEquals(s3Client.uploadImage(fileName, base64EncodedImage), "");
        assertEquals(s3Client.deleteImageByName(fileName), "success");
    }

}
