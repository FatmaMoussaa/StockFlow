package view;

import controller.*;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
import javafx.stage.Screen;
import model.*;

import java.sql.SQLException;
import java.util.List;

public class Main extends Application {


    private TraceController traceController = new TraceController();
    private MouvementController mouvementController = new MouvementController();
    private ArticleController articleController = new ArticleController();
    private LocalController localController = new LocalController();
    private FournisseurController fournisseurController = new FournisseurController();
    private ServiceController serviceController = new ServiceController();
    private StockController stockController = new StockController();
    private AuthController authController = new AuthController();

    private TableView<Trace> traceTable;
    private TableView<Mouvement> mouvementTable;

    private Stage primaryStage;


    private double screenWidth;
    private double screenHeight;


    private static final String BACKGROUND_COLOR = "#F9FAFB";
    private static final String CARD_BACKGROUND = "#FFFFFF";
    private static final String MENU_BACKGROUND = "linear-gradient(to bottom, #667eea 0%, #764ba2 100%)";
    private static final String HEADER_BACKGROUND = "linear-gradient(to right, #667eea, #764ba2)";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Screen screen = Screen.getPrimary();
        screenWidth = screen.getVisualBounds().getWidth();
        screenHeight = screen.getVisualBounds().getHeight();
        primaryStage.setMaximized(true);
        if (!authController.isLoggedIn()) {
            showWelcomeScreen();
        } else {
            launchMainApp();
        }
    }
    private void showWelcomeScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");

        VBox welcomeCard = createWelcomeCard();
        StackPane centerPane = new StackPane(welcomeCard);
        centerPane.setPadding(new Insets(20));
        root.setCenter(centerPane);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("StockFlow - Bienvenue");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createWelcomeCard() {
        VBox card = new VBox(25);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(500);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 50;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);"
        );

        Text icon = new Text("📦✨");
        icon.setFont(Font.font("Segoe UI Emoji", 70));

        Text title = new Text("StockFlow");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        title.setFill(Color.web("#667eea"));

        Text subtitle = new Text("Gestion de Stock Intelligente");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        subtitle.setFill(Color.web("#7F8C8D"));

        VBox headerBox = new VBox(10, icon, title, subtitle);
        headerBox.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("🔐 SE CONNECTER");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        Button registerBtn = new Button("📝 CRÉER UN COMPTE");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #667eea;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #667eea;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #764ba2, #667eea);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        ));

        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        ));

        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle(
                "-fx-background-color: #667eea;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        ));

        registerBtn.setOnMouseExited(e -> registerBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #667eea;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #667eea;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-cursor: hand;"
        ));

        loginBtn.setOnAction(e -> showLoginScreen());
        registerBtn.setOnAction(e -> showRegisterScreen());

        VBox buttonBox = new VBox(15, loginBtn, registerBtn);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        card.getChildren().addAll(headerBox, buttonBox);

        return card;
    }
    private void showLoginScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");

        VBox loginCard = createLoginCard();
        StackPane centerPane = new StackPane(loginCard);
        centerPane.setPadding(new Insets(20));
        root.setCenter(centerPane);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("StockFlow - Connexion");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createLoginCard() {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(450);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 50;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);"
        );

        Text title = new Text("🔐 Connexion");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.web("#667eea"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        usernameField.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        Button loginButton = new Button("SE CONNECTER");
        loginButton.setDefaultButton(true);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px;");

        Button backButton = new Button("← Retour");
        backButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #667eea;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 14px;" +
                        "-fx-underline: true;"
        );
        backButton.setOnAction(e -> showWelcomeScreen());

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Veuillez remplir tous les champs");
                return;
            }

            if (authController.login(username, password)) {
                launchMainApp();
            } else {
                messageLabel.setText("Nom d'utilisateur ou mot de passe incorrect");
            }
        });

        VBox formBox = new VBox(18, title, usernameField, passwordField, loginButton, messageLabel, backButton);
        card.getChildren().add(formBox);

        return card;
    }
    private void showRegisterScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");

        VBox registerCard = createRegisterCard();
        StackPane centerPane = new StackPane(registerCard);
        centerPane.setPadding(new Insets(20));
        root.setCenter(centerPane);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("StockFlow - Création de compte");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createRegisterCard() {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(500);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 50;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);"
        );

        Text title = new Text("📝 Créer un compte");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.web("#667eea"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur *");
        usernameField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        TextField nomField = new TextField();
        nomField.setPromptText("Nom complet *");
        nomField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        TextField emailField = new TextField();
        emailField.setPromptText("Email *");
        emailField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe *");
        passwordField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirmer le mot de passe *");
        confirmField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;"
        );

        Button registerButton = new Button("CRÉER MON COMPTE");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 15px;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button backButton = new Button("← Retour à l'accueil");
        backButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #667eea;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;" +
                        "-fx-underline: true;"
        );
        backButton.setOnAction(e -> showWelcomeScreen());

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String nomComplet = nomField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirm = confirmField.getText();

            if (username.isEmpty() || nomComplet.isEmpty() || email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Veuillez remplir tous les champs obligatoires (*)");
                return;
            }

            if (!password.equals(confirm)) {
                messageLabel.setText("Les mots de passe ne correspondent pas");
                return;
            }

            if (!email.contains("@")) {
                messageLabel.setText("Email invalide");
                return;
            }

            if (password.length() < 4) {
                messageLabel.setText("Le mot de passe doit contenir au moins 4 caractères");
                return;
            }

            Utilisateur newUser = new Utilisateur();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setNomComplet(nomComplet);
            newUser.setEmail(email);
            newUser.setRole("USER");

            if (authController.createUser(newUser)) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText("Compte créé avec succès !");
                successAlert.setContentText("Vous pouvez maintenant vous connecter avec votre compte.");
                successAlert.showAndWait();

                showLoginScreen();
            } else {
                messageLabel.setText("Erreur: Ce nom d'utilisateur existe déjà");
            }
        });

        VBox formBox = new VBox(12, title, usernameField, nomField, emailField, passwordField, confirmField, registerButton, messageLabel, backButton);
        card.getChildren().add(formBox);

        return card;
    }
    private void launchMainApp() {
        primaryStage.setTitle("✨ StockFlow - Gestion de Stock Moderne - Connecté en tant que: " + authController.getCurrentUser().getUsername() + " ✨");

        VBox header = createGradientHeader();
        VBox menuBox = createColorfulMenu();
        traceTable = createColorfulTraceTable();
        mouvementTable = createColorfulMouvementTable();
        VBox rightPanel = createColorfulTablesPanel();

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(menuBox, rightPanel);
        splitPane.setDividerPositions(0.22);
        splitPane.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(splitPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setPadding(new Insets(0));

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setCenter(scrollPane);
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10);
        dropShadow.setOffsetY(5);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        header.setEffect(dropShadow);

        Scene scene = new Scene(mainLayout, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        refreshTables();
    }

    private VBox createGradientHeader() {
        Text emoji = new Text("✨📦");
        emoji.setFont(Font.font("Segoe UI Emoji", 45));

        Text title = new Text("StockFlow");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 34));
        title.setFill(Color.WHITE);

        Text subtitle = new Text("Gestion de Stock Intelligente");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 16));
        subtitle.setFill(Color.web("#E0D4FF"));

        Label versionBadge = new Label("v2.0");
        versionBadge.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        versionBadge.setTextFill(Color.web("#667eea"));
        versionBadge.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 6 14 6 14;");

        Label userLabel = new Label("👤 " + authController.getCurrentUser().getUsername() + " (" + authController.getCurrentUser().getRole() + ")");
        userLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        userLabel.setTextFill(Color.web("#E0D4FF"));

        Button logoutButton = new Button("Déconnexion");
        logoutButton.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 12 6 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;"
        );
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle(
                "-fx-background-color: rgba(255,255,255,0.3);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 12 6 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;"
        ));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 12 6 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;"
        ));

        logoutButton.setOnAction(e -> {
            authController.logout();
            showWelcomeScreen();
        });

        VBox titleBox = new VBox(5, title, subtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox leftContent = new HBox(15, emoji, titleBox);
        leftContent.setAlignment(Pos.CENTER_LEFT);

        HBox rightContent = new HBox(20, userLabel, logoutButton, versionBadge);
        rightContent.setAlignment(Pos.CENTER_RIGHT);

        BorderPane headerContent = new BorderPane();
        headerContent.setLeft(leftContent);
        headerContent.setRight(rightContent);
        headerContent.setPadding(new Insets(20, 35, 20, 35));
        headerContent.setStyle("-fx-background-color: " + HEADER_BACKGROUND + ";");

        VBox header = new VBox(headerContent);
        header.setStyle("-fx-background-color: " + HEADER_BACKGROUND + ";");

        return header;
    }

    private VBox createColorfulMenu() {
        Label menuTitle = new Label("🍀 MENU");
        menuTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        menuTitle.setTextFill(Color.WHITE);

        Label menuSubtitle = new Label("Navigation principale");
        menuSubtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        menuSubtitle.setTextFill(Color.web("#E0D4FF"));

        VBox menuHeader = new VBox(5, menuTitle, menuSubtitle);
        menuHeader.setPadding(new Insets(0, 0, 25, 0));

        Button btnArticles = createGradientMenuButton("📦 Articles", "#FF6B6B", "#FF8E53");
        Button btnLocaux = createGradientMenuButton("🏢 Locaux", "#4ECDC4", "#44A08D");
        Button btnFournisseurs = createGradientMenuButton("🤝 Fournisseurs", "#A8E6CF", "#3B8D99");
        Button btnServices = createGradientMenuButton("🏥 Services", "#FFD93D", "#F9A826");
        Button btnStock = createGradientMenuButton("📊 Stock", "#6C5CE7", "#A29BFE");
        Button btnCommandesExternes = createGradientMenuButton("🚚 Commandes Ext.", "#00B4DB", "#0083B0");
        Button btnCommandeInterne = createGradientMenuButton("🔄 Commandes Int.", "#F2709C", "#FF9472");
        Button btnInventaire = createGradientMenuButton("📝 Inventaire", "#11998E", "#38EF7D");
        Button btnStatistiques = createGradientMenuButton("📈 Statistiques", "#FC466B", "#3F5EFB");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: rgba(255,255,255,0.2);");

        Button btnRefreshTables = createGradientMenuButton("🔄 Rafraîchir", "#FFB347", "#FFCC33");

        VBox menuBox = new VBox(12, menuHeader, btnArticles, btnLocaux, btnFournisseurs,
                btnServices, btnStock, btnCommandesExternes, btnCommandeInterne,
                btnInventaire, btnStatistiques, separator, btnRefreshTables);
        menuBox.setPadding(new Insets(25, 25, 25, 25));
        menuBox.setStyle("-fx-background-color: " + MENU_BACKGROUND + ";");
        menuBox.setMinWidth(280);

        btnArticles.setOnAction(e -> new ArticleForm().afficher());
        btnLocaux.setOnAction(e -> new LocalForm().afficher());
        btnFournisseurs.setOnAction(e -> new FournisseurForm().afficher());
        btnServices.setOnAction(e -> new ServiceForm().afficher());
        btnStock.setOnAction(e -> new StockForm().afficher());
        btnCommandesExternes.setOnAction(e -> {
            new CommandeExterneForm().afficher(
                    fournisseurController.getAllFournisseurs(),
                    articleController.getAllArticles(),
                    localController.getAllLocaux()
            );
        });
        btnCommandeInterne.setOnAction(e -> {
            try {
                new CommandeInterneForm().afficher(
                        serviceController.getAllServices(),
                        articleController.getAllArticles(),
                        localController.getAllLocaux()
                );
            } catch (SQLException ex) {
                showErrorAlert("Erreur", "Impossible de charger les services", ex.getMessage());
            }
        });
        btnInventaire.setOnAction(e -> {
            new InventaireForm(articleController.getAllArticles(),
                    localController.getAllLocaux(),
                    stockController.getStockGlobal()
            ).afficher();
        });
        btnStatistiques.setOnAction(e -> new StatistiqueForm().afficher());
        btnRefreshTables.setOnAction(e -> refreshTables());

        return menuBox;
    }

    private Button createGradientMenuButton(String text, String color1, String color2) {
        Button btn = new Button(text);
        btn.setMinWidth(230);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        btn.setStyle(
                "-fx-background-color: linear-gradient(to right, " + color1 + ", " + color2 + ");" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14 18 14 18;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to right, " + color2 + ", " + color1 + ");" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14 18 14 18;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);" +
                        "-fx-scale-x: 1.02;" +
                        "-fx-scale-y: 1.02;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to right, " + color1 + ", " + color2 + ");" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14 18 14 18;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);" +
                        "-fx-scale-x: 1;" +
                        "-fx-scale-y: 1;"
        ));
        return btn;
    }

    private VBox createColorfulTablesPanel() {
        ScrollPane mouvementScrollPane = new ScrollPane(mouvementTable);
        mouvementScrollPane.setFitToWidth(true);
        mouvementScrollPane.setFitToHeight(true);
        mouvementScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mouvementScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mouvementScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        ScrollPane traceScrollPane = new ScrollPane(traceTable);
        traceScrollPane.setFitToWidth(true);
        traceScrollPane.setFitToHeight(true);
        traceScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        traceScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        traceScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox traceSection = createStyledSection(
                "📜 HISTORIQUE DES TRACES",
                "👥 Suivi des actions utilisateurs",
                "#FF6B6B",
                traceScrollPane
        );

        VBox mouvementSection = createStyledSection(
                "🔄 MOUVEMENTS DE STOCK",
                "📊 Historique des entrées et sorties",
                "#4ECDC4",
                mouvementScrollPane
        );

        VBox rightPanel = new VBox(25, traceSection, mouvementSection);
        rightPanel.setPadding(new Insets(25));
        rightPanel.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        return rightPanel;
    }

    private VBox createStyledSection(String title, String subtitle, String accentColor, ScrollPane scrollPane) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#2C3E50"));

        Label badge = new Label("●");
        badge.setTextFill(Color.web(accentColor));
        badge.setFont(Font.font(22));

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitleLabel.setTextFill(Color.web("#7F8C8D"));

        HBox titleBox = new HBox(10, badge, titleLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        VBox headerBox = new VBox(5, titleBox, subtitleLabel);

        VBox card = new VBox(15, headerBox, scrollPane);
        card.setStyle(
                "-fx-background-color: " + CARD_BACKGROUND + ";" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 5);"
        );

        return card;
    }

    private TableView<Trace> createColorfulTraceTable() {
        TableView<Trace> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: transparent;");

        table.setRowFactory(tv -> new TableRow<Trace>() {
            @Override
            protected void updateItem(Trace item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #F8F9FA;");
                    } else {
                        setStyle("-fx-background-color: white;");
                    }
                }
            }
        });

        TableColumn<Trace, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idTrace"));
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Trace, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        TableColumn<Trace, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateAction"));

        TableColumn<Trace, String> userCol = new TableColumn<>("Utilisateur");
        userCol.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));

        String headerStyle = "-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12;";
        idCol.setStyle(headerStyle);
        actionCol.setStyle(headerStyle);
        dateCol.setStyle(headerStyle);
        userCol.setStyle(headerStyle);

        table.getColumns().addAll(idCol, actionCol, dateCol, userCol);
        table.setPrefHeight(300);

        return table;
    }

    private TableView<Mouvement> createColorfulMouvementTable() {
        TableView<Mouvement> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: transparent;");

        table.setRowFactory(tv -> new TableRow<Mouvement>() {
            @Override
            protected void updateItem(Mouvement item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #F8F9FA;");
                    } else {
                        setStyle("-fx-background-color: white;");
                    }
                }
            }
        });

        TableColumn<Mouvement, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getIdMouvement()));
        idCol.setMinWidth(60);
        idCol.setMaxWidth(80);

        TableColumn<Mouvement, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getTypeMouvement() != null ? cell.getValue().getTypeMouvement().toString() : ""
        ));
        typeCol.setMinWidth(80);
        typeCol.setMaxWidth(100);

        typeCol.setCellFactory(column -> new TableCell<Mouvement, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("ENTREE".equals(item)) {
                        setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-background-color: #E8F5E9; -fx-background-radius: 5;");
                        setAlignment(Pos.CENTER);
                    } else if ("SORTIE".equals(item)) {
                        setStyle("-fx-text-fill: #FF5252; -fx-font-weight: bold; -fx-background-color: #FFEBEE; -fx-background-radius: 5;");
                        setAlignment(Pos.CENTER);
                    }
                }
            }
        });

        TableColumn<Mouvement, String> articleCol = new TableColumn<>("Article");
        articleCol.setCellValueFactory(cell -> {
            Article a = articleController.getAllArticles().stream()
                    .filter(ar -> ar.getIdArticle() == cell.getValue().getIdArticle())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(a != null ? a.getReference() : "");
        });
        articleCol.setMinWidth(150);
        articleCol.setMaxWidth(250);

        TableColumn<Mouvement, String> localCol = new TableColumn<>("Local");
        localCol.setCellValueFactory(cell -> {
            Local l = localController.getAllLocaux().stream()
                    .filter(loc -> loc.getIdLocal() == cell.getValue().getIdLocal())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(l != null ? l.getNomLocal() : "");
        });
        localCol.setMinWidth(120);
        localCol.setMaxWidth(180);

        TableColumn<Mouvement, Integer> qteCol = new TableColumn<>("Quantité");
        qteCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantite()));
        qteCol.setMinWidth(80);
        qteCol.setMaxWidth(100);
        qteCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<Mouvement, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getDateMouvement() != null ? cell.getValue().getDateMouvement().toString() : ""
        ));
        dateCol.setMinWidth(120);
        dateCol.setMaxWidth(150);

        TableColumn<Mouvement, String> obsCol = new TableColumn<>("Observation");
        obsCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getObservation()));
        obsCol.setMinWidth(200);
        obsCol.setMaxWidth(400);

        String headerStyle = "-fx-background-color: #4ECDC4; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12;";
        idCol.setStyle(headerStyle);
        typeCol.setStyle(headerStyle);
        articleCol.setStyle(headerStyle);
        localCol.setStyle(headerStyle);
        qteCol.setStyle(headerStyle);
        dateCol.setStyle(headerStyle);
        obsCol.setStyle(headerStyle);

        table.getColumns().addAll(idCol, typeCol, articleCol, localCol, qteCol, dateCol, obsCol);
        table.setPrefHeight(380);

        return table;
    }

    private void refreshTables() {
        try {
            traceTable.setItems(FXCollections.observableArrayList(traceController.getAllTraces()));
            mouvementTable.setItems(FXCollections.observableArrayList(mouvementController.getAllMouvements()));
        } catch (Exception e) {
            showErrorAlert("❌ Erreur", "Impossible de rafraîchir les données", e.getMessage());
        }
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}