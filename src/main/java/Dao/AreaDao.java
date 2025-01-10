package Dao;

import Model.Area;
import Model.ParkingLot;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AreaDao {
    public AreaDao() {
        super();
    }
    //lay danh sach area
    public List<Area> getAreaList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Area> areaList = null;
        try {
            String hql = "select a from Area a";
            areaList = session.createQuery(hql).list();
            System.out.println(areaList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return areaList;
    }
    // them area
    public void addArea(Area area) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(area);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }
    // cap nhat area
    public void updateArea(Area area) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(area); // Sử dụng merge thay vì update
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    // xoa area
    public void deleteArea(Area area) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.delete(area);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public List<Area> getAreaByParkingLot(ParkingLot parkingLot) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Area> areaList = null;
        try {
            String hql = "select a from Area a where a.parkinglot = :parkingLot";
            areaList = session.createQuery(hql).setParameter("parkingLot", parkingLot).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return areaList;
    }
}
