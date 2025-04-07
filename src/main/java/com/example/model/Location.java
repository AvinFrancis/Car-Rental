package com.example.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="locations")
public class Location {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false) 
    private String address;

    @Column(nullable=false)
    private String city;

    @Column(nullable=false)
    private String country;
    
    @OneToMany(mappedBy="pickupLocation")
    @JsonIgnore
    private Set<Booking> pickups= new HashSet<>();

    @OneToMany(mappedBy="dropoffLocation")
    @JsonIgnore
    private Set<Booking> dropoffs=new HashSet<>();

    //Getters and Setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Booking> getPickups() {
        return pickups;
    }
    public void setPickups(Set<Booking> pickups) {
        this.pickups = pickups;
    }

    public Set<Booking> getDropoffs() { return dropoffs;}
    public void setDropoffs(Set<Booking> dropoffs) {this.dropoffs = dropoffs;}
}
