/*package com.example.busmanagement.service;

import com.example.busmanagement.domain.Bus;
import com.example.busmanagement.repository.BusRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusSimulationService {

    private final BusRepository busRepository;

    public BusSimulationService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    // Simulating real-time updates for bus occupancy and location
    @Scheduled(fixedRate = 5000) // Updates every 5 seconds
    public void updateBusStatus() {
        List<Bus> buses = busRepository.findAll();
        buses.forEach(bus -> {
            // Gradual change in occupancy (e.g., between -5 and +5)
            int currentOccupancy = (int) bus.getOccupancy();
            int capacity = (int) bus.getCapacity();

            // Random change between -5 and +5, but ensuring it doesn't exceed the capacity
            int occupancyChange = (int) (Math.random() * 10) - 5; // Between -5 and +5
            int newOccupancy = Math.max(0, Math.min(currentOccupancy + occupancyChange, capacity)); // Stay within 0 to capacity
            bus.setOccupancy(newOccupancy);

            // Gradual location change
            // Assuming location is a number for simplicity, but you can use more complex data types for actual geo-coordinates
            String currentLocation = bus.getCurrentLocation();
            int locationChange = (int) (Math.random() * 5) - 2; // Small change in location (between -2 and +2)
            int currentLocationNumber = Integer.parseInt(currentLocation.replaceAll("[^0-9]", ""));
            int newLocationNumber = Math.max(0, currentLocationNumber + locationChange); // Ensure positive location value
            String newLocation = "Location " + newLocationNumber;
            bus.setCurrentLocation(newLocation);

            // Simulating bus operational state (randomly set it as operating or not)
            bus.setOperating(Math.random() > 0.5);

            // Saving the updated bus information
            busRepository.save(bus);

            // Logging for confirmation
            System.out.println("Updated bus status for: " + bus.getBusName() +
                    //" | New Occupancy: " + newOccupancy +
                    " | Location: " + newLocation +
                    " | Operating: " + (bus.isOperating() ? "Yes" : "No"));
        });
    }
}*/
