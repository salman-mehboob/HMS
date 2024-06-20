package com.example.HMS.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.HMS.domain.Doctor;

import java.io.IOException;
import java.util.List;

@Repository
public class DoctorRepository {
    private static final String FILE_PATH = "classpath:doctors.json";

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public DoctorRepository(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    public List<Doctor> getAllDoctors() throws IOException {
        Resource resource = resourceLoader.getResource(FILE_PATH);
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Doctor>>() {});
    }
}
