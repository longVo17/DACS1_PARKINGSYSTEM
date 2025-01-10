package Dao;

import Model.Vehicle;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleDao {
    public VehicleDao() {
        super();
    }


    //lay danh sach vehicle
    public List<Vehicle> getVehicleList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v";
            vehicleList = session.createQuery(hql).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }
    //lấy danh sách xe còn trong bãi ( chưa trả)
    public List<Vehicle> getVehicleListNotReturned() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v where v.isReturned = false";
            vehicleList = session.createQuery(hql).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }
    //lấy danh sách xe đã trả rồi
    public List<Vehicle> getVehicleListReturned() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v where v.isReturned = true";
            vehicleList = session.createQuery(hql).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    // get vehicles with id containing 'DT' and not returned
    public List<Vehicle> getVehicleListByDT() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v where v.id_ticket like '%DT%' and v.isReturned = false";
            vehicleList = session.createQuery(hql).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    // get vehicles with id containing 'MT' and not returned
    public List<Vehicle> getVehicleListByMT() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v where v.id_ticket like '%MT%' and v.isReturned = false";
            vehicleList = session.createQuery(hql).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    public void addVehicle(Vehicle vehicle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(vehicle);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(vehicle);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteVehicle(Vehicle vehicle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(vehicle);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }


    public List<Vehicle> searchVehicleById_and_NumberPlates(String id_ticket, String numberPlates) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            if (id_ticket == null && numberPlates == null) {
                return getVehicleList();
            } else if (id_ticket == null) {
                String hql = "select v from Vehicle v where v.numberPlates like :numberPlates";
                vehicleList = session.createQuery(hql).setParameter("numberPlates", numberPlates).list();
                System.out.println(vehicleList.size());
            } else if (numberPlates == null) {
                String hql = "select v from Vehicle v where v.id_ticket like :id_ticket";
                vehicleList = session.createQuery(hql).setParameter("id_ticket", id_ticket).list();
                System.out.println(vehicleList.size());
            }
            String hql = "select v from Vehicle v where v.id_ticket like :id_ticket and v.numberPlates like :numberPlates";
            vehicleList = session.createQuery(hql).setParameter("id_ticket", id_ticket).setParameter("numberPlates", numberPlates).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    public List<Vehicle> searchVehicleByNumberPlates(String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v where v.numberPlates like :text";
            vehicleList = session.createQuery(hql).setParameter("text", text).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    public List<Vehicle> searchVehicleById(String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v where v.id_ticket like :text";
            vehicleList = session.createQuery(hql).setParameter("text", text).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    //search by phone
    public List<Vehicle> searchVehicleByPhone(String phone) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v where v.phoneNumber like :phone";
            vehicleList = session.createQuery(hql).setParameter("phone", phone).list();
            System.out.println(vehicleList.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }



    //chọn bằng ngày nhận ngày trả
    public List<Vehicle> getReturnedVehicles(Date fromDate, Date toDate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicles = null;
        try {
            session.beginTransaction();
            Query<Vehicle> query = session.createQuery("from Vehicle where isReturned = true and dateReturn between :fromDate and :toDate", Vehicle.class);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            vehicles = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return vehicles;
    }


    public List<Vehicle> searchVehicleByAll(String id_ticket, String numberPlates, String nameOfOwner) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vehicle> vehicleList = null;
        try {
            String hql = "select v from Vehicle v where 1=1";
            if (id_ticket != null) {
                hql += " and v.id_ticket like :id_ticket";
            }
            if (numberPlates != null) {
                hql += " and v.numberPlates like :numberPlates";
            }
            if (nameOfOwner != null) {
                hql += " and v.nameOfOwner like :nameOfOwner";
            }
            Query query = session.createQuery(hql);
            if (id_ticket != null) {
                query.setParameter("id_ticket", "%" + id_ticket + "%");
            }
            if (numberPlates != null) {
                query.setParameter("numberPlates", "%" + numberPlates + "%");
            }
            if (nameOfOwner != null) {
                query.setParameter("nameOfOwner", "%" + nameOfOwner + "%");
            }
            vehicleList = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return vehicleList;
    }


    public Vehicle getVehicleById(String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Vehicle vehicle = null;
        try {
            String hql = "select v from Vehicle v where v.id_ticket like :text";
            vehicle = (Vehicle) session.createQuery(hql).setParameter("text", text).uniqueResult();
            System.out.println(vehicle);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return vehicle;
    }



    public int getVehicleType(List<Vehicle> returnedVehicles, String car) {
        return returnedVehicles.stream().filter(vehicle -> vehicle.getVehicleName().equals(car)).collect(Collectors.toList()).size();
    }
}




//


