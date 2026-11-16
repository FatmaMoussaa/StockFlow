package view;

import controller.InventaireController;
import controller.TraceController;
import model.*;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InventaireForm {

    private InventaireController inventaireController = new InventaireController();
    private TraceController traceController = new TraceController();

    private ObservableList<Stock> stockList = FXCollections.observableArrayList();
    private ObservableList<LigneInventaire> lignesList = FXCollections.observableArrayList();

    private List<Article> articles;
    private List<Local> locaux;


    private static final String GRADIENT_PRIMARY = "linear-gradient(to right, #667eea, #764ba2)";
    private static final String GRADIENT_SUCCESS = "linear-gradient(to right, #4facfe, #00f2fe)";
    private static final String GRADIENT_DANGER = "linear-gradient(to right, #fa709a, #fee140)";
    private static final String GRADIENT_WARNING = "linear-gradient(to right, #f093fb, #f5576c)";
    private static final String GRADIENT_INFO = "linear-gradient(to right, #4ECDC4, #44A08D)";
    private static final String BACKGROUND_COLOR = "#F9FAFB";
    private static final String CARD_COLOR = "#FFFFFF";
    private static final String VIOLET_MAIN = "#667eea";

    public InventaireForm(List<Article> articles, List<Local> locaux, List<Stock> stocks) {
        this.articles = articles;
        this.locaux = locaux;
        this.stockList.addAll(stocks);
    }

    public void afficher() {
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("✨ Gestion Inventaire - StockFlow ✨");

        String fieldStyle = "-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;";


        ComboBox<Stock> cbStock = new ComboBox<>(stockList);
        cbStock.setPromptText("Sélectionner un stock");
        cbStock.setStyle(fieldStyle);


        cbStock.setConverter(new javafx.util.StringConverter<Stock>() {
            @Override
            public String toString(Stock stock) {
                if (stock == null) return "";
                String articleRef = stock.getArticle() != null ? stock.getArticle().getReference() : "Inconnu";
                String localNom = stock.getLocal() != null ? stock.getLocal().getNomLocal() : "Inconnu";
                return articleRef + " - " + localNom;
            }
            @Override
            public Stock fromString(String string) {
                return null;
            }
        });

        TextField txtQuantite = new TextField();
        txtQuantite.setPromptText("Quantité relevée");
        txtQuantite.setStyle(fieldStyle);

        Button btnAjouterLigne = createSmallButton("➕ Ajouter Ligne", GRADIENT_SUCCESS);
        Button btnSupprimerLigne = createSmallButton("🗑️ Supprimer Ligne", GRADIENT_DANGER);

        HBox ajoutLigneBox = new HBox(12, cbStock, txtQuantite, btnAjouterLigne, btnSupprimerLigne);
        ajoutLigneBox.setAlignment(Pos.CENTER_LEFT);
        ajoutLigneBox.setPadding(new Insets(15));
        ajoutLigneBox.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        HBox.setHgrow(cbStock, Priority.ALWAYS);
        HBox.setHgrow(txtQuantite, Priority.ALWAYS);

        TitledPane tpAjout = new TitledPane("📝 Ajouter Ligne Inventaire", ajoutLigneBox);
        tpAjout.setExpanded(true);
        tpAjout.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        TableView<LigneInventaire> tableLignes = new TableView<>();
        tableLignes.setStyle("-fx-background-color: transparent;");

        TableColumn<LigneInventaire, String> colLigneArticle = new TableColumn<>("Article");
        colLigneArticle.setCellValueFactory(cell -> {
            Article a = articles.stream()
                    .filter(ar -> ar.getIdArticle() == cell.getValue().getIdArticle())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(a != null ? a.getReference() : "Inconnu");
        });
        colLigneArticle.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<LigneInventaire, String> colLigneLocal = new TableColumn<>("Local");
        colLigneLocal.setCellValueFactory(cell -> {
            Local l = locaux.stream()
                    .filter(loc -> loc.getIdLocal() == cell.getValue().getIdLocal())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(l != null ? l.getNomLocal() : "Inconnu");
        });
        colLigneLocal.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<LigneInventaire, Integer> colLigneQuantite = new TableColumn<>("Quantité Relevée");
        colLigneQuantite.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantite()));
        colLigneQuantite.setStyle("-fx-alignment: CENTER;");


        colLigneArticle.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.45));
        colLigneLocal.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.35));
        colLigneQuantite.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.20));

        tableLignes.getColumns().addAll(colLigneArticle, colLigneLocal, colLigneQuantite);
        tableLignes.setItems(lignesList);


        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 12;";
        for (TableColumn<?, ?> col : tableLignes.getColumns()) {
            col.setStyle(headerStyle);
        }


        tableLignes.setRowFactory(tv -> new TableRow<LigneInventaire>() {
            @Override
            protected void updateItem(LigneInventaire item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #F8FAFC;");
                    } else {
                        setStyle("-fx-background-color: " + CARD_COLOR + ";");
                    }
                }
            }
        });

        VBox.setVgrow(tableLignes, Priority.ALWAYS);

        VBox lignesContainer = new VBox(tableLignes);
        lignesContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        VBox.setVgrow(lignesContainer, Priority.ALWAYS);

        TitledPane tpLignes = new TitledPane("📋 Lignes de l'Inventaire", lignesContainer);
        tpLignes.setExpanded(true);
        tpLignes.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        TableView<Stock> tableStock = new TableView<>();
        tableStock.setStyle("-fx-background-color: transparent;");

        TableColumn<Stock, String> colStockArticle = new TableColumn<>("Article");
        colStockArticle.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getArticle() != null ? cell.getValue().getArticle().getReference() : "Inconnu"));
        colStockArticle.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Stock, String> colStockLocal = new TableColumn<>("Local");
        colStockLocal.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getLocal() != null ? cell.getValue().getLocal().getNomLocal() : "Inconnu"));
        colStockLocal.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Stock, Integer> colStockQuantite = new TableColumn<>("Quantité Actuelle");
        colStockQuantite.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantiteActuelle()));
        colStockQuantite.setStyle("-fx-alignment: CENTER;");


        colStockQuantite.setCellFactory(column -> new TableCell<Stock, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.valueOf(item));
                    if (item == 0) {
                        setStyle("-fx-text-fill: #f5576c; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    }
                    setAlignment(Pos.CENTER);
                }
            }
        });

        colStockArticle.prefWidthProperty().bind(tableStock.widthProperty().multiply(0.45));
        colStockLocal.prefWidthProperty().bind(tableStock.widthProperty().multiply(0.35));
        colStockQuantite.prefWidthProperty().bind(tableStock.widthProperty().multiply(0.20));

        tableStock.getColumns().addAll(colStockArticle, colStockLocal, colStockQuantite);
        tableStock.setItems(stockList);

        for (TableColumn<?, ?> col : tableStock.getColumns()) {
            col.setStyle(headerStyle);
        }

        tableStock.setRowFactory(tv -> new TableRow<Stock>() {
            @Override
            protected void updateItem(Stock item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #F8FAFC;");
                    } else {
                        setStyle("-fx-background-color: " + CARD_COLOR + ";");
                    }
                }
            }
        });

        VBox.setVgrow(tableStock, Priority.ALWAYS);

        VBox stockContainer = new VBox(tableStock);
        stockContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        VBox.setVgrow(stockContainer, Priority.ALWAYS);

        TitledPane tpStock = new TitledPane("📦 Stocks Actuels", stockContainer);
        tpStock.setExpanded(true);
        tpStock.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        ComboBox<Local> cbLocalInventaire = new ComboBox<>(FXCollections.observableArrayList(locaux));
        cbLocalInventaire.setPromptText("Sélectionner un local");
        cbLocalInventaire.setStyle(fieldStyle);

        Button btnChargerLocalInventaire = createStyledButton("📂 Charger articles du local", GRADIENT_INFO);
        btnChargerLocalInventaire.setOnAction(e -> {
            Local selectedLocal = cbLocalInventaire.getValue();
            if (selectedLocal != null) {
                lignesList.clear();
                for (Stock s : stockList) {
                    if (s.getLocal().getIdLocal() == selectedLocal.getIdLocal()) {
                        LigneInventaire ligne = new LigneInventaire();
                        ligne.setIdArticle(s.getArticle().getIdArticle());
                        ligne.setIdLocal(selectedLocal.getIdLocal());
                        ligne.setQuantite(s.getQuantiteActuelle());
                        lignesList.add(ligne);
                    }
                }
                int nbArticles = lignesList.size();
                int qteTotale = lignesList.stream().mapToInt(LigneInventaire::getQuantite).sum();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("📊 Résultat");
                alert.setHeaderText("Local : " + selectedLocal.getNomLocal());
                alert.setContentText("Articles différents : " + nbArticles + "\nQuantité totale : " + qteTotale);
                alert.showAndWait();
            } else {
                showWarning("⚠️ Attention", "Veuillez sélectionner un local");
            }
        });

        HBox localBoxInventaire = new HBox(12, cbLocalInventaire, btnChargerLocalInventaire);
        localBoxInventaire.setAlignment(Pos.CENTER_LEFT);
        localBoxInventaire.setPadding(new Insets(15));
        localBoxInventaire.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        HBox.setHgrow(cbLocalInventaire, Priority.ALWAYS);

        TitledPane tpLocalInventaire = new TitledPane("🏢 Inventaire par Local", localBoxInventaire);
        tpLocalInventaire.setExpanded(true);
        tpLocalInventaire.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        Button btnValider = createStyledButton("✅ Valider Inventaire", GRADIENT_SUCCESS);
        btnValider.setOnAction(e -> validerInventaire());

        VBox actionsContainer = new VBox(btnValider);
        actionsContainer.setAlignment(Pos.CENTER);
        actionsContainer.setPadding(new Insets(15));
        actionsContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TitledPane tpActions = new TitledPane("⚡ Actions Inventaire", actionsContainer);
        tpActions.setExpanded(true);
        tpActions.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        btnAjouterLigne.setOnAction(e -> ajouterLigne(cbStock, txtQuantite));

        btnSupprimerLigne.setOnAction(e -> {
            LigneInventaire selected = tableLignes.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Voulez-vous vraiment supprimer cette ligne ?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait();
                if (confirm.getResult() == ButtonType.YES) {
                    lignesList.remove(selected);
                    showSuccess("✅ Succès", "Ligne supprimée avec succès");
                }
            } else {
                showWarning("⚠️ Attention", "Sélectionner une ligne à supprimer");
            }
        });


        VBox mainLayout = new VBox(15, tpAjout, tpLignes, tpStock, tpLocalInventaire, tpActions);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: " + BACKGROUND_COLOR + ";");

        VBox.setVgrow(tpLignes, Priority.ALWAYS);
        VBox.setVgrow(tpStock, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Scene scene = new Scene(scrollPane, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }


    private void ajouterLigne(ComboBox<Stock> cbStock, TextField txtQuantite) {
        Stock selected = cbStock.getValue();
        if (selected != null && !txtQuantite.getText().isEmpty()) {
            try {
                int qte = Integer.parseInt(txtQuantite.getText());
                if (qte < 0) throw new Exception("Quantité invalide");
                LigneInventaire ligne = new LigneInventaire();
                ligne.setIdArticle(selected.getArticle().getIdArticle());
                ligne.setIdLocal(selected.getLocal().getIdLocal());
                ligne.setQuantite(qte);
                lignesList.add(ligne);

                txtQuantite.clear();
                cbStock.setValue(null);
                showSuccess("✅ Succès", "Ligne ajoutée avec succès");
            } catch (Exception ex) {
                showAlert("❌ Erreur", ex.getMessage());
            }
        } else {
            showWarning("⚠️ Attention", "Veuillez sélectionner un stock et saisir une quantité");
        }
    }

    private void validerInventaire() {
        if (!lignesList.isEmpty()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Voulez-vous vraiment valider cet inventaire ?\nLes stocks seront ajustés automatiquement.",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                Inventaire inv = new Inventaire();
                inv.setStatut(StatutInventaire.EN_COURS);
                inv.setDateInventaire(new Date());
                inv.setLignes(new ArrayList<>(lignesList));

                try {
                    if (inventaireController.ajouterInventaire(inv)) {
                        showSuccess("✅ Succès", "Inventaire enregistré et stocks ajustés avec succès");
                        lignesList.clear();
                    } else {
                        showAlert("❌ Erreur", "Erreur lors de l'enregistrement");
                    }
                } catch (Exception ex) {
                    showAlert("❌ Erreur", "Erreur : " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            showWarning("⚠️ Attention", "Aucune ligne d'inventaire à valider");
        }
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
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + gradient + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);" +
                        "-fx-scale-x: 1.02;" +
                        "-fx-scale-y: 1.02;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + gradient + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);" +
                        "-fx-scale-x: 1;" +
                        "-fx-scale-y: 1;"
        ));

        return btn;
    }

    private Button createSmallButton(String text, String gradient) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        btn.setStyle(
                "-fx-background-color: " + gradient + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 6 14 6 14;" +
                        "-fx-background-radius: 6;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 4, 0, 0, 1);"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + gradient + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 6 14 6 14;" +
                        "-fx-background-radius: 6;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + gradient + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 6 14 6 14;" +
                        "-fx-background-radius: 6;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 4, 0, 0, 1);"
        ));

        return btn;
    }

    private void showSuccess(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
}