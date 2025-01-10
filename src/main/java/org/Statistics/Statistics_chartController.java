package org.Statistics;

import Dao.AreaDao;
import Dao.VehicleDao;
import Model.Area;
import Model.ParkingLot;
import Model.Vehicle;
import function.FeeCalculator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.DailyTicket.DailyTicketController;
import org.LoginForm.CurrentUser;
import org.LoginForm.LoginController;
import org.MainMenu.MainController;
import org.MenuParkingLot.MenuParkingController;
import org.MonthlyTicket.MonthlyTicketController;
import org.ParkingLot.ParkingLotController;
import org.UserAccount.UserAccountController;
import Model.Manager;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;

// ...



public class Statistics_chartController {


    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button btnMenu;
    @FXML
    private AnchorPane slider;
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

    //    public Vehicle(String id_ticket, Area area, String vehicleName, String numberPlates, Date dateReceipt,
//                   Date dateReturn, String typeOfTicket, String nameOfOwner, String phoneNumber) {


    @FXML
    private Label txtRole;
    @FXML
    private Label txtEmail;
    @FXML
    private Label txtPhone;
    @FXML
    private Label lblParkingName;
    @FXML
    private Label lblParkingLot;

    @FXML
    private Pane pane_Chart;
    @FXML
    private PieChart pieChart;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;

    private ObservableList<Vehicle> listVehicle;
    private ParkingLot currentParkingLot;

    @FXML
    public void initialize() {
        // Đặt vị trí ban đầu của slider (menu trượt) để nó mặc định ở trạng thái đóng
        slider.setTranslateX(-300);

        btnMenu.setOnAction(event -> openMenu(event));
        closeMenu.setOnAction(event -> closeMenu(event));
        btnAccount.setOnAction(event -> toggleAccount(event));
        sliderAccount.setVisible(false);


        CurrentUser currentUser = CurrentUser.getInstance();
        Manager currentManager = currentUser.getLoggedInManager();
        currentParkingLot = currentUser.getLoggedInParkingLot();

        System.out.println("current parking lot: " + currentParkingLot.getNameParkingLot());

        lblParkingName.setText(currentParkingLot.getNameParkingLot().toUpperCase());
        lblParkingLot.setText(currentParkingLot.getNameParkingLot().toUpperCase());
        txtRole.setText(currentManager.getRole());
        txtEmail.setText(currentManager.getEmail());
        txtPhone.setText(currentManager.getPhone());
        Username.setText(currentManager.getUsername());

        pieChart.setVisible(false);
        barChart.setVisible(false);
        stackedBarChart.setVisible(false);

    }

    @FXML
    public void pieChart(ActionEvent actionEvent) {
        pieChart.setVisible(true);
        barChart.setVisible(false);
        stackedBarChart.setVisible(false);
        // Tạo một ObservableList để lưu trữ dữ liệu cho biểu đồ
        VehicleDao vehicleDao = new VehicleDao();
        List<Vehicle> vehicleList = vehicleDao.getVehicleList();

        // Lấy ngày từ DatePicker
        if (dateFrom.getValue() == null || dateTo.getValue() == null) {
            //lấy tất cả
            this.listVehicle = FXCollections.observableArrayList(vehicleList);

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            int totalVehicles = vehicleList.size();
            System.out.println("test pie chart: totalVehicle:" + totalVehicles);
            Map<String, Integer> vehicleTypeCounts = new HashMap<>();
            for (Vehicle vehicle : vehicleList) {
                vehicleTypeCounts.put(vehicle.getVehicleName(), vehicleTypeCounts.getOrDefault(vehicle.getVehicleName(), 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : vehicleTypeCounts.entrySet()) {
                double percentage = (double) entry.getValue() / totalVehicles * 100;
                pieChartData.add(new PieChart.Data(entry.getKey() + " (" + String.format("%.2f", percentage) + "%)", entry.getValue()));
            }
            pieChart.setData(pieChartData);
            pieChart.setTitle("Vehicle Types in Parking Lot");


        } else {
            Date fromDate = java.sql.Date.valueOf(dateFrom.getValue());
            Date toDate = java.sql.Date.valueOf(dateTo.getValue());

            // Lọc danh sách vehicleList
            List<Vehicle> filteredVehicleList = vehicleList.stream()
                    .filter(vehicle -> vehicle.getDateReceipt().after(fromDate) && vehicle.getDateReceipt().before(toDate))
                    .collect(Collectors.toList());
            this.listVehicle = FXCollections.observableArrayList(filteredVehicleList);
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            // Tính toán tỷ lệ phần trăm của mỗi loại xe
            int totalVehicles = filteredVehicleList.size();
            System.out.println("test pie chart: totalVehicle:" + totalVehicles);
            Map<String, Integer> vehicleTypeCounts = new HashMap<>();
            for (Vehicle vehicle : filteredVehicleList) {
                vehicleTypeCounts.put(vehicle.getVehicleName(), vehicleTypeCounts.getOrDefault(vehicle.getVehicleName(), 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : vehicleTypeCounts.entrySet()) {
                double percentage = (double) entry.getValue() / totalVehicles * 100;
                pieChartData.add(new PieChart.Data(entry.getKey() + " (" + String.format("%.2f", percentage) + "%)", entry.getValue()));
            }


            // Tạo biểu đồ tròn và thiết lập dữ liệu
            pieChart.setData(pieChartData);
            pieChart.setTitle("Vehicle Types in Parking Lot");
        }
    }

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    public void barChart(ActionEvent actionEvent) {
        barChart.setVisible(true);
        pieChart.setVisible(false);
        stackedBarChart.setVisible(false);
        VehicleDao vehicleDao = new VehicleDao();
        List<Vehicle> vehicleList = vehicleDao.getVehicleList();
        List<Vehicle> filteredVehicleList;
        if (dateFrom.getValue() == null || dateTo.getValue() == null) {
            filteredVehicleList = vehicleList;
        } else {
            Date fromDate = java.sql.Date.valueOf(dateFrom.getValue());
            Date toDate = java.sql.Date.valueOf(dateTo.getValue());
            filteredVehicleList = vehicleList.stream()
                    .filter(vehicle -> !vehicle.getDateReceipt().before(fromDate) && !vehicle.getDateReceipt().after(toDate))
                    .collect(Collectors.toList());
        }
        this.listVehicle = FXCollections.observableArrayList(filteredVehicleList);

        Map<DayOfWeek, Long> vehiclesPerDay = filteredVehicleList.stream()
                .collect(Collectors.groupingBy(vehicle -> vehicle.getDateReceipt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek(), Collectors.counting()));

        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<DayOfWeek, Long> entry : vehiclesPerDay.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }
        barChart.setTitle("Vehicles per Day of Week");
        barChart.getData().add(series);
    }

    @FXML private StackedBarChart<String, Number> stackedBarChart;
    @FXML
    public void stackBarChart() {
        pieChart.setVisible(false);
        barChart.setVisible(false);
        stackedBarChart.setVisible(true);
        VehicleDao vehicleDao = new VehicleDao();
        List<Vehicle> vehicleList = vehicleDao.getVehicleList();
        Map<DayOfWeek, Map<String, Long>> vehiclesPerDayPerType = vehicleList.stream()
                .collect(Collectors.groupingBy(
                        vehicle -> vehicle.getDateReceipt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek(),
                        Collectors.groupingBy(Vehicle::getVehicleName, Collectors.counting())
                ));

        stackedBarChart.getData().clear();
        for (Map.Entry<DayOfWeek, Map<String, Long>> entry : vehiclesPerDayPerType.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(entry.getKey().toString());
            for (Map.Entry<String, Long> entry2 : entry.getValue().entrySet()) {
                series.getData().add(new XYChart.Data<>(entry2.getKey(), entry2.getValue()));
            }
            stackedBarChart.getData().add(series);
        }
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
    public void switchtoStatistics (javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Statistics/Statistics.fxml"));
            root= loader.load();
            StatisticsController controller = loader.getController();
            Platform.runLater(() -> controller.setUsername(Username.getText()));

            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/Statistics/Statistics.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchtoLogin (javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/LoginForm/LoginForm.fxml"));
            root= loader.load();
            LoginController controller = loader.getController();
//            Platform.runLater(() -> controller.setUsername(Username.getText()));
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
            //Platform.runLater(() -> controller.setUsername(Username.getText()));

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
            controller.setParkingLot(this.currentParkingLot);
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
    public void switchtoDailyTicket (javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/DailyTicket/DailyTicket.fxml"));
            root = loader.load();
            DailyTicketController controller = loader.getController();
            Platform.runLater(() -> controller.setUsername(Username.getText()));

            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
    public void switchtoSceneParkingLot(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/ParkingLot/ParkingLot.fxml"));
            root= loader.load();
            ParkingLotController controller = loader.getController();
            Platform.runLater(() -> controller.setUsername(Username.getText()));

            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/ParkingLot/ParkingLot.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchtoUserAccount(javafx.event.ActionEvent actionEvent) {
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



    //đặt username
    public void setUsername (String username){
        Username.setText(username);
    }


    public void setParkingName(String text) {
        lblParkingName.setText(text);
    }

}



