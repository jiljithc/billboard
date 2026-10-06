package com.jeeniv.billboard;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.jeeniv.billboard.model.Billboard;
import com.jeeniv.billboard.model.BillboardType;
import com.jeeniv.billboard.repository.BillboardRepository;


@SpringBootApplication
public class BillboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillboardApplication.class, args);
	}
@Bean
CommandLineRunner initDatabase(BillboardRepository repository) {
    return args -> {
        // Only seed if table is currently empty
        if (repository.count() == 0) {
            LocalDate today = LocalDate.now();
            Billboard b1 = new Billboard(
                "BLB-101", "Downtown Central Plaza Unipole", "Downtown",
                "Corner of Main Ave & 4th Street", 40.7128, -74.0060,
                BillboardType.UNIPOLE, "40x20 ft", 4800.0, 85000,
                "Apex Telecom", "5G Nationwide Rollout",
                today.minusMonths(6), today.minusDays(5), false
            );
            repository.save(b1);
            // Add others as needed...
            System.out.println(">>> Seed data populated.");
        } else {
            System.out.println(">>> Database already initialized, skipping seed data.");
        }
    };
}
}
