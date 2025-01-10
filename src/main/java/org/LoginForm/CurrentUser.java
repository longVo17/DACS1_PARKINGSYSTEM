package org.LoginForm;

import Model.Manager;
import Model.ParkingLot;

public class CurrentUser {
    private static CurrentUser instance;
    private Manager loggedInManager;
    private ParkingLot loggedInParkingLot;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public Manager getLoggedInManager() {
        return loggedInManager;
    }

    public void setLoggedInManager(Manager loggedInManager) {
        this.loggedInManager = loggedInManager;
    }

    public ParkingLot getLoggedInParkingLot() {
        return loggedInParkingLot;
    }

    public void setLoggedInParkingLot(ParkingLot loggedInParkingLot) {
        this.loggedInParkingLot = loggedInParkingLot;
    }


}
