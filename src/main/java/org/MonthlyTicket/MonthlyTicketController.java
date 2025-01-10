package org.MonthlyTicket;

import Dao.AreaDao;
import Dao.HibernateUtil;
import Dao.VehicleDao;
import Model.Area;
import Model.Manager;
import Model.ParkingLot;
import Model.Vehicle;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.DailyTicket.DailyTicketController;
import org.LoginForm.CurrentUser;
import org.LoginForm.LoginController;
import org.MainMenu.MainController;
import org.MenuParkingLot.MenuParkingController;
import org.ParkingLot.ParkingLotController;
import org.Statistics.StatisticsController;
import org.Statistics.Statistics_chartController;
import org.UserAccount.UserAccountController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MonthlyTicketController {


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

    @FXML
    private ComboBox boxType;

    @FXML
    private Button btnMenuDaily;
    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtOwner;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtId;


    @FXML
    private ImageView imgViewChoose;

    @FXML
    private AnchorPane containerImg;;
    @FXML
    private TextField txtNumberPlate;
    @FXML
    private TableView<Vehicle> tableMonthly;
    @FXML
    private TableColumn<Vehicle, Integer> colNo;
    @FXML
    private TableColumn <Vehicle, String> colId;
    @FXML
    private TableColumn <Vehicle, String> colPlates;
    @FXML
    private TableColumn <Vehicle, String> colVehicle;
    @FXML
    private TableColumn <Vehicle, Date> colReceipt;
    @FXML
    private TableColumn <Vehicle, Date> colExpired;
    @FXML
    private TableColumn <Vehicle, String> colCart;
    @FXML
    private TableColumn <Vehicle, String> colOwner;
    @FXML
    private TableColumn <Vehicle, String> colPhone;
    @FXML
    private Label vacancyCar;
    @FXML
    private Label txtRole;
    @FXML
    private Label txtIdCart;
    @FXML
    private Label txtEmail;
    @FXML
    private Label txtPhone1;
    @FXML
    private Label vacancyMoto;
    @FXML
    private Label lblParkingLot;
    private ParkingLot currentParkingLot;
    private ObservableList<Object> listVehicle;

    @FXML
    public void initialize() {
        boxType.getItems().addAll("Car", "Motorbike", "Bicycle");
        boxType.getSelectionModel().selectFirst(); // Chọn phần tử đầu tiên làm mặc định
        // Các phần khác của phương thức initialize()

        // Đặt vị trí ban đầu của slider (menu trượt) để nó mặc định ở trạng thái đóng
        slider.setTranslateX(-300);

        btnMenu.setOnAction(event -> openMenu(event));
        closeMenu.setOnAction(event -> closeMenu(event));
        btnAccount.setOnAction(event -> toggleAccount(event));
        sliderAccount.setVisible(false);

        colNo.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("No"));

        //table
        VehicleDao vehicleDao = new VehicleDao();
        List<Vehicle> vehicleList = vehicleDao.getVehicleListByMT();
        ObservableList<Vehicle> listVehicle = FXCollections.observableArrayList(vehicleList);
        setCellValueFromTable();
        tableMonthly.setItems(listVehicle);

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
        colCart.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("idCart"));
        colOwner.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("nameOfOwner"));
        colPhone.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("phoneNumber"));
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
                if (!v.isReturned()) {
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
    public void PickupVehicle(javafx.event.ActionEvent e) {
        if (listVehicle == null) {
            listVehicle = FXCollections.observableArrayList();
        }
        if (txtNumberPlate.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter number plates");
            alert.showAndWait();
            return;
        }
        VehicleDao vehicleDao = new VehicleDao();
        List<Vehicle> dsx = vehicleDao.getVehicleListNotReturned();

        //AREA
        AreaDao areaDao = new AreaDao();
        List<Area> arealist = areaDao.getAreaList();

        for (Vehicle v : dsx) {
            if (v.getNumberPlates().equals(txtNumberPlate.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Your number plates is already in the system");
                alert.showAndWait();
                return;
            }
        }
        int carCount = 1;
        int bikeCount = 51;

        //id
        Calendar calendar = Calendar.getInstance(); // lấy thể hiện hiện tại của Calendar
        calendar.setTime(new Date()); // đặt thời gian của Calendar là ngày hiện tại
        calendar.add(Calendar.MONTH, 1); // thêm 1 tháng vào Calendar
        Date oneMonthLater = calendar.getTime(); // lấy Date từ Calendar

        System.out.println("car count: " + carCount);
        System.out.println("bike count:" + bikeCount);
        Vehicle vehicle = new Vehicle();
        if (boxType.getSelectionModel().getSelectedItem().equals("Car")) {
//            public Vehicle(Area area, String vehicleName, String numberPlates, Date dateReceipt,
//                    Date dateReturn, String typeOfTicket, String nameOfOwner, String phoneNumber, String manager, String idCart)
            vehicle = new Vehicle(arealist.get(0), "Car", txtNumberPlate.getText(), new Date(), oneMonthLater,null, false,"Monthly Ticket", txtOwner.getText(), txtPhone.getText(), Username.getText(),txtId.getText(),null,null,null);
            vehicle.setId_ticket("MT" + carCount++);

        } else if (boxType.getSelectionModel().getSelectedItem().equals("Motorbike")) {
            vehicle = new Vehicle(arealist.get(2), "Motorbike", txtNumberPlate.getText(), new Date(), oneMonthLater,null, false, "Monthly Ticket",  txtOwner.getText(), txtPhone.getText(), Username.getText(),txtId.getText(),null,null,null);
            vehicle.setId_ticket("MT" + bikeCount++);

        } else {
            vehicle = new Vehicle(arealist.get(2), "Bicycle", txtNumberPlate.getText(), new Date(), oneMonthLater,null, false,"Monthly Ticket", txtOwner.getText(), txtPhone.getText(), Username.getText(),txtId.getText(),null,null,null);
            vehicle.setId_ticket("MT" + bikeCount++);
        }

        List<Vehicle> dsxCheck = vehicleDao.getVehicleList();
        for (Vehicle v : dsxCheck) {
            if (vehicle.getId_ticket().equals(v.getId_ticket())) {
                if (vehicle.getVehicleName().equals("Car")) {
                    vehicle.setId_ticket("MT" + carCount++);
                } else {
                    vehicle.setId_ticket("MT" + bikeCount++);
                }
            }
        }

        listVehicle.add(vehicle);


        String type = "Motorbike area";
        if (boxType.getSelectionModel().getSelectedItem().equals("Car")) {
            type = "Car area";
        }
        for (Area a : arealist) {
            if (a.getAreaName().equals(type)) {
                vehicle.setArea(a);
                break;
            }
        }
        vehicleDao.addVehicle(vehicle);
        listVehicle.add(vehicle);
        //update vacancy
        updateVehicleCountInParkingLot(currentParkingLot);

        List<Vehicle> vehicleList = vehicleDao.getVehicleListByMT();
        ObservableList<Vehicle> listVehicle = FXCollections.observableArrayList(vehicleList);

        setCellValueFromTable();
        tableMonthly.setItems(listVehicle);
        createTicketPdf(vehicle);
        //thông báo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Pick up vehicle successfully");
        alert.showAndWait();
        //mở file pdf
        try {
            File file = new File("ticket_" + vehicle.getId_ticket() + ".pdf");
            if (file.exists()) {
                Process p = Runtime
                        .getRuntime()
                        .exec("rundll32 url.dll,FileProtocolHandler " + "ticket_" + vehicle.getId_ticket() + ".pdf");
                p.waitFor();
            } else {
                System.out.println("File is not exists");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void createTicketPdf(Vehicle vehicle) {
        String fileName = "ticket_" + vehicle.getId_ticket() + ".pdf";
        String fee = "";
        if(vehicle.getVehicleName().equals("Car")){
            fee="400000";
        }else if(vehicle.getVehicleName().equals("Motorbike")){
            fee="50000";
        }
        else{
            fee="25000";
        }
        //format tiền việt
        String formattedFee = String.format("%,d", Integer.parseInt(fee));
        fee = formattedFee.replace(",", ".");

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title and date at the top of the page
            Paragraph title = new Paragraph("VEHICLE TICKET");
            title.setFontSize(20); // Set font size to 20
            title.setBold();

            document.add(title);

            Paragraph date = new Paragraph("Date: " + LocalDate.now().toString());
            date.setFontSize(12); // Set font size to 12
            document.add(date);

            // Add vehicle details
            if (vehicle.getVehicleName().equals("Car")) {
                //random số từ 1 đến 5
                Random random = new Random();
                int number = random.nextInt(5) + 1;
                ImageData imageData = ImageDataFactory.create("src/main/resources/org/DailyTicket/car" + number + ".png");
                com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
                document.add(image);
            }
            else if (vehicle.getVehicleName().equals("Motorbike")) {
                //random số từ 1 đến 3
                Random random = new Random();
                int number = random.nextInt(3) + 1;
                ImageData imageData = ImageDataFactory.create("src/main/resources/org/DailyTicket/motorbike" + number + ".png");
                com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
                document.add(image);
            }
            else {
                //random số từ 1 đến 2
                Random random = new Random();
                int number = random.nextInt(2) + 1;
                ImageData imageData = ImageDataFactory.create("src/main/resources/org/DailyTicket/bicycle" + number + ".png");
                com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
                document.add(image);
            }

            Paragraph ticketDetails = new Paragraph(
                    "Ticket ID: " + vehicle.getId_ticket() + "\n" +
                            "Number Plate: " + vehicle.getNumberPlates() + "\n" +
                            "Fee: " + fee + "\n" +
                            "Receipt Date: " + vehicle.getDateReceipt() + "\n" +
                            "Expiration Date: " + vehicle.getDateExpired() + "\n"
            );
            ticketDetails.setFontSize(14); // Set font size to 12

            //cảnh báo nếu trả xe trễ xẽ bị phạt
            Paragraph warning = new Paragraph("\nNote: If you return the vehicle late, you will be fined.");
            warning.setFontSize(12); // Set font size to 12

            document.add(ticketDetails);
            document.add(warning);
            document.close();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }



    @FXML
    public void Cancel(javafx.event.ActionEvent e) {
        imgViewChoose.setImage(null);
        txtNumberPlate.setText("");
        txtOwner.setText("");
        txtPhone.setText("");
        txtId.setText("");
        boxType.getSelectionModel().selectFirst();
    }


    @FXML
    public void ChooseImage(javafx.event.ActionEvent e) {
        System.setProperty("TESSDATA_PREFIX", "D:\\JAVA PROJECT\\DACS1\\tessdata");
        Stage st = (Stage) imgViewChoose.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a number plates");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(st);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imgViewChoose.setImage(image);
        }
        Tesseract tessInst = new Tesseract();
        tessInst.setDatapath("D:\\JAVA PROJECT\\DACS1");
        try {
            String result = tessInst.doOCR(file);
            result = result.replace(".", ""); // Loại bỏ tất cả các dấu chấm
            result = result.replace("L", "4");
            result = result.replace("I", "1");
            result = result.replace("O", "0");
            result = result.replace("]", "");
            result = result.replace("[", "");
            result = result.replace("{", "");
            result = result.replace("}", "");
            result = result.replace("(", "");
            result = result.replace(")", "");
            result = result.replace(" ", "");
            result = result.replace("/", "");
            result = result.replace("\\", "");
            result = result.replace("\"", "");

            result  = result.replace("?", "");
            result = result.replace("|", "");

            // Chuyển tất cả các ký tự thành chữ hoa
            result = result.toUpperCase();

            System.out.println(result);
            txtNumberPlate.setText(result);

        } catch (TesseractException ex) {
            System.err.println(ex.getMessage());

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
    public void switchtoSceneMonthly(javafx.event.ActionEvent actionEvent) {
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
 //           StatisticsController controller = loader.getController();
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


