package Dao;

import Model.Area;
import Model.ParkingLot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class test_updateNumberOfVehicle {
    public static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            //cập nhật lại số xe trong bãi cho Area trong parknig 1
            ParkingLotDao parkingLotDao = new ParkingLotDao();
            List<ParkingLot> listPk = parkingLotDao.getParkingLotList();
            ParkingLot pk1 = listPk.get(0);
            ParkingLot pk2 = listPk.get(1);

            List<Area> listArea = pk1.getListArea();
            Area areaCar = listArea.get(0);
            areaCar.setNumberOfVehicle(listArea.get(0).getListVehicle().size());
            Area areaMotorbike = listArea.get(1);
            areaMotorbike.setNumberOfVehicle(listArea.get(1).getListVehicle().size());

            session.merge(areaCar);
            session.merge(areaMotorbike);
            session.saveOrUpdate(pk1);
            session.saveOrUpdate(pk2);

            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
