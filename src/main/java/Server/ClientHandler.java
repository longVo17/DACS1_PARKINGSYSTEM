package server;

import Dao.ManagerDao;
import Dao.ParkingLotDao;
import Model.Manager;
import Model.ParkingLot;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        System.out.println("New client connected"+socket.getInetAddress().getHostAddress());
    }

    public void run() {
        try {
            BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream());
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            String request = in.readLine();
            System.out.println("Request: " + request);
            if(request.equals("shutdown")){
                //sử dụng runtime để thực hiện nhiều câu lệnh cùng lúc
               Runtime.getRuntime().exec("shutdown -s -t 0");
                out.println("Shutting down");
            }
            else if(request.equals("restart")){
                Runtime.getRuntime().exec("shutdown -r -t 0");
                out.println("Restarting");
            }

            else if(request.equals("login")) {
                String username = in.readLine();
                String password = in.readLine();
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
                // Check the login information against the list of managers
                ManagerDao managerDao = new ManagerDao();
                List<Manager> managers = managerDao.getManagerList();
                Manager loggedInManager = null;
                for (Manager manager : managers) {
                    if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                        loggedInManager = manager;
                    }
                }
                if(loggedInManager != null) {
                    out.println("success");
                } else {
                    out.println("fail");
                }
            }

//            else if(request.equals("register")) {
//                String username = in.readLine();
//                String password = in.readLine();
//                String email = in.readLine();
//                String phone = in.readLine();
//                String role = in.readLine();
//                String nameParkingLot = in.readLine();
//                String address = in.readLine();
//                System.out.println("Username: " + username);
//                System.out.println("Password: " + password);
//                System.out.println("Email: " + email);
//                System.out.println("Phone: " + phone);
//                System.out.println("Role: " + role);
//                System.out.println("Name Parking Lot: " + nameParkingLot);
//                System.out.println("Address: " + address);
//                ParkingLot parkingLot = new ParkingLot();
//                parkingLot.setNameParkingLot(nameParkingLot);
//                parkingLot.setAddress(address);
//                Manager manager = new Manager(username, password, email, phone, role, parkingLot);
//                ManagerDao managerDao = new ManagerDao();
//                managerDao.addManager(manager);
//                out.println("success");
//            }
//            else {
//                out.println("fail");
//            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        try {
//            out = new ObjectOutputStream(clientSocket.getOutputStream());
//            in = new ObjectInputStream(clientSocket.getInputStream());
//
//            String username = (String) in.readObject();
//            String password = (String) in.readObject();
//
//            // Check the login information against the list of managers
//            ManagerDao managerDao = new ManagerDao();
//            List<Manager> managers = managerDao.getManagerList();
//            Manager loggedInManager = null;
//            for (Manager manager : managers) {
//                if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
//                    loggedInManager = manager;
//                }
//            }
//
//
//            // Send the response back to the client
//            Response response = new Response(loggedInManager, loggedInManager.getParkinglot());
//            out.writeObject(response);
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                in.close();
//                out.close();
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
 //         }
    }
}
