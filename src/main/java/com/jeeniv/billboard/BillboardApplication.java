package com.jeeniv.billboard;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.jeeniv.billboard.model.Billboard;
import com.jeeniv.billboard.model.BillboardType;
import com.jeeniv.billboard.repository.BillboardRepository;
import java.util.List;

@SpringBootApplication
public class BillboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillboardApplication.class, args);
	}
@Bean
    CommandLineRunner initDatabase(BillboardRepository repository) {
        return args -> {
            LocalDate today = LocalDate.now();

            Billboard b1 = new Billboard(
                "BLB-101", "Downtown Central Plaza Unipole", "Downtown",
                "Corner of Main Ave & 4th Street", 40.7128, -74.0060,
                BillboardType.UNIPOLE, "40x20 ft", 4800.0, 85000,
                "Apex Telecom", "5G Nationwide Rollout",
                today.minusMonths(6), today.minusDays(5), false // EXPIRED
            );

            Billboard b2 = new Billboard(
                "BLB-102", "North Highway Gantry LED", "North Highway",
                "Highway 101 Exit 14 Overpass", 40.7306, -73.9352,
                BillboardType.DIGITAL_LED, "50x15 ft", 6200.0, 110000,
                "Summit Motors", "Electric SUV Fall Launch",
                today.minusMonths(3), today.plusDays(6), false // EXPIRING SOON
            );

            Billboard b3 = new Billboard(
                "BLB-103", "Commercial Hub Double Face", "Commercial District",
                "Grand Avenue & Trade Mall", 40.7580, -73.9855,
                BillboardType.UNIPOLE, "30x15 ft", 3500.0, 62000,
                "Prime Energy Drinks", "Charged Up Campaign",
                today.minusMonths(5), today.plusDays(22), false // PENDING RENEWAL
            );

            Billboard b4 = new Billboard(
                "BLB-104", "Metro Transit Station Bus Shelter", "Downtown",
                "Central Terminal Platform East", 40.7505, -73.9934,
                BillboardType.BUS_SHELTER, "12x6 ft", 1400.0, 42000,
                "Metro Bank", "Smart Savings 2026",
                today.minusMonths(2), today.plusMonths(4), false // ACTIVE
            );

            Billboard b5 = new Billboard(
                "BLB-105", "Bayside Boulevard Prime Rooftop", "Waterfront",
                "Marina Pier Viewpoint 8", 40.7028, -74.0160,
                BillboardType.ROOFTOP, "35x18 ft", 3900.0, 54000,
                null, null,
                null, null, true // VACANT
            );

            repository.saveAll(List.of(b1, b2, b3, b4, b5));
            System.out.println(">>> Sample Billboard inventory successfully seeded!");
        };
    }
}
