package com.example.busmanagement.service;

import com.example.busmanagement.domain.Bus;
import com.example.busmanagement.repository.BusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BusService {

    @Value("${bus.max.delay}")
    private double maxDelay;

    private final BusRepository busRepository;
    private final Random random = new Random();

    private static final Logger logger = LoggerFactory.getLogger(BusService.class);

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }

    public Bus createBus(Bus bus) {
        try {
            bus.setScheduledArrivalTime(10.00);
            bus.setActualArrivalTime(10.00);
            return busRepository.save(bus);
        } catch (Exception e) {
            logger.error("Error creating bus", e);
            throw new RuntimeException("Error creating bus: " + e.getMessage(), e);
        }
    }

    public Bus updateBus(Long id, Bus busDetails) {
        Bus bus = busRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
        bus.setBusName(busDetails.getBusName());
        bus.setCapacity(busDetails.getCapacity());
        bus.setOccupancy(busDetails.getOccupancy());
        bus.setCurrentLocation(busDetails.getCurrentLocation());
        bus.setOperating(busDetails.isOperating());
        bus.setRoutes(busDetails.getRoutes());
        return busRepository.save(bus);
    }

    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    public Bus updateOccupancy(Long busId, int change) {
        Bus bus = busRepository.findById(busId).orElseThrow(() -> new RuntimeException("Bus not found"));
        int currentOccupancy = bus.getOccupancy();
        int newOccupancy = Math.max(0, Math.min(currentOccupancy + change, (int) bus.getCapacity())); // Ensure occupancy is within bounds
        bus.setOccupancy(newOccupancy);
        return busRepository.save(bus);
    }

    @Scheduled(fixedRate = 5000) // Updates every 5 seconds
    public void updateBusStatus() {
        List<Bus> buses = busRepository.findAll();
        buses.forEach(bus -> {
            int currentOccupancy = (int) bus.getOccupancy();
            int capacity = (int) bus.getCapacity();

            // Handle null currentLocation
            String currentLocation = bus.getCurrentLocation();

            if (currentLocation != null) {
                String currentLocationNumberStr = currentLocation.replaceAll("[^0-9]", ""); // Extract only numbers

                int currentLocationNumber = 0; // Default to 0 if no valid number is found
                if (!currentLocationNumberStr.isEmpty()) {
                    try {
                        currentLocationNumber = Integer.parseInt(currentLocationNumberStr); // Parse if valid number
                    } catch (NumberFormatException e) {
                        logger.error("Error parsing location number from: " + currentLocation, e);
                    }
                }

                int locationChange = (int) (Math.random() * 5) - 2; // Small change in location (between -2 and +2)
                int newLocationNumber = Math.max(0, currentLocationNumber + locationChange);
                String newLocation = "Location " + newLocationNumber;
                bus.setCurrentLocation(newLocation);
            } else {
                logger.warn("Bus {} has a null current location. Setting default value.", bus.getId());
                bus.setCurrentLocation("Location 0"); // Set a default location if currentLocation is null
            }

            bus.setOperating(Math.random() > 0.5);

            // 70% chance the bus arrives on time, 30% chance it's delayed
            if (Math.random() < 0.7) {
                // Bus arrives on time
                bus.setActualArrivalTime(bus.getScheduledArrivalTime());
            } else {
                // Bus is delayed (by a customizable amount)
                double delay = random.nextDouble() * maxDelay; // Using maxDelay from configuration
                bus.setActualArrivalTime(bus.getScheduledArrivalTime() + delay);

                // Log the delay using the logger
                logger.info("Bus {} is delayed by {} minutes.", bus.getBusName(), delay);
            }

            busRepository.save(bus);
        });
    }


    public List<Bus> getBusesByRoute(String route) {
        return busRepository.findAll().stream()
                .filter(bus -> bus.getRoutes().contains(route))
                .collect(Collectors.toList());
    }
}
