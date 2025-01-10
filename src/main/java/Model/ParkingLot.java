package Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ParkingLot {
    @Id
    private String nameParkingLot;
    private String address;

    @OneToMany(mappedBy = "parkinglot")
    private List<Manager> ListManager;

    @OneToMany(mappedBy = "parkinglot")
    private List<Area> ListArea;


    public List<Manager> getListManager() {
        return ListManager;
    }


    public String getNameParkingLot() {
        return nameParkingLot;
    }

    public void setNameParkingLot(String nameParkingLot) {
        this.nameParkingLot = nameParkingLot;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setListManage(List<Manager> ListManager) {
        this.ListManager = ListManager;
    }

    public void setListManager(List<Manager> listManager) {
        ListManager = listManager;
    }

    public List<Area> getListArea() {
        return ListArea;
    }
    public void setListArea(List<Area> listArea) {
        ListArea = listArea;
    }

    public ParkingLot() {
    }
    public ParkingLot( String nameParkingLot, String address, List<Manager> ListManager, List<Area> ListArea) {
        this.ListManager=ListManager;
        this.address = address;
        this.nameParkingLot = nameParkingLot;
        this.ListArea = ListArea;

    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "nameParkingLot='" + nameParkingLot + '\'' +
                ", address='" + address + '\'' +
                ", ListManager=" + ListManager +
                ", ListArea=" + ListArea +
                '}';
    }

    public List<Manager> getManagerList() {
        return ListManager;

    }

    public int getTotalSlot() {
        int totalSlot = 0;
        for (Area area : ListArea) {
            totalSlot += area.getCapacity();
        }
        return totalSlot;
    }


        public int getCurrentMotorbikeArea() {
            int totalMotorbikeArea = 0;
            for (Area area : ListArea) {
                if (area.getAreaName().equals("Motorbike")) {
                    totalMotorbikeArea += area.getListVehicle().size();
                }
            }
            return totalMotorbikeArea;
        }

        public int getCurrentCarArea() {
            int totalCarArea = 0;
            for (Area area : ListArea) {
                if (area.getAreaName().equals("Car")) {
                    totalCarArea += area.getListVehicle().size();
                }
            }
            return totalCarArea;
        }
    }








