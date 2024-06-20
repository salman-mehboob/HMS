package com.example.HMS.repository;

import com.example.HMS.domain.Patient;
import com.example.HMS.domain.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PatientRepository {
    private static final String FILE_PATH = "classpath:Patients.json";
    private static final String USER_FILE_PATH = "classpath:users.json";

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public PatientRepository(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    public List<Patient> getAllPatients() throws IOException {
        Resource resource = resourceLoader.getResource(FILE_PATH);
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Patient>>() {});
    }

    public List<User> getAllUsers() throws IOException {
        Resource resource = resourceLoader.getResource(USER_FILE_PATH);
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<User>>() {});
    }
    public void addPatient(Patient patient) throws IOException {
        List<Patient> patients = getAllPatients();
        List<User> users = getAllUsers();
        User user = User.builder()
                .id(patient.getId())
                .email(patient.getEmail())
                .password("12345")
                .role("patient")
                .build();
        users.add(user);
        patients.add(patient);
        try {
            objectMapper.writeValue(resourceLoader.getResource(FILE_PATH).getFile(), patients);
            objectMapper.writeValue(resourceLoader.getResource(USER_FILE_PATH).getFile(), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Patient findPatientById(int id) throws IOException {
        List<Patient> patients = getAllPatients();
        Optional<Patient> patientOptional = patients.stream().filter(p -> p.getId() == id).findFirst();
        return patientOptional.orElse(null);
    }

    public Patient findPatientByEmail(String email) throws IOException {
        List<Patient> patients = getAllPatients();
        Optional<Patient> patientOptional = patients.stream().filter(p -> Objects.equals(p.getEmail(), email)).findFirst();
        return patientOptional.orElse(null);
    }

    public void updatePatient(Patient updatedPatient) throws IOException {
        List<Patient> patients = getAllPatients();
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            if (patient.getId() == updatedPatient.getId()) {
                patients.set(i, updatedPatient);
                objectMapper.writeValue(resourceLoader.getResource(FILE_PATH).getFile(), patients);
                return;
            }
        }
        throw new IllegalArgumentException("Patient with ID " + updatedPatient.getId() + " not found.");
    }
}
