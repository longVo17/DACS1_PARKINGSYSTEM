package org.ParkingLot;

import Dao.HibernateUtil;
import Dao.VehicleDao;
import Model.Area;
import Model.Manager;
import Model.ParkingLot;
import Model.Vehicle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.DailyTicket.DailyTicketController;
import org.LoginForm.CurrentUser;
import org.LoginForm.LoginController;
import org.MainMenu.MainController;
import org.MenuParkingLot.MenuParkingController;
import org.MonthlyTicket.MonthlyTicketController;
import org.Statistics.StatisticsController;
import org.Statistics.Statistics_chartController;
import org.UserAccount.UserAccountController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingLotController {


        private Stage stage;
        private Scene scene;
        private Parent root;
        @FXML
        private ComboBox boxType;
        @FXML
        private AnchorPane slider;
        @FXML
        private Button btnMenu;
        @FXML
        private Button closeMenu;
        @FXML
        private Pane overlay;
        @FXML
        private AnchorPane sliderAccount;
        private boolean isSliderAccountOpen = false;
        @FXML
        private Button btnAccount;
        @FXML
        private Label Username;
        @FXML
        private ComboBox boxTypeVehicle;
    @FXML
    private ImageView imgViewChoose;

    @FXML
    private AnchorPane containerImg;;
    @FXML
    private TextField txtNumberPlate;
    //table
    @FXML
    private TableView<Vehicle> tableParkingLot;
    @FXML
    private TableColumn<Vehicle, Integer>colNo;
    @FXML
    private TableColumn<Vehicle, String> colPlates;
    @FXML
    private TableColumn<Vehicle, String> colId;
    @FXML
    private TableColumn<Vehicle, String>colVehicle;
    @FXML
    private TableColumn<Vehicle, Date> colReceipt;
    @FXML
    private TableColumn<Vehicle, Date> colExpired;
@FXML
private TableColumn<Vehicle, String> colOwner;
@FXML
private TableColumn<Vehicle, String> colPhone;
@FXML
private TableColumn<Vehicle, String> colIdCart;
@FXML
private TableColumn<Vehicle, String>colStaff;

@FXML
private Label vacancyCar;
@FXML
private Label vacancyMoto;
@FXML
private Label txtRole;
@FXML
private Label txtEmail;
@FXML
private Label txtPhone1;
@FXML
private Label lblParkingLot;

//thông tin
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtOwner;
    @FXML
    private TextField txtIdCart;
    @FXML
    private TextField txtPlates;
    @FXML
    private TextField txtReceipt;
    @FXML
    private TextField txtExpired;


private Manager currentManager;
    private ParkingLot currentParkingLot;


    public void initialize() {
            boxType.getItems().addAll("Car", "Motorbike", "Bicycle");
            boxType.getSelectionModel().selectFirst(); // Chọn phần tử đầu tiên làm mặc định
            // Các phần khác của phương thức initialize()

            // Đặt vị trí ban đầu của slider (menu trượt) để nó mặc định ở trạng thái đóng
            slider.setTranslateX(-300);

            btnMenu.setOnAction(event -> openMenu(event));
            closeMenu.setOnAction(event -> closeMenu(event));



            boxTypeVehicle.getItems().addAll("All Vehicle","Daily Ticket", "Monthly Ticket");




            sliderAccount.setVisible(false);

            //table
            //table
            VehicleDao vehicleDao = new VehicleDao();
            List<Vehicle> vehicleList = vehicleDao.getVehicleListNotReturned();
            ObservableList<Vehicle> listVehicle = FXCollections.observableArrayList(vehicleList);
            setCellValueFromTable();
            tableParkingLot.setItems(listVehicle);

            CurrentUser currentUser = CurrentUser.getInstance();
            Manager currentManager = currentUser.getLoggedInManager();
            currentParkingLot = currentUser.getLoggedInParkingLot();

            System.out.println("current parking lot: " + currentParkingLot.getNameParkingLot());

            updateVehicleCountInParkingLot(currentParkingLot);

            lblParkingLot.setText(currentParkingLot.getNameParkingLot().toUpperCase());
            txtRole.setText(currentManager.getRole());
            txtEmail.setText(currentManager.getEmail());
            txtPhone1.setText(currentManager.getPhone());
            Username.setText(currentManager.getUsername());


        searchOwnerName.setText(null);
        searchPlate.setText(null);
        searchId.setText(null);


        }

    private void setCellValueFromTable() {
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1
        ));
        colId.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("id_ticket"));
        colPlates.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("numberPlates"));
        colVehicle.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleName"));
        colReceipt.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("dateReceipt"));
        colExpired.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("DateExpired"));
        colIdCart.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("idCart"));
        colOwner.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("nameOfOwner"));
        colPhone.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("phoneNumber"));
        colStaff.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("Manager"));

        //lấy dữ liệu từ bảng khi nhấn vào
        tableParkingLot.setOnMouseClicked(event -> {
            Vehicle vehicle = tableParkingLot.getSelectionModel().getSelectedItem();
            if (vehicle != null) {
                txtPlates.setText(vehicle.getNumberPlates());
                boxType.getSelectionModel().select(vehicle.getVehicleName());
                txtId.setText(vehicle.getId_ticket());
                txtPhone.setText(vehicle.getPhoneNumber());
                txtOwner.setText(vehicle.getNameOfOwner());
                txtIdCart.setText(vehicle.getIdCart());
                txtPlates.setText(vehicle.getNumberPlates());
                txtReceipt.setText(vehicle.getDateReceipt().toString());
                txtExpired.setText(vehicle.getDateExpired().toString());

            }
        });
        // lấy dữ liệu từ bảng khi dùng dấu mũi tên
        tableParkingLot.setOnKeyPressed(event -> {
            Vehicle vehicle = tableParkingLot.getSelectionModel().getSelectedItem();
            if (vehicle != null) {
                txtPlates.setText(vehicle.getNumberPlates());
                boxType.getSelectionModel().select(vehicle.getVehicleName());
                txtId.setText(vehicle.getId_ticket());
                txtPhone.setText(vehicle.getPhoneNumber());
                txtOwner.setText(vehicle.getNameOfOwner());
                txtIdCart.setText(vehicle.getIdCart());
                txtPlates.setText(vehicle.getNumberPlates());
                txtReceipt.setText(vehicle.getDateReceipt().toString());
                txtExpired.setText(vehicle.getDateExpired().toString());
            }
        });
    }

    //NẾU boxVehile chọn All vehicle  thì lấy hết còn Daily Ticket thì lấy Danh sách vé ngàh
    @FXML
    public void filterVehicle(javafx.event.ActionEvent e) {
        VehicleDao vehicleDao = new VehicleDao();
        List<Vehicle> vehicleList = null;
        if (boxTypeVehicle.getValue().equals("All Vehicle")) {
            vehicleList = vehicleDao.getVehicleListNotReturned();
        } else if (boxTypeVehicle.getValue().equals("Daily Ticket")) {
            vehicleList = vehicleDao.getVehicleListByDT();
        } else if (boxTypeVehicle.getValue().equals("Monthly Ticket")) {
            vehicleList = vehicleDao.getVehicleListByMT();
        }
        ObservableList<Vehicle> listVehicle = FXCollections.observableArrayList(vehicleList);
        for(Vehicle v : vehicleList){
            System.out.println(v.getId_ticket());
        }
        tableParkingLot.setItems(listVehicle);
    }

    public void updateVehicleCountInParkingLot(ParkingLot parkingLot) {
        int count_car = 0;
        int count_moto = 0;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            List<Area> listArea = parkingLot.getListArea();
            for(Area area : listArea){
                System.out.println("Area name: " + area.getAreaName() + " number of vehicle: " + area.getNumberOfVehicle());
            }
            List<Vehicle>listVehicleDao = new VehicleDao().getVehicleList();
            for (Vehicle v : listVehicleDao) {
                if (v.isReturned() == false) {
                    if (v.getArea().getAreaName().equals("Car area")) {
                        count_car++;
                    } else if (v.getArea().getAreaName().equals("Motorbike area")) {
                        count_moto++;
                    }
                }
            }
            listArea.get(0).setNumberOfVehicle(count_car);
            listArea.get(1).setNumberOfVehicle(count_moto);

            session.merge(listArea.get(0));
            session.merge(listArea.get(1));
            session.getTransaction().commit();

            vacancyCar.setText(parkingLot.getListArea().get(0).getNumberOfVehicle() + " / " + parkingLot.getListArea().get(0).getCapacity());
            vacancyMoto.setText(parkingLot.getListArea().get(1).getNumberOfVehicle() + " / " + parkingLot.getListArea().get(1).getCapacity());

        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    public void UpdateVehicle(javafx.event.ActionEvent e) {
        VehicleDao vehicleDao = new VehicleDao();
        Vehicle vehicle = new Vehicle();
        vehicle.setId_ticket(txtId.getText());
        vehicle.setNumberPlates(txtPlates.getText());
        vehicle.setVehicleName(boxType.getValue().toString());
        vehicle.setDateReceipt(new Date());
        //date return là 1 ngày sau nếu mã vé là DT 1 tháng nếu là MT
        if(vehicle.getId_ticket().contains("DT")){
            vehicle.setDateReturn(new Date(System.currentTimeMillis() + 86400000));
        }
        else{
            vehicle.setDateReturn(new Date(System.currentTimeMillis() + 2592000000L));
        }
        vehicle.setNameOfOwner(txtOwner.getText());
        vehicle.setPhoneNumber(txtPhone.getText());
        vehicle.setIdCart(txtIdCart.getText());
        vehicle.setManager(Username.getText());
          if(vehicle.getVehicleName().equals("Car")) {
              vehicle.setArea(currentParkingLot.getListArea().get(0));
          }
          else{
                vehicle.setArea(currentParkingLot.getListArea().get(1));
          }
        vehicleDao.updateVehicle(vehicle);
        List<Vehicle> vehicleList = vehicleDao.getVehicleListNotReturned();
        ObservableList<Vehicle> listVehicle = FXCollections.observableArrayList(vehicleList);
        tableParkingLot.setItems(listVehicle);
        updateVehicleCountInParkingLot(currentParkingLot);
    }

    //xóa
    @FXML
    public void ReturnVehicle(javafx.event.ActionEvent e) {
        // Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return Vehicle");
        alert.setHeaderText("Are you sure you want to return this vehicle?");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            // Update vehicle status
            VehicleDao vehicleDao = new VehicleDao();
            Vehicle vehicle = vehicleDao.getVehicleById(txtId.getText());
            if(vehicle != null) {
                vehicle.setReturned(true);
                vehicle.setDateReturn(new Date());
                vehicleDao.updateVehicle(vehicle);

                List<Vehicle> vehicleList = vehicleDao.getVehicleListNotReturned();
                ObservableList<Vehicle> listVehicle = FXCollections.observableArrayList(vehicleList);
                tableParkingLot.setItems(listVehicle);

                updateVehicleCountInParkingLot(currentParkingLot);
                // Clear all text fields
                txtId.setText("");
                txtPlates.setText("");
                txtOwner.setText("");
                txtPhone.setText("");
                txtIdCart.setText("");
                txtReceipt.setText("");
                txtExpired.setText("");
                boxType.getSelectionModel().selectFirst();
            } else {
                // Show error message if vehicle not found
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Vehicle not found");
                errorAlert.setContentText("The vehicle with the given ID could not be found.");
                errorAlert.showAndWait();
            }
        }
    }

    //cancel
    @FXML
    public void Cancel(javafx.event.ActionEvent e) {
        txtId.setText("");
        txtPlates.setText("");
        txtOwner.setText("");
        txtPhone.setText("");
        txtIdCart.setText("");
        txtReceipt.setText("");
        txtExpired.setText("");
    }
    @FXML
    private TextField searchId;
    @FXML
    private TextField searchPlate;
    @FXML
    private TextField searchOwnerName;
    @FXML
    public void Search (javafx.event.ActionEvent e) {
        System.out.println("seachId:"+searchId.getText());
        System.out.println("searchPlate:"+searchPlate.getText());
        System.out.println("searchOwnerName:"+searchOwnerName.getText());


        VehicleDao test = new VehicleDao();
        System.out.println("123");
        List<Vehicle> vehicleList = test.searchVehicleByAll(searchId.getText(), searchPlate.getText(),searchOwnerName.getText());
        //những xe chưa trả
        List<Vehicle>vehicleListReall = new ArrayList<>();
        for(Vehicle v : vehicleList){
            if(!v.isReturned()){
                vehicleListReall.add(v);
            }
        }

        System.out.println("abc");
        for (Vehicle vehicle : vehicleList) {
            System.out.println(vehicle.getId_ticket());
        }
        ObservableList<Vehicle> listVehicle = FXCollections.observableArrayList(vehicleListReall);
        System.out.println("cái khác");
        for(Vehicle v : listVehicle){
            System.out.println(v.getId_ticket());
        }
        tableParkingLot.setItems(listVehicle);

        searchOwnerName.setText(null);
        searchPlate.setText(null);
        searchId.setText(null);
    }

        @FXML
        //search by id
        public void SearchById(javafx.event.ActionEvent e) {
            VehicleDao test = new VehicleDao();
            List<Vehicle> vehicleList = test.searchVehicleById_and_NumberPlates(searchId.getText(),searchPlate.getText());
            ObservableList<Vehicle> listVehicle = FXCollections.observableArrayList(vehicleList);
            tableParkingLot.setItems(listVehicle);
        }



        @FXML
        public void openMenu(javafx.event.ActionEvent actionEvent) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(javafx.util.Duration.seconds(0.5));
            slide.setNode(slider);
            slide.setToX(0); // Đặt vị trí cuối cùng mà nút cần di chuyển đến
            slide.play();

            overlay.setVisible(true); // Hiển thị overlay
        }

        @FXML
        public void closeMenu(javafx.event.ActionEvent actionEvent) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(javafx.util.Duration.seconds(0.5));
            slide.setNode(slider);
            slide.setToX(-300); // Đặt vị trí cuối cùng mà nút cần di chuyển đến
            slide.play();

            overlay.setVisible(false); // Ẩn overlay
        }

    @FXML
    public void toggleAccount(javafx.event.ActionEvent actionEvent) {

//trường hợp đónng
        if (isSliderAccountOpen) {

            // overlay.setVisible(false); // Ẩn overlay
            sliderAccount.setVisible(false);
        } else {

            //  overlay.setVisible(true); // Hiển thị overlay
            sliderAccount.setVisible(true);
        }

        isSliderAccountOpen = !isSliderAccountOpen; // Đảo trạng thái của sliderAccount
    }





    //chuyển scene
        @FXML
        public void switchtoLogin (javafx.event.ActionEvent actionEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/LoginForm/LoginForm.fxml"));
                root= loader.load();
                LoginController controller = loader.getController();
                //Platform.runLater(() -> controller.setUsername(Username.getText()));
                stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/org/LoginForm/LoginForm.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        @FXML
        public void switchtoSceneMenu(ActionEvent actionEvent){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/MainMenu/MainMenu.fxml"));
                root= loader.load();
                MainController controller = loader.getController();
                Platform.runLater(() -> controller.setUsername(Username.getText()));

                stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/org/MainMenu/MainMenu.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @FXML
        public void switchtoSceneParkingMenu (javafx.event.ActionEvent actionEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/MenuParkingLot/MenuParking.fxml"));
                root= loader.load();
                MenuParkingController controller = loader.getController();
                Platform.runLater(() -> controller.setUsername(Username.getText()));

                stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/org/MenuParkingLot/MenuParking.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        public void switchtoSceneDaily(javafx.event.ActionEvent actionEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/DailyTicket/DailyTicket.fxml"));
                root= loader.load();
                DailyTicketController controller = loader.getController();
                Platform.runLater(() -> controller.setUsername(Username.getText()));

                stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/org/DailyTicket/DailyTicket.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    @FXML
    public void switchtoSceneMonthlyTicket (javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/MonthlyTicket/MonthlyTicket.fxml"));
            root= loader.load();
            MonthlyTicketController controller = loader.getController();
            Platform.runLater(() -> controller.setUsername(Username.getText()));

            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/MonthlyTicket/MonthlyTicket.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchtoSceneAccountUser (javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/userAccount/userAccount.fxml"));
            root= loader.load();
            UserAccountController controller = loader.getController();
            Platform.runLater(() -> controller.setUsername(Username.getText()));

            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/userAccount/userAccount.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchtoSceneStatistics (javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Statistics/Statistics.fxml"));
            root= loader.load();
  //          StatisticsController controller = loader.getController();
//            Platform.runLater(() -> controller.setUsername(Username.getText()));

            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/Statistics/Statistics.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



        //đặt username
        public void setUsername(String username) {
            Username.setText(username);
        }
    }

