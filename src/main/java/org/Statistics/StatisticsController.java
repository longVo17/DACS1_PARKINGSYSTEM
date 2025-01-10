package org.Statistics;

import Dao.AreaDao;
import Dao.VehicleDao;
import Model.Area;
import Model.ParkingLot;
import Model.Vehicle;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import function.FeeCalculator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import org.UserAccount.UserAccountController;
import Model.Manager;
import javafx.event.ActionEvent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.FXCollections;


// ...



public class StatisticsController {


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

    private ObservableList<Vehicle> listVehicle;
    private ParkingLot currentParkingLot;

    @FXML
    private TableView tableBill;

    @FXML
    private TableColumn<Vehicle, Date> colReceipt;
    @FXML
    private TableColumn<Vehicle, Date> colReturn;
    @FXML
    private TableColumn<Vehicle, String> colId;
    @FXML
    private TableColumn<Vehicle, String> colVehicle;
    @FXML
    private TableColumn<Vehicle, String> colPlates;
    @FXML
    private TableColumn<Vehicle, String> colFee;
    @FXML
    private TableColumn<Vehicle, String> colPenalty;
    @FXML
    private TableColumn<Vehicle, String> colTotal;

    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private TextField txtTotalRevenue;
    @FXML
    private TextField txtTotalVehicle;
    @FXML
    private TextField txtCar;
    @FXML
    private TextField txtMoto;
    @FXML
    private TextField txtBicycle;

    @FXML
    public void initialize() {

        // Đặt vị trí ban đầu của slider (menu trượt) để nó mặc định ở trạng thái đóng
        slider.setTranslateX(-300);

        btnMenu.setOnAction(event -> openMenu(event));
        closeMenu.setOnAction(event -> closeMenu(event));
        btnAccount.setOnAction(event -> toggleAccount(event));
        sliderAccount.setVisible(false);

        //tính tiền
        VehicleDao vehicleDao = new VehicleDao();
        List<Vehicle> vehicleList = vehicleDao.getVehicleListReturned();
        for (Vehicle vehicle : vehicleList) {
            vehicle.setFee(FeeCalculator.calculateFee(vehicle));
            vehicle.setPenalty(FeeCalculator.calculatePenalty(vehicle));
            vehicle.setTotalFee(FeeCalculator.calculateFeeTotal(vehicle));
            vehicleDao.updateVehicle(vehicle);
        }

        //table
        setCellValueFromTable();
        VehicleDao vehicledao = new VehicleDao();
        List<Vehicle> returnedVehiclesAll = vehicledao.getVehicleListReturned();
        ObservableList<Vehicle> observableListAll = FXCollections.observableArrayList(returnedVehiclesAll);
        tableBill.setItems(observableListAll);

        // Lọc danh sách vehicleList
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

// Cập nhật dữ liệu bảng
        this.listVehicle = FXCollections.observableArrayList(filteredVehicleList);
        tableBill.setItems(this.listVehicle);

// Cập nhật các trường txt
        updateTextFields(tableBill.getItems());



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


    }

    private void updateTextFields(ObservableList<Vehicle> currentTableData) {
        // Tính tổng số lượng xe
        txtTotalVehicle.setText(String.valueOf(currentTableData.size()));

        // Tính số lượng từng loại xe
        long carCount = currentTableData.stream().filter(vehicle -> "Car".equals(vehicle.getVehicleName())).count();
        long motoCount = currentTableData.stream().filter(vehicle -> "Motorbike".equals(vehicle.getVehicleName())).count();
        long bicycleCount = currentTableData.stream().filter(vehicle -> "Bicycle".equals(vehicle.getVehicleName())).count();

        txtCar.setText(String.valueOf(carCount));
        txtMoto.setText(String.valueOf(motoCount));
        txtBicycle.setText(String.valueOf(bicycleCount));

        // Tính tổng doanh thu
        double totalRevenue = currentTableData.stream()
                .mapToDouble(vehicle -> Double.parseDouble(vehicle.getTotalFee().replaceAll("[^\\d]", "").replace(',', '.')))
                .sum();

        NumberFormat vietnam = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalRevenue = vietnam.format(totalRevenue);
        txtTotalRevenue.setText(formattedTotalRevenue);
    }


    @FXML
    public void getReturnedVehicles() {
        // Convert DatePicker value to java.util.Date
        Date fromDate = java.sql.Date.valueOf(dateFrom.getValue());
        Date toDate = java.sql.Date.valueOf(dateTo.getValue());

        // Get list of returned vehicles
        VehicleDao vehicleDao = new VehicleDao();
        List<Vehicle> returnedVehicles = vehicleDao.getReturnedVehicles(fromDate, toDate);

        // Update the table data
        this.listVehicle = FXCollections.observableArrayList(returnedVehicles);
        tableBill.setItems(this.listVehicle);

        // Update text fields
        updateTextFields(tableBill.getItems());
    }


    private void setCellValueFromTable() {
        colId.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("id_ticket"));
        colVehicle.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleName"));
        colPlates.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("numberPlates"));
        colReceipt.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("dateReceipt"));
        colReturn.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("dateReturn"));
        colFee.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("fee"));
        colPenalty.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("penalty"));
        //cột tổng thì là feeTota
        colTotal.setCellValueFactory(cellData -> {
            Vehicle vehicle = cellData.getValue();
            String fee = vehicle.getFee().replaceAll("[^\\d]", "").replace(',', '.');
            String penalty = vehicle.getPenalty().replaceAll("[^\\d]", "").replace(',', '.');
            double total = Double.parseDouble(fee) + Double.parseDouble(penalty);
            return new ReadOnlyObjectWrapper<>(String.format("%.2f", total));
        });
    }

    //in hóa đơn pdf

    @FXML
    public void createInvoicePdf() {
        List<Vehicle> vehicles = tableBill.getItems();
        System.out.println("test in"+vehicles.size());
        //file name là date hôm đó
        String fileName = "invoice_" + LocalDate.now() + ".pdf";
        createInvoice(vehicles, fileName);
        //hiện thông báo in thành công
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Invoice has been printed successfully!");
        alert.showAndWait();
        //mở file pdf
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createInvoice(List<Vehicle> vehicles, String fileName) {
        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);


            // Add title and date at the top of the page
            Paragraph title = new Paragraph("INVOICE");
            title.setFontSize(20); // Set font size to 20
            document.add(title);

            Paragraph date;
            date = new Paragraph("Date: " + LocalDate.now().toString());
            date.setFontSize(12); // Set font size to 12
            document.add(date);

            float[] columnWidths = {1, 2, 2, 2, 2, 1, 1, 1}; // Adjust these values as needed
            Table table = new Table(columnWidths);
            table.setWidth(100); // Set the table width to 100% of the page width

            // Add table headers
            List<String> headers = List.of("ID", "Vehicle", "Number Plates", "Receipt Date", "Return Date", "Fee", "Penalty", "Total");
            headers.forEach(columnTitle -> {
                Cell header = new Cell();
                header.setBackgroundColor(new DeviceRgb(211, 211, 211)); // Equivalent to LIGHT_GRAY
                header.add(new Paragraph(columnTitle));

                table.addCell(header);
            });

            // Add table rows
            vehicles.forEach(vehicle -> {
                Cell cell = new Cell().add(new Paragraph(vehicle.getId_ticket()));
                cell.setHeight(10); // Set a fixed height for the cell

                table.addCell(new Cell().add(new Paragraph(vehicle.getId_ticket())));
                table.addCell(new Cell().add(new Paragraph(vehicle.getVehicleName())));
                table.addCell(new Cell().add(new Paragraph(vehicle.getNumberPlates())));
                table.addCell(new Cell().add(new Paragraph(vehicle.getDateReceipt().toString())));
                table.addCell(new Cell().add(new Paragraph(vehicle.getDateReturn().toString())));
                table.addCell(new Cell().add(new Paragraph(vehicle.getFee())));
                table.addCell(new Cell().add(new Paragraph(vehicle.getPenalty())));
                table.addCell(new Cell().add(new Paragraph(vehicle.getTotalFee())));
            });

            document.add(table);

            double total = vehicles.stream()
                    .mapToDouble(vehicle -> Double.parseDouble(vehicle.getTotalFee().replaceAll("[^\\d]", "").replace(',', '.')))
                    .sum();
            Paragraph totalParagraph = new Paragraph("Total: " + total);
            totalParagraph.setFontSize(14); // Set font size to 12
            document.add(totalParagraph);

            document.close(); // Close the document
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
    public void switchtoStatistics_chart(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Statistics/Statistics_chart.fxml"));
            root= loader.load();
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/Statistics/Statistics_chart.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //nhấn enter để đăng nhập

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
           // DailyTicketController controller = loader.getController();
          //  Platform.runLater(() -> controller.setUsername(Username.getText()));

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

}



