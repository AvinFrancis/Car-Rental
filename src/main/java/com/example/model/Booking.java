package com.example.model;


import java.time.LocalDate;


import jakarta.persistence.*;

@Entity
@Table(name="bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinColumn(name="car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="pickup_location_id",nullable=false)
    private Location pickupLocation;

    @ManyToOne
    @JoinColumn(name="drop_location_id", nullable=false)
    private Location dropoffLocation;

    @Column(nullable=false)
    private LocalDate pickupDate;

    @Column(nullable=false)
    private LocalDate dropoffDate;

    @Column(nullable=false)
    private double totalAmount;

    @Column(nullable=false)
    private boolean isPaid=false;

    //Getters And Setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public Car getCar(){return car;}
    public void setCar(Car car){this.car=car;}

    public User getUser(){return user;}
    public void setUser(User user){this.user=user;}

    public LocalDate getPickuptDate(){return pickupDate;}
    public void setPickupDate(LocalDate pickupDate){this.pickupDate=pickupDate;}

    public LocalDate getDropoffDate(){return dropoffDate;}
    public void setDropoffDate(LocalDate dropoffDate){this.dropoffDate=dropoffDate;}

    public double getTotalAmount(){return totalAmount;}
    public void setTotalAmount(double totalAmount){this.totalAmount=totalAmount;}

    public Location getPickupLocation(){return pickupLocation;}
    public void setPickupLocation(Location pickupLocation){this.pickupLocation=pickupLocation;}

    public Location getDropoffLocation(){return dropoffLocation;}
    public void setDropoffLocation(Location dropoffLocation){this.dropoffLocation=dropoffLocation;}
    
    public boolean isPaid(){return isPaid;}
    public void setPaid(boolean isPaid){this.isPaid=isPaid;}
}
