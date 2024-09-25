package com.example.busmanagement.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "bus")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String busName;
    private double capacity;
    private int occupancy;
    private String currentLocation;
    private boolean isOperating;

    @ElementCollection
    @CollectionTable(name = "bus_routes", joinColumns = @JoinColumn(name = "bus_id"))
    @Column(name = "route")
    private List<String> routes; // List of routes the bus is running

    private double scheduledArrivalTime;
    private double actualArrivalTime;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isOperating() {
        return isOperating;
    }

    public void setOperating(boolean operating) {
        isOperating = operating;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public double getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(double scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public double getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(double actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }
}