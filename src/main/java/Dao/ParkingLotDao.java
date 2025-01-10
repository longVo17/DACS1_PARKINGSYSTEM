package Dao;

import Model.ParkingLot;
import org.apache.log4j.chainsaw.Main;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotDao {
    //lấy danh sách parkinglot
    public ParkingLotDao() {
        super();
    }



    //lấy danh sách parkinglot
    public List<ParkingLot> getParkingLotList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ParkingLot> parkingLotList = null;
        try {
            String hql = "select p from ParkingLot p";
            parkingLotList = session.createQuery(hql).list();
            System.out.println(parkingLotList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return parkingLotList;
    }

    //thêm parkinglot
//    public void addParkingLot(ParkingLot parkingLot) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        try {
//            session.beginTransaction();
//            session.save(parkingLot);
//            session.getTransaction().commit();
//        } catch (HibernateException e) {
//            session.getTransaction().rollback();
//            e.printStackTrace();
//        }
//    }
//    //cập nhật parkinglot
    public void updateParkingLot(ParkingLot parkingLot) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(parkingLot);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }


    public ParkingLot getParkingLotByName(List<ParkingLot> parkingLotList, String nameParkingLot) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ParkingLot parkingLot = null;
        try {
            for (ParkingLot p : parkingLotList) {
                if (p.getNameParkingLot().equals(nameParkingLot)) {
                    parkingLot = p;
                    break;
                }
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return parkingLot;
    }

    public ParkingLot findParkingLotByName(String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ParkingLot parkingLot = null;
        try {
            String hql = "select p from ParkingLot p where p.nameParkingLot = :nameParkingLot";
            parkingLot = (ParkingLot) session.createQuery(hql).setParameter("nameParkingLot", text).uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return parkingLot;
    }
}
