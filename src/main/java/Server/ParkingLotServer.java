package server;

import Dao.ManagerDao;
import Dao.ParkingLotDao;
import Model.Manager;
import Model.ParkingLot;
import org.LoginForm.LoginRun;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotServer {
    private static final int PORT = 8080;
    private List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected" + socket.getInetAddress().getHostAddress());

                // Tạo một đối tượng ClientHandler mới
                ClientHandler clientHandler = new ClientHandler(socket);

                // Tạo và khởi động một luồng mới với ClientHandler
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        clientHandler.run();
                    }
                });
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

