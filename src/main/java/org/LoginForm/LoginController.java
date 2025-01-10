package org.LoginForm;

import Dao.ManagerDao;
import Model.Manager;
import Model.ParkingLot;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.MainMenu.MainController;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.MenuParkingLot.MenuParkingController;


public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    @FXML
    private Pane paneLoading;
    @FXML
    private Label loginSuccess;
    @FXML
    private Label loginFailed;
    @FXML
    private ProgressBar progressBar;
    private ParkingLot parkingLot;

    public void initialize() {
        paneLoading.setVisible(false);
        loginSuccess.setVisible(false);
        loginFailed.setVisible(false);
        progressBar.setProgress(0);


        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            loginSuccess.setVisible(false);
            loginFailed.setVisible(false);
            paneLoading.setVisible(true);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1.5), new KeyValue(progressBar.progressProperty(), 1))
            );
            timeline.setOnFinished(e -> {
                // Check the login information against the list of managers
                ManagerDao managerDao = new ManagerDao();
                List<Manager> managers = managerDao.getManagerList();
                Manager loggedInManager = null;
                for (Manager manager : managers) {
                    if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                        loggedInManager = manager;
                        break;
                    }
                }

                if (loggedInManager != null) {
                    // If the login is successful, get the parking lot information
                    parkingLot = loggedInManager.getParkinglot();

                    // Set the logged-in manager and parking lot in the CurrentUser instance
                    CurrentUser currentUser = CurrentUser.getInstance();
                    currentUser.setLoggedInManager(loggedInManager);
                    currentUser.setLoggedInParkingLot(parkingLot);

                    loginSuccess.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                    pause.setOnFinished(event1 -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/MenuParkingLot/MenuParking.fxml"));
                            root = loader.load();
                            MenuParkingController controller = loader.getController();
                            //controller.setParkingLot(this.parkingLot); // Ensure parkingLot is set before switching
                            Platform.runLater(() -> controller.setUsername(usernameField.getText()));
                            //truyền parkingLot đã đăng nhập ở đây qua dailyTicket

                            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            scene.getStylesheets().add(getClass().getResource("/org/MenuParkingLot/MenuParking.css").toExternalForm());
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    pause.play();
                } else {
                    loginFailed.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                    pause.setOnFinished(event1 -> paneLoading.setVisible(false));
                    pause.play();
                }
            });
            progressBar.setProgress(0);
            timeline.play();
        });

        //nhấn enter
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });

    }
    //khi nhấn quen mk
    @FXML
    public void forgotPassword(ActionEvent actionEvent) {
        // Show a dialog to get the user's email
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Forgot Password");
        dialog.setHeaderText("Enter your email:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(email -> {
            // Check if the email exists in the system
            ManagerDao managerDao = new ManagerDao();
            Manager manager = managerDao.getManagerByEmail(email);
            if (manager != null) {
                // If the email exists, send the username and password to the email
                sendEmail(email, "vothanhlong1704@gmail.com", "Password Recovery PARKING LOT MANAGEMENT", "Your username is " + manager.getUsername() + "\n your password is " + manager.getPassword());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Email sent");
                alert.setHeaderText("Email sent successfully!");

            } else {
                // If the email does not exist, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Email not found");
                alert.setContentText("The email you entered does not exist in our system.");
                alert.showAndWait();
            }
        });
    }






    public void switchtoSceneMenuParkingLot (ActionEvent actionEvent) {
        try {
            String username = usernameField.getText();
            System.out.println("Username: " + username);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/MenuParkingLot/MenuParking.fxml"));
            //root = FXMLLoader.load(getClass().getResource("/org/MainMenu/MainMenu.fxml"));
            root= loader.load();
            MainController controller = loader.getController();
            controller.setUsername(username);

            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            //phải tạo scene trc r css sau
            scene.getStylesheets().add(getClass().getResource("/org/MenuParkingLot  /MenuParking.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void increaseProgress() {
        double progress = progressBar.getProgress();
        if (progress < 1) {
            progressBar.setProgress(progress + 0.1);
        }
    }

    public static void sendEmail(String to, String from, String subject, String text) {

        //logic
        //smtp properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.transport.protocl", "stmp");
        String username = "vothanhlong1704@gmail.com";
        String password2 = "lllj vvmp psuc mvmf";


        //session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(username, password2);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email sent");
            alert.setHeaderText("Email sent successfully!");

        } catch (MessagingException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email not sent");
            alert.setHeaderText("Email not sent!");

        }
    }



    }

