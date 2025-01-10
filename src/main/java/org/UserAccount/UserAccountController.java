package org.UserAccount;

import Dao.ManagerDao;
import Dao.ParkingLotDao;
import Model.Manager;
import Model.ParkingLot;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import org.Statistics.StatisticsController;
import org.Statistics.Statistics_chartController;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class UserAccountController {

        private Stage stage;
        private Scene scene;
        private Parent root;

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
        private TextField txtUsername;
        @FXML
        private TextField txtPassword;
        @FXML
        private TextField txtEmail;
        @FXML
        private TextField txtPhone;
        @FXML
        private TextField txtParkingLot;
        @FXML
        private TextField txtRole;

        @FXML
        private Button btnCancel;
        @FXML
        private Button btnInsert;
        @FXML
        private Button btnUpdate;
        @FXML
        private Button btnDelete;

        @FXML
        private TableColumn<Manager, Integer> colNo;
        @FXML
        private TableColumn<Manager, String> colUsername;
        @FXML
        private TableColumn<Manager, String> colPassword;
        @FXML
        private TableColumn<Manager, String> colEmail;
        @FXML
        private TableColumn<Manager, String> colPhone;
        @FXML
        private TableColumn<Manager, String> colRole;
        @FXML
        private TableColumn<Manager, String> colParkingLot;
        @FXML
        private TableView<Manager> tableAccount;
        @FXML
        private Label txtRoleAccount;
        @FXML
        private Label txtEmailAccount;
        @FXML
        private Label txtPhoneAccount;
        @FXML
        private Label lblParkingLot;

    private ParkingLot currentParkingLot;

    public void initialize() {



            // Đặt vị trí ban đầu của slider (menu trượt) để nó mặc định ở trạng thái đóng
            slider.setTranslateX(-300);

            btnMenu.setOnAction(event -> openMenu(event));
            closeMenu.setOnAction(event -> closeMenu(event));
            btnAccount.setOnAction(event -> toggleAccount(event));

            sliderAccount.setVisible(false);
                //tab
            CurrentUser currentUser = CurrentUser.getInstance();
            Manager currentManager = currentUser.getLoggedInManager();
            currentParkingLot = currentUser.getLoggedInParkingLot();

            System.out.println("current parking lot: " + currentParkingLot.getNameParkingLot());

            //updateVehicleCounts();
            lblParkingLot.setText(currentParkingLot.getNameParkingLot().toUpperCase());
            txtRoleAccount.setText(currentManager.getRole());
            txtEmailAccount.setText(currentManager.getEmail());
            txtPhoneAccount.setText(currentManager.getPhone());
            Username.setText(currentManager.getUsername());
            //table
        //table
        ManagerDao  managerDao = new ManagerDao();
        List<Manager> listManagerDAO = managerDao.getManagerList();
        ObservableList<Manager> listManager = FXCollections.observableArrayList(listManagerDAO);
        setCellValueFromTable();
        tableAccount.setItems(listManager);

        }

    private void setCellValueFromTable() {
        //nếu là admin
if (txtRoleAccount.getText().equals("admin")) {
            colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1
            ));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
            colParkingLot.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParkingLot().getNameParkingLot()));

    tableAccount.setOnMouseClicked(event -> {
        Manager manager = tableAccount.getItems().get(tableAccount.getSelectionModel().getSelectedIndex());
        txtUsername.setText(manager.getUsername());
        txtPassword.setText(manager.getPassword());
        txtEmail.setText(manager.getEmail());
        txtPhone.setText(manager.getPhone());
        txtRole.setText(manager.getRole());
        txtParkingLot.setText(manager.getParkingLot().getNameParkingLot());
    });
    // dùng nút mũi tên để chọn tk
    tableAccount.setOnKeyPressed(event -> {
        Manager manager = tableAccount.getItems().get(tableAccount.getSelectionModel().getSelectedIndex());
        txtUsername.setText(manager.getUsername());
        txtPassword.setText(manager.getPassword());
        txtEmail.setText(manager.getEmail());
        txtPhone.setText(manager.getPhone());
        txtRole.setText(manager.getRole());
        txtParkingLot.setText(manager.getParkingLot().getNameParkingLot());
    });
        } else {
            //nếu là staff
            colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1
            ));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
            colParkingLot.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParkingLot().getNameParkingLot()));

    tableAccount.setOnMouseClicked(event -> {
        Manager manager = tableAccount.getItems().get(tableAccount.getSelectionModel().getSelectedIndex());
        txtUsername.setText(manager.getUsername());
      //  txtPassword.setText(manager.getPassword());
        txtEmail.setText(manager.getEmail());
        txtPhone.setText(manager.getPhone());
        txtRole.setText(manager.getRole());
        txtParkingLot.setText(manager.getParkingLot().getNameParkingLot());
    });
    // dùng nút mũi tên để chọn tk
    tableAccount.setOnKeyPressed(event -> {
        Manager manager = tableAccount.getItems().get(tableAccount.getSelectionModel().getSelectedIndex());
        txtUsername.setText(manager.getUsername());
      //  txtPassword.setText(manager.getPassword());
        txtEmail.setText(manager.getEmail());
        txtPhone.setText(manager.getPhone());
        txtRole.setText(manager.getRole());
        txtParkingLot.setText(manager.getParkingLot().getNameParkingLot());
    });
        }

//
//        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
//                cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1
//        ));
//        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
//        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
//        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
//        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
//        colParkingLot.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParkingLot().getNameParkingLot()));
//


    }

    @FXML
    public void Insert(javafx.event.ActionEvent e) {
        ManagerDao managerDao = new ManagerDao();
        Manager manager = new Manager();
        manager.setUsername(txtUsername.getText());
        manager.setPassword(txtPassword.getText());
        manager.setEmail(txtEmail.getText());
        manager.setPhone(txtPhone.getText());
        manager.setRole(txtRole.getText());
        ParkingLotDao parkingLotDao = new ParkingLotDao();
        ParkingLot parkingLot = parkingLotDao.findParkingLotByName(txtParkingLot.getText());
        manager.setParkingLot(parkingLot);
        managerDao.insertManager(manager);
        List<Manager> listManagerDAO = managerDao.getManagerList();
        ObservableList<Manager> listManager = FXCollections.observableArrayList(listManagerDAO);
        tableAccount.setItems(listManager);
    }
@FXML
    public void Update(javafx.event.ActionEvent e) {
        ManagerDao managerDao = new ManagerDao();
        Manager manager = new Manager();
        manager.setUsername(txtUsername.getText());
        manager.setPassword(txtPassword.getText());
        manager.setEmail(txtEmail.getText());
        manager.setPhone(txtPhone.getText());
        manager.setRole(txtRole.getText());
    ParkingLotDao parkingLotDao = new ParkingLotDao();
    ParkingLot parkingLot = parkingLotDao.findParkingLotByName(txtParkingLot.getText());
        manager.setParkingLot(parkingLot);
        managerDao.updateManager(manager);
        List<Manager> listManagerDAO = managerDao.getManagerList();
        ObservableList<Manager> listManager = FXCollections.observableArrayList(listManagerDAO);
        tableAccount.setItems(listManager);
    }
    @FXML
    public void Delete(javafx.event.ActionEvent e) {
        ManagerDao managerDao = new ManagerDao();
        Manager manager = new Manager();
        manager.setUsername(txtUsername.getText());
        manager.setPassword(txtPassword.getText());
        manager.setEmail(txtEmail.getText());
        manager.setPhone(txtPhone.getText());
        manager.setRole(txtRole.getText());
        ParkingLotDao parkingLotDao = new ParkingLotDao();
        ParkingLot parkingLot = parkingLotDao.findParkingLotByName(txtParkingLot.getText());
        manager.setParkingLot(parkingLot);
        managerDao.deleteManager(manager);
        List<Manager> listManagerDAO = managerDao.getManagerList();
        ObservableList<Manager> listManager = FXCollections.observableArrayList(listManagerDAO);
        tableAccount.setItems(listManager);
    }

        @FXML
        public void Cancel(javafx.event.ActionEvent e) {
          txtUsername.setText("");
            txtPassword.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
            txtParkingLot.setText("");
            txtRole.setText("");
        }


    //tìm bởi username và email
    @FXML
    private TextField searchEmail;
    @FXML
    private TextField searchUsername;
    @FXML
    public void findByUsernameAndEmail(javafx.event.ActionEvent e) {
        ManagerDao managerDao = new ManagerDao();
        String username = searchUsername.getText();
        String email = searchEmail.getText();
        List<Manager> listManagerDAO = (List<Manager>) managerDao.findManagerByUsernameAndEmail(username, email);
        ObservableList<Manager> listManager = FXCollections.observableArrayList(listManagerDAO);
        tableAccount.setItems(listManager);

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

        @FXML
        private AnchorPane panelHideButtons;
        //đặt username
        public void setUsername(String username) {
            Username.setText(username);
            // Get the role of the user
            ManagerDao userDao = new ManagerDao();
            Manager user = userDao.findManagerByUsername(username);
            String role = user.getRole();

            if (role.equals("admin")) {
                // Display the password
                txtPassword.setText(user.getPassword());
                // Show the buttons
                panelHideButtons.setVisible(false);
            } else if (role.equals("staff")) {
                // Display the message
                txtPassword.setText("You can't view this information.");
                //ko thể thao tác với txtPassword
                txtPassword.setEditable(false);

                // Hide the buttons

                panelHideButtons.setVisible(true);
            }
        }
    }

