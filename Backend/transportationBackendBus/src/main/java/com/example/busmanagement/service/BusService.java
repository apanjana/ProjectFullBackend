package com.example.busmanagement.service;

import com.example.busmanagement.domain.Bus;
import com.example.busmanagement.repository.BusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
    private final List<String> notifications = new ArrayList<>();

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }

    public List<Bus> getDelayedBuses() {
        List<Bus> allBuses = busRepository.findAll();
        return allBuses.stream()
                .filter(bus -> bus.getActualArrivalTime() > bus.getScheduledArrivalTime()) // Buses that are delayed
                .collect(Collectors.toList());
    }

    // Bus creation without actualArrivalTime and currentLocation input from user
    public Bus createBus(Bus bus) {
        try {
            bus.setScheduledArrivalTime(10.00);  // Example fixed scheduled time
            simulateBusDetails(bus);  // Simulate the actual arrival time and current location
            return busRepository.save(bus);
        } catch (Exception e) {
            logger.error("Error creating bus", e);
            throw new RuntimeException("Error creating bus: " + e.getMessage(), e);
        }
    }

    // Update the bus without requiring input for arrival time or location
    public Bus updateBus(Long id, Bus busDetails) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        bus.setBusName(busDetails.getBusName());
        bus.setCapacity(busDetails.getCapacity());
        bus.setOccupancy(busDetails.getOccupancy());
        bus.setOperating(busDetails.isOperating());
        bus.setRoutes(busDetails.getRoutes());

        simulateBusDetails(bus);  // Simulate the actual arrival time and current location
        return busRepository.save(bus);
    }

    // Simulates the actual arrival time and current location
    private void simulateBusDetails(Bus bus) {
        // Simulate current location
        int newLocationNumber = random.nextInt(100); // Random location (between 0 and 99)
        String newLocation = "Location " + newLocationNumber;
        bus.setCurrentLocation(newLocation);

        // 70% chance the bus arrives on time, 30% chance it's delayed
        if (Math.random() < 0.7) {
            // Bus arrives on time
            bus.setActualArrivalTime(bus.getScheduledArrivalTime());
        } else {
            // Bus is delayed (by a customizable amount)
            double delay = random.nextDouble() * maxDelay; // Using maxDelay from configuration
            bus.setActualArrivalTime(bus.getScheduledArrivalTime() + delay);

            // Add delay notification
            String notification = "Bus " + bus.getBusName() + " is delayed by " + delay + " minutes.";
            notifications.add(notification);
            logger.info(notification);
        }
    }

    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    public Bus updateOccupancy(Long busId, int change) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        int currentOccupancy = bus.getOccupancy();
        int newOccupancy = Math.max(0, Math.min(currentOccupancy + change, (int) bus.getCapacity()));
        bus.setOccupancy(newOccupancy);
        return busRepository.save(bus);
    }

    // Method to update bus status every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void updateBusStatus() {
        List<Bus> buses = busRepository.findAll();
        buses.forEach(bus -> {
            int locationChange = (int) (Math.random() * 5) - 2; // Small change in location (between -2 and +2)
            String currentLocation = bus.getCurrentLocation();
            String currentLocationNumberStr = currentLocation.replaceAll("[^0-9]", "");

            int currentLocationNumber = 0;
            if (!currentLocationNumberStr.isEmpty()) {
                try {
                    currentLocationNumber = Integer.parseInt(currentLocationNumberStr);
                } catch (NumberFormatException e) {
                    logger.error("Error parsing location number from: " + currentLocation, e);
                }
            }

            int newLocationNumber = Math.max(0, currentLocationNumber + locationChange);
            bus.setCurrentLocation("Location " + newLocationNumber);
            bus.setOperating(Math.random() > 0.5);

            // Simulate delay logic as before
            if (Math.random() < 0.7) {
                bus.setActualArrivalTime(bus.getScheduledArrivalTime());
            } else {
                double delay = random.nextDouble() * maxDelay;
                bus.setActualArrivalTime(bus.getScheduledArrivalTime() + delay);
                String notification = "Bus " + bus.getBusName() + " is delayed by " + delay + " minutes.";
                notifications.add(notification);
                logger.info(notification);
            }

            busRepository.save(bus);
        });
    }

    public List<Bus> getBusesByRoute(String route) {
        return busRepository.findAll().stream()
                .filter(bus -> bus.getRoutes().contains(route))
                .collect(Collectors.toList());
    }

    public List<Bus> getBusesByRoutePattern(String startRoute, String endRoute) {
        logger.info("Searching for buses from '{}' to '{}'", startRoute, endRoute);

        List<Bus> allBuses = busRepository.findAll();
        logger.info("Total buses found: {}", allBuses.size());

        List<Bus> filteredBuses = allBuses.stream()
                .filter(bus -> {
                    List<String> routes = bus.getRoutes();
                    int startIndex = routes.indexOf(startRoute);
                    int endIndex = routes.indexOf(endRoute);
                    boolean matches = startIndex >= 0 && endIndex >= 0 && startIndex < endIndex;
                    logger.info("Bus {}: matches = {}", bus.getBusName(), matches);
                    return matches;
                })
                .collect(Collectors.toList());

        logger.info("Total buses matching the criteria: {}", filteredBuses.size());
        return filteredBuses;
    }

    // Method to get all delay notifications
    public List<String> getDelayNotifications() {
        return new ArrayList<>(notifications);
    }
}
