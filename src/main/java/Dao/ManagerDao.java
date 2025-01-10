package Dao;

import Model.Manager;
import Model.ParkingLot;
import Model.Vehicle;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ManagerDao {
    //chọn tất cả tk
    public ManagerDao() {
        super();
    }

    public static void main(String[] args) {
        ManagerDao managerDao = new ManagerDao();
        ParkingLotDao parkingLotDao = new ParkingLotDao();
        List<ParkingLot> ListParkingLot = parkingLotDao.getParkingLotList();
        //public Manager(String username, String password, String email, String phone, String id_card, String role, ParkingLot parkinglot)
        Manager manager1 = new Manager("Long Vo", "thanhlong10", "vothanhlong1704@gmail.com", "0888390733", "admin", ListParkingLot.get(0));
        Manager mangager2 = new Manager("Anh Nguyen", "quynhanh10", "nguyenhuuquynhanh2@gmail.com", "0123456789", "admin", ListParkingLot.get(1));
        managerDao.insertManager(manager1);
        managerDao.insertManager(mangager2);
        List<Manager> managerList = managerDao.getManagerList();
        for (Manager manager : managerList) {
            System.out.println(manager.getUsername());
        }
    }

    //lay danh sach vehicle
    public List<Manager> getManagerList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Manager> managerList = null;
        try {
            String hql = "select m from Manager m";
            managerList = session.createQuery(hql).list();
            System.out.println(managerList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return managerList;
    }

    public void insertManager(Manager manager) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(manager);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }


    //delete
    public void deleteManager(Manager manager) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(manager);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    //update
    public void updateManager(Manager manager) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(manager);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    //find by username
    public Manager findManagerByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Manager manager = null;
        try {
            manager = (Manager) session.get(Manager.class, username);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return manager;
    }
    //find by email
    public Manager findManagerByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Manager manager = null;
        try {
            manager = (Manager) session.get(Manager.class, email);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return manager;
    }

    public List<Manager> findManagerByUsernameAndEmail(String username, String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Manager> managerList = null;
        try {
            String hql = "select m from Manager m where 1=1";
            if (username != null && !username.isEmpty()) {
                hql += " and m.username like :username";
            }
            if (email != null && !email.isEmpty()) {
                hql += " and m.email like :email";
            }
            Query query = session.createQuery(hql);
            if (username != null && !username.isEmpty()) {
                query.setParameter("username", "%" + username + "%");
            }
            if (email != null && !email.isEmpty()) {
                query.setParameter("email", "%" + email + "%");
            }
            managerList = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return managerList;
    }


    public Manager getManagerByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Manager manager = null;
        try {
            String hql = "select m from Manager m where m.email = :email";
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            manager = (Manager) query.uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return manager;
    }
}