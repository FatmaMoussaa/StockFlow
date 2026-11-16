package view;

import controller.AuthController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.DBConnection;


import controller.*;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

import java.sql.SQLException;
import java.util.List;// ← CORRECTION: Ajout de l'import

public class TestLogin extends Application {

    private AuthController authController = new AuthController();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");
        VBox loginCard = createLoginCard(primaryStage);
        StackPane centerPane = new StackPane(loginCard);
        centerPane.setPadding(new Insets(20));
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("StockFlow - Test Connexion");
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }

    private VBox createLoginCard(Stage stage) {
        VBox card = new VBox(25);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setMaxHeight(550);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 40;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);"
        );
        Text icon = new Text("📦✨");
        icon.setFont(Font.font("Segoe UI Emoji", 60));

        Text title = new Text("StockFlow");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.web("#667eea"));

        Text subtitle = new Text("Test de Connexion");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setFill(Color.web("#7F8C8D"));

        VBox headerBox = new VBox(10, icon, title, subtitle);
        headerBox.setAlignment(Pos.CENTER);
        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        usernameField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        Button loginButton = new Button("SE CONNECTER");
        loginButton.setDefaultButton(true);
        loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #764ba2, #667eea);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        ));

        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        ));

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        TextArea debugArea = new TextArea();
        debugArea.setPromptText("Résultats du test...");
        debugArea.setMaxHeight(120);  // ← CORRECTION: Légèrement augmenté
        debugArea.setEditable(false);
        debugArea.setStyle("-fx-font-size: 11px;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Veuillez remplir tous les champs");
                debugArea.setText("❌ Erreur: Champs vides");
                return;
            }

            debugArea.setText("🔐 Tentative de connexion pour: " + username + "\n");

            try {
                if (authController.login(username, password)) {
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("✅ Connexion réussie !");
                    debugArea.appendText("✅ Authentification réussie !\n");
                    debugArea.appendText("👤 Utilisateur: " + authController.getCurrentUser().getUsername() + "\n");
                    debugArea.appendText("🎭 Rôle: " + authController.getCurrentUser().getRole() + "\n");
                    debugArea.appendText("📧 Email: " + authController.getCurrentUser().getEmail() + "\n");
                    debugArea.appendText("✨ Vous pouvez maintenant intégrer ce login à votre application !");
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText("Connexion réussie !");
                    successAlert.setContentText("Bienvenue " + authController.getCurrentUser().getNomComplet() +
                            "\nRôle: " + authController.getCurrentUser().getRole());
                    successAlert.showAndWait();

                } else {
                    messageLabel.setText("❌ Identifiants incorrects");
                    debugArea.appendText("❌ Échec de l'authentification\n");
                    debugArea.appendText("Vérifiez votre nom d'utilisateur et mot de passe\n");
                    debugArea.appendText("Assurez-vous que la table 'utilisateurs' existe et contient votre compte");
                }
            } catch (Exception ex) {
                messageLabel.setText("❌ Erreur de connexion");
                debugArea.appendText("❌ Exception: " + ex.getMessage() + "\n");
                debugArea.appendText("StackTrace: \n");
                for (StackTraceElement element : ex.getStackTrace()) {
                    debugArea.appendText("  " + element.toString() + "\n");
                }
                ex.printStackTrace();
            }
        });

        VBox formBox = new VBox(15, usernameField, passwordField, loginButton, messageLabel, debugArea);
        Button testDBButton = new Button("Tester connexion DB");
        testDBButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );

        testDBButton.setOnAction(e -> {
            try {
                java.sql.Connection conn = DBConnection.getConnection();
                if (conn != null && !conn.isClosed()) {
                    debugArea.setText("✅ Connexion à PostgreSQL réussie !\n");
                    debugArea.appendText("📊 Base de données: " + conn.getMetaData().getDatabaseProductName() + "\n");
                    debugArea.appendText("🔌 URL: " + conn.getMetaData().getURL() + "\n");
                    java.sql.Statement stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM utilisateurs");
                    if (rs.next()) {
                        debugArea.appendText("📋 Table 'utilisateurs' existe avec " + rs.getInt(1) + " enregistrements\n");
                    }
                    rs.close();
                    stmt.close();
                } else {
                    debugArea.setText("❌ Échec de connexion à la base de données");
                }
            } catch (Exception ex) {
                debugArea.setText("❌ Erreur de connexion DB: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(10, testDBButton);
        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(headerBox, formBox, buttonBox);

        return card;
    }

    public static void main(String[] args) {
        System.out.println("=== DÉMARRAGE DU TEST DE LOGIN ===");
        System.out.println("Assurez-vous que:");
        System.out.println("1. PostgreSQL est en cours d'exécution");
        System.out.println("2. La table 'utilisateurs' existe");
        System.out.println("3. Vous avez un compte admin (admin/admin123)");
        System.out.println("=====================================\n");

        launch(args);
    }
}