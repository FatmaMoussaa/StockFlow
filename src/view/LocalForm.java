package view;

import controller.LocalController;
import model.Local;
import model.TypeLocal;
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

public class LocalForm {

    private LocalController lc = new LocalController();
    private ObservableList<Local> list = FXCollections.observableArrayList();

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
        stage.setTitle("✨ Gestion des Locaux - StockFlow ✨");
        TextField txtNom = new TextField();
        ComboBox<TypeLocal> comboType = new ComboBox<>();
        comboType.setItems(FXCollections.observableArrayList(TypeLocal.values()));
        comboType.getSelectionModel().selectFirst();
        TextField txtCapacite = new TextField();
        TextField txtAdresse = new TextField();
        String fieldStyle = "-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;";
        txtNom.setStyle(fieldStyle);
        comboType.setStyle(fieldStyle);
        txtCapacite.setStyle(fieldStyle);
        txtAdresse.setStyle(fieldStyle);

        txtNom.setPromptText("Ex: Salle A101");
        txtCapacite.setPromptText("Ex: 50");
        txtAdresse.setPromptText("Ex: 1er étage, aile A");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        formGrid.add(createStyledLabel("Nom Local :"), 0, 0);
        formGrid.add(txtNom, 1, 0);
        formGrid.add(createStyledLabel("Type :"), 0, 1);
        formGrid.add(comboType, 1, 1);
        formGrid.add(createStyledLabel("Capacité :"), 0, 2);
        formGrid.add(txtCapacite, 1, 2);
        formGrid.add(createStyledLabel("Adresse :"), 0, 3);
        formGrid.add(txtAdresse, 1, 3);

        TitledPane tpForm = new TitledPane("📝 Détails du Local", formGrid);
        tpForm.setExpanded(true);
        tpForm.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");
        Button btnAjouter = createStyledButton("➕ Ajouter", GRADIENT_SUCCESS);
        Button btnModifier = createStyledButton("✏️ Modifier", GRADIENT_PRIMARY);
        Button btnSupprimer = createStyledButton("🗑️ Supprimer", GRADIENT_DANGER);

        HBox actionButtons = new HBox(12, btnAjouter, btnModifier, btnSupprimer);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(5));

        TitledPane tpActions = new TitledPane("⚡ Actions", actionButtons);
        tpActions.setExpanded(true);
        tpActions.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        TextField txtRecherche = new TextField();
        txtRecherche.setStyle(fieldStyle);
        txtRecherche.setPromptText("Nom du local...");

        Button btnRecherche = createStyledButton("🔍 Rechercher", GRADIENT_INFO);
        Button btnReset = createStyledButton("🔄 Reset", GRADIENT_WARNING);

        HBox searchBox = new HBox(12, createStyledLabel("Recherche :"), txtRecherche, btnRecherche, btnReset);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10));

        VBox searchContainer = new VBox(searchBox);
        searchContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TitledPane tpSearch = new TitledPane("🔍 Recherche", searchContainer);
        tpSearch.setExpanded(true);
        tpSearch.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");

        TableView<Local> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent;");

        TableColumn<Local, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomLocal"));
        colNom.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Local, TypeLocal> colTypeCol = new TableColumn<>("Type");
        colTypeCol.setCellValueFactory(new PropertyValueFactory<>("typeLocal"));
        colTypeCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Local, Integer> colCapCol = new TableColumn<>("Capacité");
        colCapCol.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colCapCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Local, String> colAdrCol = new TableColumn<>("Adresse");
        colAdrCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colAdrCol.setStyle("-fx-alignment: CENTER-LEFT;");

        colNom.prefWidthProperty().bind(table.widthProperty().multiply(0.30));
        colTypeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
        colCapCol.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
        colAdrCol.prefWidthProperty().bind(table.widthProperty().multiply(0.30));

        table.getColumns().addAll(colNom, colTypeCol, colCapCol, colAdrCol);


        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 12;";
        for (TableColumn<?, ?> col : table.getColumns()) {
            col.setStyle(headerStyle);
        }


        table.setRowFactory(tv -> new TableRow<Local>());

        list.addAll(lc.getAllLocaux());
        table.setItems(list);


        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPrefHeight(400);

        VBox tableContainer = new VBox(table);
        tableContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        TitledPane tpTable = new TitledPane("📋 Liste des Locaux", tableContainer);
        tpTable.setExpanded(true);
        tpTable.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");
        VBox.setVgrow(tpTable, Priority.ALWAYS);

        btnAjouter.setOnAction(e -> ajouterLocal(txtNom, comboType, txtCapacite, txtAdresse, table));
        btnModifier.setOnAction(e -> modifierLocal(table, txtNom, comboType, txtCapacite, txtAdresse));
        btnSupprimer.setOnAction(e -> supprimerLocal(table, txtNom, comboType, txtCapacite, txtAdresse));
        btnRecherche.setOnAction(e -> {
            list.setAll(lc.rechercherLocal(txtRecherche.getText()));
            if (list.isEmpty()) {
                showInfo("ℹ️ Information", "Aucun local trouvé");
            }
        });
        btnReset.setOnAction(e -> {
            txtRecherche.clear();
            refreshTable();
            clearFields(txtNom, txtCapacite, txtAdresse);
            comboType.getSelectionModel().selectFirst();
            showSuccess("✅ Succès", "Liste réinitialisée");
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNom.setText(newSel.getNomLocal());
                comboType.getSelectionModel().select(newSel.getTypeLocal());
                txtCapacite.setText(String.valueOf(newSel.getCapacite()));
                txtAdresse.setText(newSel.getAdresse());
            }
        });
        VBox mainLayout = new VBox(15, tpForm, tpActions, tpSearch, tpTable);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: " + BACKGROUND_COLOR + ";");
        VBox.setVgrow(tpTable, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Scene scene = new Scene(scrollPane, 950, 750);
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

    private void ajouterLocal(TextField txtNom, ComboBox<TypeLocal> comboType,
                              TextField txtCapacite, TextField txtAdresse, TableView<Local> table) {
        try {
            if (txtNom.getText().isEmpty() || txtCapacite.getText().isEmpty() || txtAdresse.getText().isEmpty()) {
                showAlert("❌ Erreur", "Tous les champs sont obligatoires !");
                return;
            }
            int cap;
            try {
                cap = Integer.parseInt(txtCapacite.getText());
            } catch (NumberFormatException ex) {
                showAlert("❌ Erreur", "Capacité invalide !");
                return;
            }

            Local l = new Local();
            l.setNomLocal(txtNom.getText());
            l.setTypeLocal(comboType.getSelectionModel().getSelectedItem());
            l.setCapacite(cap);
            l.setAdresse(txtAdresse.getText());

            if (lc.ajouterLocal(l)) {
                refreshTable();
                clearFields(txtNom, txtCapacite, txtAdresse);
                comboType.getSelectionModel().selectFirst();
                showSuccess("✅ Succès", "Local ajouté avec succès !");
            } else {
                showAlert("❌ Erreur", "Erreur lors de l'ajout !");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("❌ Erreur", "Une erreur est survenue !");
        }
    }

    private void modifierLocal(TableView<Local> table, TextField txtNom, ComboBox<TypeLocal> comboType,
                               TextField txtCapacite, TextField txtAdresse) {
        Local selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                if (txtNom.getText().isEmpty() || txtCapacite.getText().isEmpty() || txtAdresse.getText().isEmpty()) {
                    showAlert("❌ Erreur", "Tous les champs sont obligatoires !");
                    return;
                }
                int cap;
                try {
                    cap = Integer.parseInt(txtCapacite.getText());
                } catch (NumberFormatException ex) {
                    showAlert("❌ Erreur", "Capacité invalide !");
                    return;
                }

                selected.setNomLocal(txtNom.getText());
                selected.setTypeLocal(comboType.getSelectionModel().getSelectedItem());
                selected.setCapacite(cap);
                selected.setAdresse(txtAdresse.getText());

                if (lc.modifierLocal(selected)) {
                    refreshTable();
                    clearFields(txtNom, txtCapacite, txtAdresse);
                    comboType.getSelectionModel().selectFirst();
                    showSuccess("✅ Succès", "Local modifié avec succès !");
                } else {
                    showAlert("❌ Erreur", "Erreur lors de la modification !");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("❌ Erreur", "Une erreur est survenue !");
            }
        } else {
            showWarning("⚠️ Attention", "Veuillez sélectionner un local à modifier");
        }
    }

    private void supprimerLocal(TableView<Local> table, TextField txtNom, ComboBox<TypeLocal> comboType,
                                TextField txtCapacite, TextField txtAdresse) {
        Local selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer le local");
            confirm.setContentText("Êtes-vous sûr de vouloir supprimer ce local ?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (lc.supprimerLocal(selected.getIdLocal())) {
                    refreshTable();
                    clearFields(txtNom, txtCapacite, txtAdresse);
                    comboType.getSelectionModel().selectFirst();
                    showSuccess("✅ Succès", "Local supprimé avec succès !");
                } else {
                    showAlert("❌ Erreur", "Erreur lors de la suppression !");
                }
            }
        } else {
            showWarning("⚠️ Attention", "Veuillez sélectionner un local à supprimer");
        }
    }

    private void refreshTable() {
        list.setAll(lc.getAllLocaux());
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