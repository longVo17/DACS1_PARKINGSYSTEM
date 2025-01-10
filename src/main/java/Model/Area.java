package Model;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Area {
    @Id
    private String areaName;
    private int capacity, numberOfVehicle;

    @ManyToOne
    @JoinColumn(name = "nameParkingLot")
    private ParkingLot parkinglot;


    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private List<Vehicle> ListVehicle;

    public Area() {
        super();
    }



    public Area(String areaName, int capacity, int numberOfVehicle,  ParkingLot parkinglot, List<Vehicle> listVehicle) {
        super();
        this.areaName = areaName;
        this.capacity = capacity;
        this.parkinglot = parkinglot;
        this.numberOfVehicle = numberOfVehicle;
        ListVehicle = listVehicle;
    }

    public String getAreaName() {
        return areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public int getNumberOfVehicle() {
        return numberOfVehicle;
    }
    public void setNumberOfVehicle(int vacancy) {
        this.numberOfVehicle = vacancy;
    }
    public ParkingLot getParkinglot() {
        return parkinglot;
    }
    public void setParkinglot(ParkingLot parkinglot) {
        this.parkinglot = parkinglot;
    }

    public List<Vehicle> getListVehicle() {
        return ListVehicle;
    }
    public void setListVehicle(List<Vehicle> listVehicle) {
        ListVehicle = listVehicle;
    }


}

