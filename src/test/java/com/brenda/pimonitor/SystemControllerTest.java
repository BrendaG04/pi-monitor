package com.brenda.pimonitor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

/**
	Integration Testing ~ Controller / Http Endpooint
*/

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SystemControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testHomeEndpoint() {
		String url = "http://localhost:" + port + "/";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");

		assertTrue(response.getBody().contains("Brenda"), "Should mention Brenda");
		assertTrue(response.getBody().contains("Pi Monitor"), "Should mention Pi Monitor");

		System.out.println("✔ Home endpoint test passed!");
	}

	@Test
	public void testTemperatureEndpoint() {
		String url = "http://localhost:" + port + "/temperature";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");

		assertTrue(response.getBody().contains("CPU Temperature"), "Should contain 'CPU Temperatureee'");
		assertTrue(response.getBody().contains("°C"), "Should contain degree symbol");

		System.out.println("✔ Temperature endpoint test passed!");
	}

	@Test
	public void testStatsEndpoint() {
		String url = "http://localhost:"+ port +"/stats";
		ResponseEntity<SystemStats> response = restTemplate.getForEntity(url, SystemStats.class);

		assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");

		SystemStats stats = response.getBody();
		assertNotNull(stats, "Should return SystemStats Object");

		assertTrue(stats.getCpuTemperature() > 20.0, "CPU temp should be reasonable");
		assertTrue(stats.getTotalMemoryMB() > 512, "Total memory should be reasonable");
		assertTrue(stats.getMemoryUsagePercent() > 0, "Memory usage should be positive"); 
		assertNotNull(stats.getUptime(), "Uptime should not be null");

		System.out.println("✔ SystemStats enpoint test passed!");
		System.out.println(" Temperature: " + stats.getCpuTemperature() + "°C");
		System.out.println(" Memory: "+ stats.getUsedMemoryMB() + "/" + stats.getTotalMemoryMB() + " MB (" + String.format("%.1f", stats.getMemoryUsagePercent()) + "%)");
		System.out.println(" Uptime: " + stats.getUptime());
	}
}
