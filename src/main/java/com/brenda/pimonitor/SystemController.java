package com.brenda.pimonitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController 
public class SystemController {
	
	@GetMapping("/")
	public String home() {
		return "Welcome to Brenda's Pi Monitor!"; 
	}


	@GetMapping("/temperature")
	public String getTemperature() throws Exception {
		String temp = Files.readString(Paths.get("/sys/class/thermal/thermal_zone0/temp"));
		double celcius = Double.parseDouble(temp.trim()) / 1000.0;
		return "CPU Temperature: " + celcius + "C";
	}
}
