package com.example.model;

import jakarta.persistence.*;


@Entity
@Table(name="cars")
public class Car {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable=false)
    private String brand;

    @Column(nullable=false)
    private String licensePlate;

    @Column(nullable=false)
    private double pricePerDay;

    @Column(nullable=false)
    private boolean available=true; //by default the car is available

    //getter and setters
    public long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public String getModel(){return model;}
    public void setModel(String model){this.model=model;}

    public String getBrand(){return brand;}
    public void setBrand(String brand){this.brand=brand;}

    public String getlicensePlate(){return licensePlate;}
    public void setlicensePlate(String licensePlate){this.licensePlate=licensePlate;}

    public double getPricePerDay(){return pricePerDay;}
    public void setPricePerDay(double pricePerDay){this.pricePerDay=pricePerDay;}

    public boolean isAvailable(){return available;}
    public void setAvailable(boolean available){
        this.available=available;
    }




}
