package Dao;

import Model.Area;
import Model.Manager;
import Model.ParkingLot;
import Model.Vehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class test_dao {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        if (sessionFactory != null) {
            try {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();

                List<Manager>dsnv = new ArrayList<>();
                List<Area>dskv = new ArrayList<>();
                List<Vehicle>ds_car = new ArrayList<>();
                List<Vehicle>ds_motorbike = new ArrayList<>();

                ParkingLot pk1 = new ParkingLot("Parking Lot 1","35 Dong Da - Hai Chau district - Da Nang city", dsnv, dskv);
                ParkingLot pk2 = new ParkingLot("Parking Lot 2","200 Tran Phu - Vinh Phuoc - Hue city", dsnv, dskv);


                Area areaCar = new Area("Car area", 50, 0, pk1, ds_car);
                Area areaMotorbike = new Area("Motorbike area", 150,0, pk1, ds_motorbike);


//     public Vehicle(Area area, String vehicleName, String numberPlates, Date dateReceipt,Date DateExpired,
//                   Date dateReturn, Boolean isReturned,  String typeOfTicket, String nameOfOwner, String phoneNumber, String manager, String idCart) {

                 Vehicle v1 = new Vehicle(areaCar, "Car", "43A12345", new Date(), new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000), null,false,  "Daily Ticket", null, null,"Long Vo",null,null,null,null);

                Vehicle v2 = new Vehicle(areaCar, "Motorbike", "75A23456", new Date(), new Date(), null,false,  "Daily Ticket", null, null,"Long Vo",null, null,null,null );
                Vehicle v3 = new Vehicle(areaCar, "Car", "75A34567", new Date(), new Date(), null,false,  "Daily Ticket", null, null,"Long Vo",null, null, null,null);


                Calendar calendar = Calendar.getInstance(); // lấy thể hiện hiện tại của Calendar
                calendar.setTime(new Date()); // đặt thời gian của Calendar là ngày hiện tại
                calendar.add(Calendar.MONTH, 1); // thêm 1 tháng vào Calendar
                Date oneMonthLater = calendar.getTime(); // lấy Date từ Calendar

                Vehicle v4 = new Vehicle(areaMotorbike, "Motorbike", "75A123456", new Date(),new Date(System.currentTimeMillis()+24*60*60*1000) ,new Date(System.currentTimeMillis()+24*60*60*1000*4),true,  "Daily Ticket", "Quynh Anh", "0123456789","Long Vo","048235234457", null, null,null);
                Vehicle v5 = new Vehicle(areaCar, "Car", "76B123456", new Date(),new Date(System.currentTimeMillis()+24*60*60*1000) ,new Date(System.currentTimeMillis()+24*60*60*1000*2),true,  "Daily Ticket", null,null ,"Quanh",null, null, null,null);
                Vehicle v6 = new Vehicle(areaCar, "Car", "43C123457", new Date(System.currentTimeMillis()-24*60*60*1000*4),new Date(System.currentTimeMillis()-24*60*60*1000*3) ,new Date(System.currentTimeMillis()+24*60*60*1000*1),true,  "Daily Ticket", null,null ,"Quanh",null, null, null,null);
                Vehicle v7 = new Vehicle(areaCar, "Car", "76B123458", new Date(System.currentTimeMillis()-24*60*60*1000*4),new Date(System.currentTimeMillis()-24*60*60*1000*3) ,new Date(System.currentTimeMillis()-24*60*60*1000*3),true,  "Daily Ticket", null,null ,"Quanh",null, null, null,null);
                        Vehicle v8 = new Vehicle(areaCar, "Motorbike", "92B123459", new Date(System.currentTimeMillis()-24*60*60*1000*3),new Date(System.currentTimeMillis()-24*60*60*1000*2) ,new Date(System.currentTimeMillis()-24*60*60*1000),true,  "Daily Ticket", null,null ,"Quanh",null, null, null,null);
                        Vehicle v9 = new Vehicle(areaCar, "Motorbike", "92B123460", new Date(System.currentTimeMillis()-24*60*60*1000*3),new Date(System.currentTimeMillis()-24*60*60*1000*2) ,new Date(System.currentTimeMillis()-24*60*60*1000*2),true,  "Daily Ticket", null,null ,"Quanh",null, null, null,null);
                        Vehicle v10 = new Vehicle(areaMotorbike, "Bicycle", "0", new Date(System.currentTimeMillis()-24*60*60*1000*5),new Date(System.currentTimeMillis()-24*60*60*1000*4) ,new Date(System.currentTimeMillis()-24*60*60*1000*3),true,  "Daily Ticket", null,null ,"Quanh",null, null, null,null);

                        v5.setId_ticket("DT10");
                        v6.setId_ticket("DT11");
                        v7.setId_ticket("DT12");
                        v8.setId_ticket("DT57");
                        v9.setId_ticket("DT58");
                        v10.setId_ticket("DT59");

                    v1.setId_ticket("DT1");
                    v2.setId_ticket("DT51");
                    v3.setId_ticket("DT2");
                    v4.setId_ticket("DT52");

                // ds_car.add(v1);
                dskv.add(areaCar);
                dskv.add(areaMotorbike);



                //public Manager(String username, String password, String name, String phone, String id_card, String role)
                Manager mng1 = new Manager("Long Vo", "thanhlong10", "vothanhlong1704@gmail.com", "0987654321", "admin", pk1);
                Manager mng2 = new Manager("Quanh", "quynhanh10", "nguyenhuuquynhanh@gmail.com", "0987654321", "admin", pk1);

                ds_motorbike.add(v8);
                ds_motorbike.add(v9);
                ds_motorbike.add(v2);
                ds_motorbike.add(v4);
                ds_motorbike.add(v10);
                ds_car.add(v5);
                ds_car.add(v6);
                ds_car.add(v7);
                ds_car.add(v3);
                ds_car.add(v1);
                dsnv.add(mng1);
                dsnv.add(mng2);

                session.saveOrUpdate(v10);
                session.saveOrUpdate(v5);
                session.saveOrUpdate(v6);
                session.saveOrUpdate(v7);
                session.saveOrUpdate(v8);
                session.saveOrUpdate(v9);

                session.saveOrUpdate(v4);
               session.saveOrUpdate(v1);
                session.saveOrUpdate(v2);
                session.saveOrUpdate(v3);
                session.saveOrUpdate(pk1);
                session.saveOrUpdate(areaCar);
                session.saveOrUpdate(areaMotorbike);
                session.saveOrUpdate(mng1);
                session.saveOrUpdate(mng2);
                session.saveOrUpdate(pk1);
                session.saveOrUpdate(pk2);
                session.getTransaction().commit();
                session.close();




//                customer customer1 = new customer("Võ Thành Long", "vothanhlong@gmail.com");
//                order order1 = new order("35 Đống Đa", customer1, "OD01",customer1.getName());
//                order order2 = new order("78 Đỗ Quang", customer1, "OD02",customer1.getName());
//                order order3 = new order("12 Lê Lợi", customer1, "OD03",customer1.getName());
//
//                List<order> list = new ArrayList<>();
//                list.add(order1);
//                list.add(order2);
//                list.add(order3);
//
//                customer1.setOderList(list);
//                session.save(order1);
//                session.save(order2);
//                session.save(order3);
//                session.save(customer1);
//                    List<order> list=session.createQuery("from order").list();
//
//                    session.getTransaction().commit();
//                    session.close();
//
//                    for(order o : list){
//                        System.out.println(o.toString());
//                    }
            } catch (Exception e) {


                e.printStackTrace();
            }

        }
    }
}


