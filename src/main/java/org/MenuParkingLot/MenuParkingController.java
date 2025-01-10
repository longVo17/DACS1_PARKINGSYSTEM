package org.MenuParkingLot;

import Dao.AreaDao;
import Dao.ParkingLotDao;
import Model.Area;
import Model.Manager;
import Model.ParkingLot;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.DailyTicket.DailyTicketController;
import org.LoginForm.CurrentUser;
import org.LoginForm.LoginController;
import org.MainMenu.MainController;
import org.MonthlyTicket.MonthlyTicketController;
import org.ParkingLot.ParkingLotController;
import org.Statistics.StatisticsController;
import org.Statistics.Statistics_chartController;
import org.UserAccount.UserAccountController;

import java.io.IOException;
import java.util.List;

public class MenuParkingController {
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
    private ParkingLot currentParkingLot;

    @FXML
    private Label txtRole;
    @FXML
    private Label txtIdCart;
    @FXML
    private Label txtEmail;
    @FXML
    private Label txtPhone;
    @FXML
    public void initialize() {
        // Đặt vị trí ban đầu của slider (menu trượt) để nó mặc định ở trạng thái đóng
        slider.setTranslateX(-300);

        btnMenu.setOnAction(event -> openMenu(event));
        closeMenu.setOnAction(event -> closeMenu(event));
        btnAccount.setOnAction(event -> toggleAccount(event));
        sliderAccount.setVisible(false);
        updateLabels();
        // Get the current user and display their informationz

        CurrentUser currentUser = CurrentUser.getInstance();
        Manager currentManager = currentUser.getLoggedInManager();
        txtRole.setText(currentManager.getRole());
        txtEmail.setText(currentManager.getEmail());
        txtPhone.setText(currentManager.getPhone());
        Username.setText(currentManager.getUsername());
        updateLabels();

    }


    public void openMenu(javafx.event.ActionEvent actionEvent) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(javafx.util.Duration.seconds(0.5));
        slide.setNode(slider);
        slide.setToX(0); // Đặt vị trí cuối cùng mà nút cần di chuyển đến
        slide.play();

        overlay.setVisible(true); // Hiển thị overlay
    }

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


    //đặt username
    public void setUsername(String username) {
        Username.setText(username);
    }

    public void setUserNameAct(java.awt.event.ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/LoginForm/LoginForm.fxml"));

        LoginController controller = loader.getController();
//        controller.setUsername(Username.getText());

    }


    @FXML
    private Label txtParkingName;
@FXML
    private Label txtParkingAddress;
    @FXML
    private Label labelTotal;
    @FXML
    private Label labelMotorbikeArea;
    @FXML
    private Label labelCarArea;

    private ParkingLot parkingLot;


    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        txtParkingName.setText(parkingLot.getNameParkingLot());
        txtParkingAddress.setText(parkingLot.getAddress());

        //labelMotorbikeArea sẽ hiện số xe hiện tại có trong khu vực xe máy
        //labelCarArea sẽ hiện số xe hiện tại có trong khu vực ô tô

        AreaDao areaDao = new AreaDao();
        List<Area>ListArea = areaDao.getAreaByParkingLot(parkingLot);
    }
    public void updateLabels() {
        // Get the updated parking lot from the database
        ParkingLotDao parkingLotDao = new ParkingLotDao();

        CurrentUser currentUser = CurrentUser.getInstance();
        Manager currentManager = currentUser.getLoggedInManager();
        currentParkingLot = currentUser.getLoggedInParkingLot();
        this.parkingLot = parkingLotDao.getParkingLotByName(parkingLotDao.getParkingLotList(), currentParkingLot.getNameParkingLot());

        // Update the labels

        txtParkingName.setText(currentParkingLot.getNameParkingLot());
        txtParkingAddress.setText(currentParkingLot.getAddress());

        List<Area> areas = currentParkingLot.getListArea();
        labelMotorbikeArea.setText(String.valueOf(areas.get(1).getNumberOfVehicle())+" / "+String.valueOf(areas.get(1).getCapacity()));
        labelCarArea.setText(String.valueOf(areas.get(0).getNumberOfVehicle())+" / "+String.valueOf(areas.get(0).getCapacity()));
        labelTotal.setText(String.valueOf(areas.get(0).getNumberOfVehicle() + areas.get(1).getNumberOfVehicle())+" / "+String.valueOf(areas.get(0).getCapacity() + areas.get(1).getCapacity()));

    }



        // ... existing code ...



    //chuyển scene
    @FXML
    public void switchToLogin (javafx.event.ActionEvent actionEvent) {
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
    public void switchtoSceneMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/MainMenu/MainMenu.fxml"));
            root = FXMLLoader.load(getClass().getResource("/org/MainMenu/MainMenu.fxml"));

           // đặt label
            MainController controller = loader.getController();
            controller.setUsername(Username.getText());
            root= loader.load();
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            //phải tạo scene trc r css sau
            scene.getStylesheets().add(getClass().getResource("/org/MainMenu/MainMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchToDailyScene(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/DailyTicket/DailyTicket.fxml"));
            root = loader.load();
            DailyTicketController controller = loader.getController();
             // Ensure parkingLot is set before switching

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
    public void switchtoMonthlyTicket (javafx.event.ActionEvent actionEvent) {
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

    @FXML
    public void switchtoSceneStatistics (javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Statistics/Statistics.fxml"));
            root= loader.load();
          //  StatisticsController controller = loader.getController();
         //   Platform.runLater(() -> controller.setUsername(Username.getText()));

            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/Statistics/Statistics.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
