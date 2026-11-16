package view;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginForm {

    private AuthController authController = new AuthController();
    private Stage primaryStage;

    public void afficher(Stage stage) {
        this.primaryStage = stage;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");


        VBox loginCard = createLoginCard();


        StackPane centerPane = new StackPane(loginCard);
        centerPane.setPadding(new Insets(20));
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("StockFlow - Connexion");
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }

    private VBox createLoginCard() {
        VBox card = new VBox(25);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setMaxHeight(500);
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

        Text subtitle = new Text("Gestion de Stock Intelligente");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setFill(Color.web("#7F8C8D"));

        VBox headerBox = new VBox(10, icon, title, subtitle);
        headerBox.setAlignment(Pos.CENTER);

        // Champs de formulaire
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

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Veuillez remplir tous les champs");
                return;
            }

            if (authController.login(username, password)) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Connexion réussie !");

                launchMainApp();
            } else {
                messageLabel.setText("Nom d'utilisateur ou mot de passe incorrect");
            }
        });

        VBox formBox = new VBox(15, usernameField, passwordField, loginButton, messageLabel);


        Hyperlink forgotPasswordLink = new Hyperlink("Changer le mot de passe ?");
        forgotPasswordLink.setStyle("-fx-text-fill: #667eea;");
        forgotPasswordLink.setOnAction(e -> showChangePasswordDialog());

        card.getChildren().addAll(headerBox, formBox, forgotPasswordLink);

        return card;
    }

    private void launchMainApp() {
        try {
            Main mainApp = new Main();
            mainApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Impossible de lancer l'application", e.getMessage());
        }
    }

    private void showChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Changer le mot de passe");
        dialog.setHeaderText("Modification du mot de passe");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        PasswordField oldPassField = new PasswordField();
        oldPassField.setPromptText("Ancien mot de passe");
        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Nouveau mot de passe");
        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirmer le nouveau mot de passe");

        grid.add(new Label("Ancien mot de passe:"), 0, 0);
        grid.add(oldPassField, 1, 0);
        grid.add(new Label("Nouveau mot de passe:"), 0, 1);
        grid.add(newPassField, 1, 1);
        grid.add(new Label("Confirmer:"), 0, 2);
        grid.add(confirmPassField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String oldPass = oldPassField.getText();
                String newPass = newPassField.getText();
                String confirmPass = confirmPassField.getText();

                if (newPass.equals(confirmPass)) {
                    // Ici, il faudrait récupérer l'ID de l'utilisateur courant
                    // Pour simplifier, on suppose que c'est l'admin (id = 1)
                    if (authController.changePassword(1, oldPass, newPass)) {
                        showInfoAlert("Succès", "Mot de passe modifié avec succès !");
                    } else {
                        showErrorAlert("Erreur", "Ancien mot de passe incorrect", "");
                    }
                } else {
                    showErrorAlert("Erreur", "Les nouveaux mots de passe ne correspondent pas", "");
                }
            }
        });
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}