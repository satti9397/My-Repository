package com.demo.paymenttransfer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerDocumentConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Master Card Assignment")
                .description("APIs to handle Modern Bank PLC's internal account transactions.")
                .license("")
                .licenseUrl("")
                .termsOfServiceUrl("")
                .version("1.0")
                .contact(new Contact("", "", "satti9397@gmail.com"))
                .build();
    }

    @Bean
    public Docket customImplementation() {
        Set<String> protocols = new HashSet<>();
        protocols.add("http");
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.demo.paymenttransfer"))
                .build()
                .apiInfo(apiInfo())
                .protocols(protocols)
                ;
    }

}
