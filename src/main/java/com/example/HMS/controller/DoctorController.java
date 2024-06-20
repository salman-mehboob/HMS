package com.example.HMS.controller;

import com.example.HMS.domain.Doctor;
import com.example.HMS.domain.Patient;
import com.example.HMS.repository.DoctorRepository;
import com.example.HMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController {
	@Autowired
	private DoctorRepository doctorRepository;
	@Autowired
	private PatientRepository patientRepository;

	@GetMapping("/")
	public String getAllDoctors(Model model) throws IOException {
		List<Doctor> doctors = doctorRepository.getAllDoctors();
		List<Patient> patients = patientRepository.getAllPatients();
		model.addAttribute("doctors", doctors);
		model.addAttribute("patients", patients);
		return "doctors";
	}
}

