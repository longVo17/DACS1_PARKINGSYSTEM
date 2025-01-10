package Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity
public class Vehicle {
    @Id
    private String id_ticket;

    @ManyToOne
    @JoinColumn(name = "areaName", nullable = false, referencedColumnName = "areaName")
    private Area area;

    private String manager;

    private String vehicleName;
    private String numberPlates;
    private Date dateReceipt;
    private Date dateReturn;
    private String typeOfTicket;
    private String idCart;
    private String nameOfOwner;
    private String phoneNumber;
    private Date dateExpired;
    private boolean isReturned;
    private String fee;
    private String penalty;
    private String totalFee;

    public Vehicle() {
        super();

    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Vehicle(Area area, String vehicleName, String numberPlates, Date dateReceipt, Date DateExpired,
                   Date dateReturn, Boolean isReturned,
                   String typeOfTicket, String nameOfOwner, String phoneNumber,
                   String manager, String idCart, String fee, String penalty,String totalFee) {
        super();
        this.area = area;
        this.vehicleName = vehicleName;
        this.numberPlates = numberPlates;
        this.dateReceipt = dateReceipt;
        this.dateReturn = dateReturn;
        this.typeOfTicket = typeOfTicket;
        this.nameOfOwner = nameOfOwner;
        this.phoneNumber = phoneNumber;
        this.manager = manager;
        this.idCart = idCart;
        this.dateExpired = DateExpired;
        this.isReturned = isReturned;
        this.fee = fee;
        this.penalty = penalty;
        this.totalFee = totalFee;

    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }
// Other methods...

    public String getId_ticket() {
        return id_ticket;
    }
    public void setId_ticket(String id_ticket) {
        this.id_ticket = id_ticket;
    }

    public Date getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public Area getArea() {
        return area;
    }
    public void setArea(Area area) {
        this.area = area;
    }
    public String getVehicleName() {
        return vehicleName;
    }
    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getNumberPlates() {
        return numberPlates;
    }
    public void setNumberPlates(String numberPlates) {
        this.numberPlates = numberPlates;
    }
    public Date getDateReceipt() {
        return dateReceipt;
    }
    public void setDateReceipt(Date dateReceipt) {
        this.dateReceipt = dateReceipt;
    }

    public Date getDateReturn() {
        return dateReturn;
    }
    public void setDateReturn(Date dateReturn) {
        this.dateReturn = dateReturn;
    }
    public String getTypeOfTicket() {
        return typeOfTicket;
    }
    public void setTypeOfTicket(String typeOfTicket) {
        this.typeOfTicket = typeOfTicket;
    }
    public String getNameOfOwner() {
        return nameOfOwner;
    }
    public void setNameOfOwner(String nameOfOwner) {
        this.nameOfOwner = nameOfOwner;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
}
