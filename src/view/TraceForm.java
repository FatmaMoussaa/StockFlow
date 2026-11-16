package view;

import controller.TraceController;
import model.Trace;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class TraceForm extends Application {

    private TraceController traceController = new TraceController();

    @Override
    public void start(Stage stage) {
        TableView<Trace> table = new TableView<>();
        TableColumn<Trace, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idTrace"));

        TableColumn<Trace, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        TableColumn<Trace, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateAction"));

        TableColumn<Trace, String> userCol = new TableColumn<>("Utilisateur");
        userCol.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));

        table.getColumns().addAll(idCol, actionCol, dateCol, userCol);

        List<Trace> traces = traceController.getAllTraces();
        ObservableList<Trace> data = FXCollections.observableArrayList(traces);
        table.setItems(data);

        VBox root = new VBox(table);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Consultation de la table Trace");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}