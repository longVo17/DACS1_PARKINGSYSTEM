package Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Manager {

    @Id
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    @ManyToOne
    @JoinColumn(name = "nameParkingLot")
    private ParkingLot parkinglot;



    public Manager() {
    }
    public Manager(String username, String password, String email, String phone, String role, ParkingLot parkinglot) {
        this.username = username;
        this.password = password;

        this.email = email;
        this.phone = phone;
        this.role = role;
        this.parkinglot = parkinglot;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public ParkingLot getParkinglot() {
        return parkinglot;
    }

    public void setParkinglot(ParkingLot parkingLotName) {
        this.parkinglot = parkingLotName;
    }

    public ParkingLot getParkingLot() {
        return parkinglot;
    }


    public void setParkingLot(ParkingLot currentParkingLot) {
        this.parkinglot = currentParkingLot;
    }

    public void setParkingLotName(String text) {
        this.parkinglot.setNameParkingLot(text);
    }
}
