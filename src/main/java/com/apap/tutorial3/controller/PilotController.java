package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("/pilot/add")
	public String add(
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "flyHour", required = true) int flyHour
			) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}
	
	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		model.addAttribute("pilot",	archive);
		return "view-pilot";
	}
	
	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		
		model.addAttribute("listPilot", archive);
		return "viewall-pilot";
	}
	
	/*
	 * LATIHAN NOMOR 1
	 */
	@RequestMapping(value = {"/pilot/view/license-number", "/pilot/view/license-number/{licenseNumber}"})
	public String view(@PathVariable Optional<String> licenseNumber, Model model) {
		String errorMessage = "";
		if (licenseNumber.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if (archive == null) {
				errorMessage = "Error! Pilot dengan license number: " + licenseNumber.get() + " tidak ditemukan!";
				model.addAttribute("errorMessage", errorMessage);
				return "error-page";
			} else {
				model.addAttribute("pilot", archive);
				return "view-pilot";
			}
		} else {
			errorMessage = "Error! license number kosong!";
			model.addAttribute("errorMessage", errorMessage);
			return "error-page";
		}
	}
	
	/*
	 * LATIHAN NOMOR 2
	 */
	@RequestMapping(value = {"/pilot/update/license-number/{licenseNumber}/fly-hour/{newFlyHour}", "/pilot/update/license-number/", "/pilot/update/license-number/{licenseNumber}", "/pilot/update/license-number/fly-hour/{newFlyHour}"})
	public String updateFlyHour(@PathVariable Optional<String> licenseNumber, @PathVariable Optional<Integer> newFlyHour, Model model) {
		String message = "";
		if (licenseNumber.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if (archive == null) {
				message = "Error! Pilot dengan license number: " + licenseNumber.get() + " tidak ditemukan! Proses update dibatalkan!";
				model.addAttribute("errorMessage", message);
				return "error-page";
			} else {
				message = "Fly hour berhasil di update!";
				archive.setFlyHour(newFlyHour.get());
				model.addAttribute("message", archive);
				return "result";
			}
		} else {
			message = "Error! license number kosong! Proses update dibatalkan!";
			model.addAttribute("errorMessage", message);
			return "error-page";
		}
	}
	
	/*
	 * LATIHAN NOMOR 3
	 */
	@RequestMapping(value = {"/pilot/delete/id", "/pilot/delete/id/{id}"})
	public String deleteId(@PathVariable Optional<String> id, Model model) {
		String message = "";
		if (id.isPresent()) {
			List<PilotModel> archive = pilotService.getPilotList();
			for (int i=0; i<archive.size(); i++) {
				if (archive.get(i).getId().equals(id.get())) {
					message = "Yeay! Pilot dengan id: " + archive.get(i).getId() + " berhasil dihapus!";
					model.addAttribute("messageDelete", message);
					archive.remove(i);
					return "result";
				}
			}
			message = "Error! Id yang dimasukkan tidak ada! Proses delete dibatalkan!";
			model.addAttribute("errorMessage", message);
			return "error-page";
		} else {
			message = "Error! Id kosong! Proses delete dibatalkan!";
			model.addAttribute("errorMessage", message);
			return "error-page";		
		}
	}
}
