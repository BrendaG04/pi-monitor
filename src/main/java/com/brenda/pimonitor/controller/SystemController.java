package com.brenda.pimonitor.controller;

import com.brenda.pimonitor.model.SystemStats;
import com.brenda.pimonitor.service.SystemMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
	Controller Class - Web Server Requests ~ API endpoints
*/
@RestController
public class SystemController {

	//Dependency Injection (service is automaticalally provided)
	@Autowired
	private SystemMonitorService monitorService;

	@GetMapping("/")
	public String home() {
		return "Welcome to Brenda's Pi Monitor API! Try endpoint /stats";
	}

	@GetMapping("/stats")
	public SystemStats getStats()  {
		try {
			return monitorService.getSystemStats();
		}catch (Exception e ){
			throw new RuntimeException("Error reading system stats: " + e.getMessage());
		}
	}

	@GetMapping("/temperature")
	public String getTemperature() {
		try {
			return "CPU Temperature: " + monitorService.getCpuTemperature() + "°C";
		} catch (Exception e){
			return "Error reading temperature: " + e.getMessage();
		}
	}
}
