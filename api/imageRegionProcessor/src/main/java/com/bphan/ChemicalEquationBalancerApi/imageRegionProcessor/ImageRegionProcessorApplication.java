package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor;

import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageRegionProcessor.ImageRegionExtractor;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageTransformer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.bphan.ChemicalEquationBalancerApi")
public class ImageRegionProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageRegionProcessorApplication.class, args);
    }

    @Bean
    public ImageTransformer imageTransformer() {
        return new ImageTransformer();
    }

    @Bean
    public ImageRegionExtractor imageRegionExtractor() {
        return new ImageRegionExtractor();
    }
}
