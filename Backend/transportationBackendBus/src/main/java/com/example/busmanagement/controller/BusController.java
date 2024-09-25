package com.example.busmanagement.controller;

import com.example.busmanagement.domain.Bus;
import com.example.busmanagement.service.BusService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Bus> getAllBuses() {
        return busService.getAllBuses();
    }

    @GetMapping("/{id}")
    public Optional<Bus> getBusById(@PathVariable Long id) {
        return busService.getBusById(id);
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:4200")
    //@CrossOrigin(origins = "http://localhost:50183/bus")
    public Bus createBus(@RequestBody Bus bus) {
        return busService.createBus(bus);
    }

    @PutMapping("/{id}")
    public Bus updateBus(@PathVariable Long id, @RequestBody Bus busDetails) {
        return busService.updateBus(id, busDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
    }

    @PutMapping("/{busId}/occupancy")
    public Bus updateOccupancy(@PathVariable Long busId, @RequestParam int change) {
        return busService.updateOccupancy(busId, change);
    }

    @GetMapping("/route")
    public List<Bus> getBusesByRoute(@RequestParam String route) {
        return busService.getBusesByRoute(route);
    }
}
