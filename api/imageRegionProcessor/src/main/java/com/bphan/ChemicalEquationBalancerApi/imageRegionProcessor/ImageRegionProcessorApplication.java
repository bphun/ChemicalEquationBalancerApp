package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor;

import com.bphan.ChemicalEquationBalancerApi.common.logging.LoggableDispatcherServlet;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageRegionProcessor.ImageRegionExtractor;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageOperations;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageTransceiver;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.apiInterfaces.ImageProcessorApiInterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication(scanBasePackages = "com.bphan.ChemicalEquationBalancerApi")
@EnableEurekaClient
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

    @Bean
    public ImageProcessorApiInterface imageProcessorApiInterface() {
        return new ImageProcessorApiInterface();
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LoggableDispatcherServlet();
    }

    @Bean  
    public TaskExecutor taskExecutor() {  
      ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();  
      threadPoolTaskExecutor.setCorePoolSize(1);  
      threadPoolTaskExecutor.setMaxPoolSize(5);  
      return threadPoolTaskExecutor;  
    }  
}
