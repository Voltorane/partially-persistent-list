package com.example.partiallypersistentlistrest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Application entry-point for the PartiallyPersistentList Web Service
 * */
@ApplicationPath("/")
public class PartiallyPersistentListApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(PartiallyPersistentListResource.class);
        classes.add(JacksonJsonProvider.class); // Register custom message body writer
        return classes;
    }
}