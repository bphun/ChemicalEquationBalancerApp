package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor;

import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageRegionProcessor.ImageRegionExtractor;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageOperations;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageTransceiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.bphan.ChemicalEquationBalancerApi")
public class ImageRegionProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageRegionProcessorApplication.class, args);
    }

    @Bean
    public ImageTransceiver imageTransceiver() {
        return new ImageTransceiver();
    }

    @Bean
    public ImageOperations imageOperations() {
        return new ImageOperations();
    }

    @Bean
    public ImageRegionExtractor imageRegionExtractor() {
        return new ImageRegionExtractor();
    }
}
