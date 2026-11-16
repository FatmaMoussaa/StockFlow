package view;

import controller.StockController;
import model.Stock;
import util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockForm {

    private ObservableList<Stock> stockList = FXCollections.observableArrayList();
    private StockController sc = new StockController();

    private static final String BACKGROUND_COLOR = "#F9FAFB";
    private static final String CARD_COLOR = "#FFFFFF";
    private static final String VIOLET_MAIN = "#667eea";

    public void afficher() {
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("✨ Consultation Stock - StockFlow ✨");
        TableView<Stock> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent;");

        TableColumn<Stock, String> colArticle = new TableColumn<>("Article");
        colArticle.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getArticle() != null ? data.getValue().getArticle().getReference() : "Inconnu"
                )
        );
        colArticle.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Stock, String> colLocal = new TableColumn<>("Local");
        colLocal.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getLocal() != null ? data.getValue().getLocal().getNomLocal() : "Inconnu"
                )
        );
        colLocal.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Stock, Integer> colQuant = new TableColumn<>("Quantité");
        colQuant.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getQuantiteActuelle()
                ).asObject()
        );
        colQuant.setStyle("-fx-alignment: CENTER;");
        colArticle.prefWidthProperty().bind(table.widthProperty().multiply(0.50));
        colLocal.prefWidthProperty().bind(table.widthProperty().multiply(0.30));
        colQuant.prefWidthProperty().bind(table.widthProperty().multiply(0.20));

        table.getColumns().addAll(colArticle, colLocal, colQuant);
        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 12;";
        for (TableColumn<?, ?> col : table.getColumns()) {
            col.setStyle(headerStyle);
        }
        table.setRowFactory(tv -> new TableRow<Stock>());
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox tableContainer = new VBox(table);
        tableContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        TitledPane tpTable = new TitledPane("📋 Stocks Disponibles", tableContainer);
        tpTable.setExpanded(true);
        tpTable.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");
        VBox.setVgrow(tpTable, Priority.ALWAYS);
        stockList.setAll(loadAllStocks());
        table.setItems(stockList);

        VBox mainLayout = new VBox(10, tpTable);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: " + BACKGROUND_COLOR + ";");
        VBox.setVgrow(tpTable, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        stage.setScene(new Scene(scrollPane, 900, 600));
        stage.show();
    }
    private List<Stock> loadAllStocks() {
        List<Stock> list = new ArrayList<>();

        String sql = "SELECT s.idStock, s.idArticle, s.idLocal, s.quantiteActuelle, " +
                "a.reference, l.nomLocal " +
                "FROM Stock s " +
                "JOIN Article a ON s.idArticle=a.idArticle " +
                "JOIN Local l ON s.idLocal=l.idLocal";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Stock s = new Stock();
                s.setIdStock(rs.getInt("idStock"));
                s.setQuantiteActuelle(rs.getInt("quantiteActuelle"));

                var a = new model.Article();
                a.setIdArticle(rs.getInt("idArticle"));
                a.setReference(rs.getString("reference"));
                s.setArticle(a);

                var l = new model.Local();
                l.setIdLocal(rs.getInt("idLocal"));
                l.setNomLocal(rs.getString("nomLocal"));
                s.setLocal(l);

                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}