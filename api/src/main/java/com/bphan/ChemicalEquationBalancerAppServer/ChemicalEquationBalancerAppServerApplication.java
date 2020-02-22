package com.bphan.ChemicalEquationBalancerAppServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

@SpringBootApplication
public class ChemicalEquationBalancerAppServerApplication {

    PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static void main(String[] args) {
		SpringApplication.run(ChemicalEquationBalancerAppServerApplication.class, args);
    }

    // @Bean
    // InitializingBean forcePrometheusPostProcessor(BeanPostProcessor meterRegistryPostProcessor, PrometheusMeterRegistry registry) {
    //     return () -> meterRegistryPostProcessor.postProcessAfterInitialization(registry, "");
    // }
}