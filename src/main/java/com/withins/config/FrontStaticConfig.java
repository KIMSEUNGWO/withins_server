package com.withins.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.net.URI;

@Configuration
public class FrontStaticConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(getResolver());
    }

    private PathResourceResolver getResolver() {
        return new PathResourceResolver() {

            @Override
            protected Resource getResource(String resourcePath, Resource location) throws IOException {
                Resource requestedResource = location.createRelative(resourcePath);
                URI uri = requestedResource.getURI();
                System.out.println("uri = " + uri);
                System.out.println(requestedResource.exists() && requestedResource.isReadable());
                if (requestedResource.exists() && requestedResource.isReadable()) {
                    return requestedResource;
                } else {
                    return new ClassPathResource("/static/index.html");
                }
            }
        };
    }

}
