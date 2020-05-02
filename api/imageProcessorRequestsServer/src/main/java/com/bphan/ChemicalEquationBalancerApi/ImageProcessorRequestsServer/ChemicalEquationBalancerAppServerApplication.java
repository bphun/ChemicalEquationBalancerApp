package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer;

import com.bphan.ChemicalEquationBalancerApi.common.logging.LoggableDispatcherServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication(scanBasePackages = "com.bphan.ChemicalEquationBalancerApi")
@EnableEurekaClient
public class ChemicalEquationBalancerAppServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChemicalEquationBalancerAppServerApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration() {
        return new ServletRegistrationBean<DispatcherServlet>(dispatcherServlet());
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LoggableDispatcherServlet();
    }
}