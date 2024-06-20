package com.example.HMS.controller;

import com.example.HMS.domain.Doctor;
import com.example.HMS.domain.Patient;
import com.example.HMS.repository.DoctorRepository;
import com.example.HMS.repository.PatientRepository;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class WebController {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @GetMapping("/")
    public String getAllPatients(Model model) throws IOException {
        List<Patient> patients = patientRepository.getAllPatients();
        model.addAttribute("patients", patients);
        return "patients";
    }

    @GetMapping("/patient/{email}")
    public String getPatients(@PathVariable String email, Model model) throws IOException {
        Patient patient = patientRepository.findPatientByEmail(email);
        model.addAttribute("patient", patient);
        return "patients";
    }
    @PostMapping("/add")
    public String addPatient(@ModelAttribute Patient patient) throws IOException {
        patientRepository.addPatient(patient);
        return "redirect:/doctors/";
    }

    @GetMapping("/search")
    public String searchPatientById(@RequestParam("id") int id, Model model) throws IOException {
        Patient patient = patientRepository.findPatientById(id);
        if (patient != null) {
            model.addAttribute("patient", patient);
        } else {
            model.addAttribute("error", "Patient not found");
        }
        List<Patient> patients = patientRepository.getAllPatients();
        List<Doctor> doctors = doctorRepository.getAllDoctors();
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);
        return "doctors";
    }

    @PostMapping("/update")
    public String updatePatient(@ModelAttribute Patient updatedPatient,  Model model) throws IOException {
        List<Patient> patients = patientRepository.getAllPatients();
        List<Doctor> doctors = doctorRepository.getAllDoctors();
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);
        try {
            Patient existingPatient = patientRepository.findPatientById(updatedPatient.getId());
            if (existingPatient == null) {
                return "redirect:/doctors/";
            }
            existingPatient.setName(updatedPatient.getName());
            existingPatient.setAge(updatedPatient.getAge());
            patientRepository.updatePatient(existingPatient);
            return "redirect:/doctors/";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/doctors/";
        }
    }


}
