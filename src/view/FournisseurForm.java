package view;

import controller.FournisseurController;
import model.Fournisseur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

public class FournisseurForm {

    private FournisseurController fc = new FournisseurController();
    private ObservableList<Fournisseur> fournisseursList = FXCollections.observableArrayList();


    private static final String GRADIENT_PRIMARY = "linear-gradient(to right, #667eea, #764ba2)";
    private static final String GRADIENT_SUCCESS = "linear-gradient(to right, #4facfe, #00f2fe)";
    private static final String GRADIENT_DANGER = "linear-gradient(to right, #fa709a, #fee140)";
    private static final String GRADIENT_WARNING = "linear-gradient(to right, #f093fb, #f5576c)";
    private static final String GRADIENT_INFO = "linear-gradient(to right, #4ECDC4, #44A08D)";
    private static final String BACKGROUND_COLOR = "#F9FAFB";
    private static final String CARD_COLOR = "#FFFFFF";
    private static final String VIOLET_MAIN = "#667eea";

    public void afficher() {
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("✨ Gestion des Fournisseurs - StockFlow ✨");


        TextField txtNom = new TextField();
        TextField txtAdresse = new TextField();
        TextField txtTel = new TextField();
        TextField txtEmail = new TextField();
        TextField txtContact = new TextField();


        String fieldStyle = "-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;";
        txtNom.setStyle(fieldStyle);
        txtAdresse.setStyle(fieldStyle);
        txtTel.setStyle(fieldStyle);
        txtEmail.setStyle(fieldStyle);
        txtContact.setStyle(fieldStyle);

        txtNom.setPromptText("Ex: TechSARL");
        txtAdresse.setPromptText("Ex: 12 Rue de Paris");
        txtTel.setPromptText("Ex: 06 12 34 56 78");
        txtEmail.setPromptText("Ex: contact@techsarl.com");
        txtContact.setPromptText("Ex: Mohamed Ali");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        formGrid.add(createStyledLabel("Nom :"), 0, 0);
        formGrid.add(txtNom, 1, 0);
        formGrid.add(createStyledLabel("Adresse :"), 0, 1);
        formGrid.add(txtAdresse, 1, 1);
        formGrid.add(createStyledLabel("Téléphone :"), 0, 2);
        formGrid.add(txtTel, 1, 2);
        formGrid.add(createStyledLabel("Email :"), 0, 3);
        formGrid.add(txtEmail, 1, 3);
        formGrid.add(createStyledLabel("Contact :"), 0, 4);
        formGrid.add(txtContact, 1, 4);

        TitledPane tpForm = new TitledPane("📝 Détails du Fournisseur", formGrid);
        tpForm.setExpanded(true);
        tpForm.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        Button btnAjouter = createStyledButton("➕ Ajouter", GRADIENT_SUCCESS);
        Button btnModifier = createStyledButton("✏️ Modifier", GRADIENT_PRIMARY);
        Button btnSupprimer = createStyledButton("🗑️ Supprimer", GRADIENT_DANGER);
        Button btnReset = createStyledButton("🔄 Reset", GRADIENT_WARNING);

        HBox actionButtons = new HBox(12, btnAjouter, btnModifier, btnSupprimer, btnReset);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(5));

        TitledPane tpActions = new TitledPane("⚡ Actions", actionButtons);
        tpActions.setExpanded(true);
        tpActions.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        TextField txtRecherche = new TextField();
        txtRecherche.setStyle(fieldStyle);
        txtRecherche.setPromptText("Nom du fournisseur...");

        HBox searchBox = new HBox(12, createStyledLabel("Recherche :"), txtRecherche);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10));

        VBox searchContainer = new VBox(searchBox);
        searchContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TitledPane tpSearch = new TitledPane("🔍 Recherche Fournisseur", searchContainer);
        tpSearch.setExpanded(true);
        tpSearch.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        TableView<Fournisseur> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent;");

        TableColumn<Fournisseur, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomFournisseur"));
        colNom.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Fournisseur, String> colAdresse = new TableColumn<>("Adresse");
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colAdresse.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Fournisseur, String> colTel = new TableColumn<>("Téléphone");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colTel.setStyle("-fx-alignment: CENTER;");

        TableColumn<Fournisseur, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Fournisseur, String> colContact = new TableColumn<>("Contact");
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactPrincipal"));
        colContact.setStyle("-fx-alignment: CENTER-LEFT;");

        colNom.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        colAdresse.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        colTel.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        colEmail.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
        colContact.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        table.getColumns().addAll(colNom, colAdresse, colTel, colEmail, colContact);


        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 12;";
        for (TableColumn<?, ?> col : table.getColumns()) {
            col.setStyle(headerStyle);
        }

        table.setRowFactory(tv -> new TableRow<Fournisseur>());

        fournisseursList.addAll(fc.getAllFournisseurs());
        table.setItems(fournisseursList);

        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPrefHeight(400);

        VBox tableContainer = new VBox(table);
        tableContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        TitledPane tpTable = new TitledPane("📋 Liste des Fournisseurs", tableContainer);
        tpTable.setExpanded(true);
        tpTable.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");

        VBox.setVgrow(tpTable, Priority.ALWAYS);

        btnAjouter.setOnAction(e -> ajouterFournisseur(txtNom, txtAdresse, txtTel, txtEmail, txtContact, table));
        btnModifier.setOnAction(e -> modifierFournisseur(table, txtNom, txtAdresse, txtTel, txtEmail, txtContact));
        btnSupprimer.setOnAction(e -> supprimerFournisseur(table, txtNom, txtAdresse, txtTel, txtEmail, txtContact));
        btnReset.setOnAction(e -> {
            txtRecherche.clear();
            refreshTable();
            clearFields(txtNom, txtAdresse, txtTel, txtEmail, txtContact);
            showSuccess("✅ Succès", "Liste réinitialisée");
        });

        txtRecherche.textProperty().addListener((obs, oldText, newText) -> {
            fournisseursList.setAll(fc.rechercherFournisseur(newText));
            if (newText != null && !newText.isEmpty() && fournisseursList.isEmpty()) {
                showInfo("ℹ️ Information", "Aucun fournisseur trouvé");
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNom.setText(newSel.getNomFournisseur());
                txtAdresse.setText(newSel.getAdresse());
                txtTel.setText(newSel.getTelephone());
                txtEmail.setText(newSel.getEmail());
                txtContact.setText(newSel.getContactPrincipal());
            }
        });

        VBox mainLayout = new VBox(15, tpForm, tpActions, tpSearch, tpTable);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: " + BACKGROUND_COLOR + ";");

        VBox.setVgrow(tpTable, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Scene scene = new Scene(scrollPane, 1100, 750);
        stage.setScene(scene);
        stage.show();
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        label.setStyle("-fx-text-fill: #4A5568;");
        return label;
    }

    private Button createStyledButton(String text, String gradient) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        btn.setStyle(
                "-fx-background-color: " + gradient + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 18 8 18;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + gradient + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 18 8 18;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);" +
                        "-fx-scale-x: 1.02;" +
                        "-fx-scale-y: 1.02;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + gradient + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 18 8 18;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);" +
                        "-fx-scale-x: 1;" +
                        "-fx-scale-y: 1;"
        ));

        return btn;
    }

    private void ajouterFournisseur(TextField txtNom, TextField txtAdresse, TextField txtTel,
                                    TextField txtEmail, TextField txtContact, TableView<Fournisseur> table) {
        if (txtNom.getText().isEmpty() || txtAdresse.getText().isEmpty()) {
            showAlert("❌ Erreur", "Nom et adresse obligatoires !");
            return;
        }

        Fournisseur f = new Fournisseur();
        f.setNomFournisseur(txtNom.getText());
        f.setAdresse(txtAdresse.getText());
        f.setTelephone(txtTel.getText());
        f.setEmail(txtEmail.getText());
        f.setContactPrincipal(txtContact.getText());

        if (fc.ajouterFournisseur(f)) {
            refreshTable();
            clearFields(txtNom, txtAdresse, txtTel, txtEmail, txtContact);
            showSuccess("✅ Succès", "Fournisseur ajouté avec succès !");
        } else {
            showAlert("❌ Erreur", "Erreur lors de l'ajout !");
        }
    }

    private void modifierFournisseur(TableView<Fournisseur> table, TextField txtNom, TextField txtAdresse,
                                     TextField txtTel, TextField txtEmail, TextField txtContact) {
        Fournisseur selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setNomFournisseur(txtNom.getText());
            selected.setAdresse(txtAdresse.getText());
            selected.setTelephone(txtTel.getText());
            selected.setEmail(txtEmail.getText());
            selected.setContactPrincipal(txtContact.getText());

            if (fc.modifierFournisseur(selected)) {
                refreshTable();
                clearFields(txtNom, txtAdresse, txtTel, txtEmail, txtContact);
                showSuccess("✅ Succès", "Fournisseur modifié avec succès !");
            } else {
                showAlert("❌ Erreur", "Erreur lors de la modification !");
            }
        } else {
            showWarning("⚠️ Attention", "Veuillez sélectionner un fournisseur à modifier");
        }
    }

    private void supprimerFournisseur(TableView<Fournisseur> table, TextField txtNom, TextField txtAdresse,
                                      TextField txtTel, TextField txtEmail, TextField txtContact) {
        Fournisseur selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer le fournisseur");
            confirm.setContentText("Êtes-vous sûr de vouloir supprimer ce fournisseur ?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (fc.supprimerFournisseur(selected.getIdFournisseur())) {
                    refreshTable();
                    clearFields(txtNom, txtAdresse, txtTel, txtEmail, txtContact);
                    showSuccess("✅ Succès", "Fournisseur supprimé avec succès !");
                } else {
                    showAlert("❌ Erreur", "Erreur lors de la suppression !");
                }
            }
        } else {
            showWarning("⚠️ Attention", "Veuillez sélectionner un fournisseur à supprimer");
        }
    }

    private void refreshTable() {
        fournisseursList.setAll(fc.getAllFournisseurs());
    }

    private void clearFields(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showSuccess(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showWarning(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}