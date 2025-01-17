package Dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        }catch (Exception e){
            System.err.println("Initial SessionFactory creation failed." + e);
            return null;
        }
    }
    //tạo hàm getSessionFactory để trả về sessionFactory
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    //tạo hàm shutdown để đóng sessionFactory
    public static void shutdown(){
        getSessionFactory().close();
    }
}
