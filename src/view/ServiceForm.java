package view;

import controller.ServiceController;
import model.Service;
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

public class ServiceForm {

    private ServiceController sc = new ServiceController();
    private ObservableList<Service> servicesList = FXCollections.observableArrayList();
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
        stage.setTitle("✨ Gestion des Services - StockFlow ✨");
        TextField txtNom = new TextField();
        TextField txtType = new TextField();
        TextField txtResp = new TextField();
        String fieldStyle = "-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;";
        txtNom.setStyle(fieldStyle);
        txtType.setStyle(fieldStyle);
        txtResp.setStyle(fieldStyle);

        txtNom.setPromptText("Ex: Service Informatique");
        txtType.setPromptText("Ex: Technique");
        txtResp.setPromptText("Ex: Jean Dupont");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        formGrid.add(createStyledLabel("Nom :"), 0, 0);
        formGrid.add(txtNom, 1, 0);
        formGrid.add(createStyledLabel("Type :"), 0, 1);
        formGrid.add(txtType, 1, 1);
        formGrid.add(createStyledLabel("Responsable :"), 0, 2);
        formGrid.add(txtResp, 1, 2);

        TitledPane tpForm = new TitledPane("📝 Détails du Service", formGrid);
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
        txtRecherche.setPromptText("Nom du service...");

        HBox searchBox = new HBox(12, createStyledLabel("Recherche :"), txtRecherche);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10));

        VBox searchContainer = new VBox(searchBox);
        searchContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TitledPane tpSearch = new TitledPane("🔍 Recherche Service", searchContainer);
        tpSearch.setExpanded(true);
        tpSearch.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");

        TableView<Service> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent;");

        TableColumn<Service, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomService"));
        colNom.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Service, String> colTypeCol = new TableColumn<>("Type");
        colTypeCol.setCellValueFactory(new PropertyValueFactory<>("typeService"));
        colTypeCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Service, String> colRespCol = new TableColumn<>("Responsable");
        colRespCol.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        colRespCol.setStyle("-fx-alignment: CENTER-LEFT;");


        colNom.prefWidthProperty().bind(table.widthProperty().multiply(0.40));
        colTypeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        colRespCol.prefWidthProperty().bind(table.widthProperty().multiply(0.35));

        table.getColumns().addAll(colNom, colTypeCol, colRespCol);


        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 12;";
        for (TableColumn<?, ?> col : table.getColumns()) {
            col.setStyle(headerStyle);
        }
        table.setRowFactory(tv -> new TableRow<Service>());

        servicesList.addAll(sc.getAllServices());
        table.setItems(servicesList);

        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPrefHeight(400);

        VBox tableContainer = new VBox(table);
        tableContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        TitledPane tpTable = new TitledPane("📋 Liste des Services", tableContainer);
        tpTable.setExpanded(true);
        tpTable.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");
        VBox.setVgrow(tpTable, Priority.ALWAYS);
        btnAjouter.setOnAction(e -> ajouterService(txtNom, txtType, txtResp, txtRecherche, table));
        btnModifier.setOnAction(e -> modifierService(table, txtNom, txtType, txtResp, txtRecherche));
        btnSupprimer.setOnAction(e -> supprimerService(table, txtNom, txtType, txtResp, txtRecherche));
        btnReset.setOnAction(e -> {
            clearFields(txtNom, txtType, txtResp, txtRecherche);
            refreshTable();
            showSuccess("✅ Succès", "Liste réinitialisée");
        });

        txtRecherche.textProperty().addListener((obs, oldText, newText) -> {
            servicesList.setAll(sc.rechercherService(newText));
            if (newText != null && !newText.isEmpty() && servicesList.isEmpty()) {
                showInfo("ℹ️ Information", "Aucun service trouvé");
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNom.setText(newSel.getNomService());
                txtType.setText(newSel.getTypeService());
                txtResp.setText(newSel.getResponsable());
            }
        });
        VBox mainLayout = new VBox(15, tpForm, tpActions, tpSearch, tpTable);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: " + BACKGROUND_COLOR + ";");
        VBox.setVgrow(tpTable, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Scene scene = new Scene(scrollPane, 900, 700);
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

    private void ajouterService(TextField txtNom, TextField txtType, TextField txtResp,
                                TextField txtRecherche, TableView<Service> table) {
        if (txtNom.getText().isEmpty()) {
            showAlert("❌ Erreur", "Le nom du service est obligatoire !");
            return;
        }

        Service s = new Service(txtNom.getText(), txtType.getText(), txtResp.getText());
        if (sc.ajouterService(s)) {
            refreshTable();
            clearFields(txtNom, txtType, txtResp, txtRecherche);
            showSuccess("✅ Succès", "Service ajouté avec succès !");
        } else {
            showAlert("❌ Erreur", "Erreur lors de l'ajout !");
        }
    }

    private void modifierService(TableView<Service> table, TextField txtNom, TextField txtType,
                                 TextField txtResp, TextField txtRecherche) {
        Service selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setNomService(txtNom.getText());
            selected.setTypeService(txtType.getText());
            selected.setResponsable(txtResp.getText());

            if (sc.modifierService(selected)) {
                refreshTable();
                clearFields(txtNom, txtType, txtResp, txtRecherche);
                showSuccess("✅ Succès", "Service modifié avec succès !");
            } else {
                showAlert("❌ Erreur", "Erreur lors de la modification !");
            }
        } else {
            showWarning("⚠️ Attention", "Veuillez sélectionner un service à modifier");
        }
    }

    private void supprimerService(TableView<Service> table, TextField txtNom, TextField txtType,
                                  TextField txtResp, TextField txtRecherche) {
        Service selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer le service");
            confirm.setContentText("Êtes-vous sûr de vouloir supprimer ce service ?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (sc.supprimerService(selected.getIdService())) {
                    refreshTable();
                    clearFields(txtNom, txtType, txtResp, txtRecherche);
                    showSuccess("✅ Succès", "Service supprimé avec succès !");
                } else {
                    showAlert("❌ Erreur", "Erreur lors de la suppression !");
                }
            }
        } else {
            showWarning("⚠️ Attention", "Veuillez sélectionner un service à supprimer");
        }
    }

    private void refreshTable() {
        servicesList.setAll(sc.getAllServices());
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