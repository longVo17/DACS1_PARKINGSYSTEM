package Dao;

import Model.Area;
import Model.Manager;
import Model.ParkingLot;
import Model.Vehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class test_vehicleDao {
    public static void main(String[] args) {
        ManagerDao managerDao = new ManagerDao();
        List<Manager>listManagers = managerDao.getManagerList();
        for(Manager manager : listManagers) {
            System.out.println(manager.getUsername());
        }

        }

    }

