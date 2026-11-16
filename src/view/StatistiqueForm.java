package view;

import controller.StatistiqueController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Statistique;

import java.time.LocalDate;

public class StatistiqueForm {

    private StatistiqueController sc = new StatistiqueController();


    private static final String GRADIENT_SUCCESS = "linear-gradient(to right, #4facfe, #00f2fe)";
    private static final String VIOLET_MAIN = "#667eea";
    private static final String BACKGROUND_COLOR = "#F9FAFB";

    public void afficher() {
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("Tableau de bord Statistiques");
        DatePicker dpDebut = new DatePicker();
        dpDebut.setPromptText("jj/mm/aaaa");
        dpDebut.setStyle("-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;");

        DatePicker dpFin = new DatePicker();
        dpFin.setPromptText("jj/mm/aaaa");
        dpFin.setStyle("-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;");

        Button btnGenerer = new Button("Générer");
        btnGenerer.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        btnGenerer.setStyle(
                "-fx-background-color: " + GRADIENT_SUCCESS + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 18 8 18;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        btnGenerer.setOnMouseEntered(e -> btnGenerer.setStyle(
                "-fx-background-color: linear-gradient(to right, #00f2fe, #4facfe);" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 18 8 18;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        ));
        btnGenerer.setOnMouseExited(e -> btnGenerer.setStyle(
                "-fx-background-color: " + GRADIENT_SUCCESS + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 18 8 18;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        ));

        VBox debutBox = new VBox(5, createStyledLabel("Date Début:"), dpDebut);
        VBox finBox = new VBox(5, createStyledLabel("Date Fin:"), dpFin);
        VBox btnBox = new VBox(5, new Label(" "), btnGenerer);

        HBox filtres = new HBox(20, debutBox, finBox, btnBox);
        filtres.setAlignment(Pos.CENTER_LEFT);
        filtres.setPadding(new Insets(15));
        filtres.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");


        TableView<Statistique> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent;");
        table.setPrefWidth(500);

        table.setRowFactory(tv -> new TableRow<Statistique>() {
            @Override
            protected void updateItem(Statistique item, boolean empty) {
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

        TableColumn<Statistique, String> colArticle = new TableColumn<>("Article");
        colArticle.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNomArticle()));
        colArticle.setPrefWidth(150);
        colArticle.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Statistique, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTypeArticle()));
        colType.setPrefWidth(100);
        colType.setStyle("-fx-alignment: CENTER;");

        TableColumn<Statistique, Integer> colQuantite = new TableColumn<>("Quantité totale");
        colQuantite.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getQuantiteTotale()));
        colQuantite.setPrefWidth(100);
        colQuantite.setStyle("-fx-alignment: CENTER;");

        TableColumn<Statistique, Integer> colNbServices = new TableColumn<>("Nb services");
        colNbServices.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getNbServices()));
        colNbServices.setPrefWidth(100);
        colNbServices.setStyle("-fx-alignment: CENTER;");

        TableColumn<Statistique, String> colService = new TableColumn<>("Vue");
        colService.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getService()));
        colService.setPrefWidth(150);
        colService.setStyle("-fx-alignment: CENTER-LEFT;");

        table.getColumns().addAll(colArticle, colType, colQuantite, colNbServices, colService);
        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 12;";
        for (TableColumn<?, ?> col : table.getColumns()) {
            col.setStyle(headerStyle);
        }
        ScrollPane tableScrollPane = new ScrollPane(table);
        tableScrollPane.setFitToWidth(true);
        tableScrollPane.setFitToHeight(true);
        tableScrollPane.setPrefHeight(400);
        tableScrollPane.setPrefWidth(550);
        tableScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tableScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tableScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Article");
        xAxis.setTickLabelFont(Font.font("Segoe UI", 12));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantité totale");
        yAxis.setTickLabelFont(Font.font("Segoe UI", 12));

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setPrefWidth(500);
        barChart.setStyle("-fx-background-color: transparent;");
        barChart.setPadding(new Insets(10));

        HBox contenu = new HBox(20, barChart, tableScrollPane);
        contenu.setStyle("-fx-padding: 10;");
        contenu.setAlignment(Pos.CENTER);
        HBox.setHgrow(barChart, Priority.ALWAYS);
        HBox.setHgrow(tableScrollPane, Priority.ALWAYS);

        VBox root = new VBox(15, filtres, contenu);
        root.setStyle("-fx-padding: 20; -fx-background-color: " + BACKGROUND_COLOR + ";");
        VBox.setVgrow(contenu, Priority.ALWAYS);

        btnGenerer.setOnAction(e -> {
            LocalDate debut = dpDebut.getValue();
            LocalDate fin = dpFin.getValue();

            if (debut == null || fin == null) {
                new Alert(Alert.AlertType.ERROR, "Sélectionner une période").showAndWait();
                return;
            }

            ObservableList<Statistique> topGlobal = sc.getProduitPlusConsommeGlobal(debut, fin);
            ObservableList<Statistique> topServices = sc.getProduitParNbServices(debut, fin);

            ObservableList<Statistique> data = FXCollections.observableArrayList();
            data.addAll(topGlobal);
            data.addAll(topServices);

            table.setItems(data);

            barChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Statistique s : data) {
                String nom = s.getNomArticle();
                if (nom.length() > 20) {
                    nom = nom.substring(0, 18) + "...";
                }
                series.getData().add(new XYChart.Data<>(nom, s.getQuantiteTotale()));
            }
            barChart.getData().add(series);
            barChart.lookupAll(".data0.chart-bar").forEach(node -> {
                node.setStyle("-fx-bar-fill: " + VIOLET_MAIN + ";");
            });
        });

        Scene scene = new Scene(root, 1200, 650);
        stage.setScene(scene);
        stage.show();
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        label.setStyle("-fx-text-fill: #4A5568;");
        return label;
    }
}