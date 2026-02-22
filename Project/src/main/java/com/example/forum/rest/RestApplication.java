package com.example.forum.rest;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TopicResource.class);
        classes.add(PostResource.class);
        classes.add(JsonbConfig.class);
        classes.add(OpenApiResource.class);
        return classes;
    }
}