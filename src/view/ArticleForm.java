package view;

import controller.ArticleController;
import model.Article;
import model.Local;
import model.TypeArticle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.TypeMouvement;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleForm {

    private ArticleController ac = new ArticleController();
    private ObservableList<Article> articlesList = FXCollections.observableArrayList();
    private Map<Integer, String> mapLocaux = new HashMap<>();


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
        stage.setTitle("Gestion des Articles - ISIMM");

        List<Local> locaux = ac.getAllLocaux();
        for (Local l : locaux) {
            mapLocaux.put(l.getIdLocal(), l.getNomLocal());
        }



        TextField txtDesignation = new TextField();
        TextField txtReference = new TextField();
        TextField txtDescription = new TextField();
        TextField txtCodeBarres = new TextField();
        TextField txtStockMinimal = new TextField();
        DatePicker datePicker = new DatePicker();
        CheckBox chkCritique = new CheckBox("Est Critique");
        chkCritique.setStyle("-fx-font-weight: bold; -fx-text-fill: #f5576c;");

        ComboBox<TypeArticle> comboType = new ComboBox<>();
        comboType.getItems().addAll(TypeArticle.values());
        comboType.setPromptText("Sélectionner un type");

        ComboBox<Local> comboLocal = new ComboBox<>();
        comboLocal.setItems(FXCollections.observableArrayList(locaux));
        comboLocal.setConverter(new javafx.util.StringConverter<Local>() {
            @Override
            public String toString(Local local) {
                return local != null ? local.getNomLocal() : "";
            }
            @Override
            public Local fromString(String string) {
                return null;
            }
        });
        comboLocal.setPromptText("Sélectionner un local");


        String fieldStyle = "-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;";
        txtReference.setStyle(fieldStyle);
        txtDesignation.setStyle(fieldStyle);
        txtDescription.setStyle(fieldStyle);
        txtCodeBarres.setStyle(fieldStyle);
        txtStockMinimal.setStyle(fieldStyle);
        datePicker.setStyle(fieldStyle);
        comboType.setStyle(fieldStyle);
        comboLocal.setStyle(fieldStyle);

        txtReference.setPromptText("Ex: Laptop Dell");
        txtDesignation.setPromptText("Ex: REF-001");
        txtDescription.setPromptText("Description...");
        txtCodeBarres.setPromptText("Ex: 1234567890123");
        txtStockMinimal.setPromptText("Ex: 10");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        // Labels avec la couleur du Main
        formGrid.add(createLabel("Désignation:"), 0, 0);
        formGrid.add(txtReference, 1, 0);
        formGrid.add(createLabel("Réference :"), 0, 1);
        formGrid.add(txtDesignation, 1, 1);
        formGrid.add(createLabel("Description :"), 0, 2);
        formGrid.add(txtDescription, 1, 2);
        formGrid.add(createLabel("Code-barres :"), 0, 3);
        formGrid.add(txtCodeBarres, 1, 3);
        formGrid.add(createLabel("Stock minimal :"), 0, 4);
        formGrid.add(txtStockMinimal, 1, 4);
        formGrid.add(createLabel("Type :"), 0, 5);
        formGrid.add(comboType, 1, 5);
        formGrid.add(createLabel("Date péremption :"), 0, 6);
        formGrid.add(datePicker, 1, 6);
        formGrid.add(createLabel("Local :"), 0, 7);
        formGrid.add(comboLocal, 1, 7);
        formGrid.add(chkCritique, 1, 8);

        TitledPane tpForm = new TitledPane("Détails de l'article", formGrid);
        tpForm.setExpanded(true);
        tpForm.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        Button btnAjouter = createMainButton("Ajouter", GRADIENT_SUCCESS);
        Button btnModifier = createMainButton("Modifier", GRADIENT_PRIMARY);
        Button btnSupprimer = createMainButton("Supprimer", GRADIENT_DANGER);
        Button btnRechercher = createMainButton("Rechercher", GRADIENT_INFO);
        Button btnReset = createMainButton("Reset", GRADIENT_WARNING);
        Button btnAlerte = createMainButton("Alerte péremption", GRADIENT_WARNING);
        Button btnAlerteStock = createMainButton("Alerte Stock", GRADIENT_SUCCESS);
        Button btnRechercherMvt = createMainButton("Recherche Mouvement", GRADIENT_PRIMARY);
        Button btnCritique = createMainButton("Consommables Critiques", GRADIENT_DANGER);

        HBox actionButtons1 = new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnReset);
        actionButtons1.setAlignment(Pos.CENTER);
        HBox actionButtons2 = new HBox(10, btnRechercher, btnAlerte, btnAlerteStock);
        actionButtons2.setAlignment(Pos.CENTER);
        HBox actionButtons3 = new HBox(10, btnRechercherMvt, btnCritique);
        actionButtons3.setAlignment(Pos.CENTER);

        VBox actionsVBox = new VBox(10, actionButtons1, actionButtons2, actionButtons3);
        actionsVBox.setPadding(new Insets(15));
        actionsVBox.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TitledPane tpActions = new TitledPane("Actions", actionsVBox);
        tpActions.setExpanded(true);
        tpActions.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        TableView<Article> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent;");

        TableColumn<Article, String> colRef = new TableColumn<>("réfernce");
        colRef.setCellValueFactory(new PropertyValueFactory<>("designation"));
        TableColumn<Article, String> colDes = new TableColumn<>("Désignation");
        colDes.setCellValueFactory(new PropertyValueFactory<>("reference"));
        TableColumn<Article, String> colCB = new TableColumn<>("Code-barres");
        colCB.setCellValueFactory(new PropertyValueFactory<>("codeBarres"));
        TableColumn<Article, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Article, TypeArticle> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Article, Integer> colStockMin = new TableColumn<>("Stock min");
        colStockMin.setCellValueFactory(new PropertyValueFactory<>("stockMinimal"));
        TableColumn<Article, Boolean> colCritiqueCol = new TableColumn<>("Critique");
        colCritiqueCol.setCellValueFactory(new PropertyValueFactory<>("estCritique"));
        TableColumn<Article, Date> colDate = new TableColumn<>("Date péremption");
        colDate.setCellValueFactory(new PropertyValueFactory<>("datePeremption"));

        TableColumn<Article, String> colLocalNom = new TableColumn<>("Local");
        colLocalNom.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            Integer idLocal = article.getIdlocal();
            String nomLocal = idLocal != null ? mapLocaux.get(idLocal) : "";
            return new SimpleStringProperty(nomLocal);
        });

        table.getColumns().addAll(colRef, colDes, colCB, colDesc, colType, colStockMin,
                colCritiqueCol, colDate, colLocalNom);


        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10;";
        for (TableColumn<?, ?> col : table.getColumns()) {
            col.setStyle(headerStyle);
        }


        table.setRowFactory(tv -> new TableRow<Article>());

        articlesList.addAll(ac.getAllArticles());
        table.setItems(articlesList);
        VBox.setVgrow(table, Priority.ALWAYS);

        TitledPane tpTable = new TitledPane("Liste des articles", table);
        tpTable.setExpanded(true);
        tpTable.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        btnAjouter.setOnAction(e -> {
            try {
                Article a = new Article();
                a.setReference(txtReference.getText());
                a.setDesignation(txtDesignation.getText());
                a.setDescription(txtDescription.getText());
                a.setCodeBarres(txtCodeBarres.getText());
                a.setStockMinimal(Integer.parseInt(txtStockMinimal.getText()));
                a.setType(comboType.getValue());
                if (datePicker.getValue() != null)
                    a.setDatePeremption(Date.valueOf(datePicker.getValue()));
                a.setEstCritique(chkCritique.isSelected());

                Local selectedLocal = comboLocal.getValue();
                if (selectedLocal != null) {
                    a.setIdlocal(selectedLocal.getIdLocal());
                }

                if (ac.ajouterArticle(a)) {
                    articlesList.add(a);
                    clearFields(txtReference, txtDesignation, txtDescription, txtCodeBarres,
                            txtStockMinimal, comboType, datePicker, chkCritique, comboLocal);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erreur", "Veuillez vérifier les données saisies");
            }
        });

        btnModifier.setOnAction(e -> {
            Article selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setReference(txtReference.getText());
                selected.setDesignation(txtDesignation.getText());
                selected.setDescription(txtDescription.getText());
                selected.setCodeBarres(txtCodeBarres.getText());
                selected.setStockMinimal(Integer.parseInt(txtStockMinimal.getText()));
                selected.setType(comboType.getValue());
                if (datePicker.getValue() != null)
                    selected.setDatePeremption(Date.valueOf(datePicker.getValue()));
                selected.setEstCritique(chkCritique.isSelected());

                Local selectedLocal = comboLocal.getValue();
                if (selectedLocal != null) {
                    selected.setIdlocal(selectedLocal.getIdLocal());
                } else {
                    selected.setIdlocal(null);
                }

                if (ac.modifierArticle(selected)) {
                    int index = articlesList.indexOf(selected);
                    articlesList.set(index, selected);
                    table.getSelectionModel().clearSelection();
                    clearFields(txtReference, txtDesignation, txtDescription, txtCodeBarres,
                            txtStockMinimal, comboType, datePicker, chkCritique, comboLocal);
                }
            }
        });

        btnSupprimer.setOnAction(e -> {
            Article selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && ac.supprimerArticle(selected.getIdArticle())) {
                articlesList.remove(selected);
                clearFields(txtReference, txtDesignation, txtDescription, txtCodeBarres,
                        txtStockMinimal, comboType, datePicker, chkCritique, comboLocal);
            }
        });

        btnRechercher.setOnAction(e -> {
            Dialog<List<Article>> dialog = new Dialog<>();
            dialog.setTitle("Recherche avancée");

            TextField txtMotCle = new TextField();
            txtMotCle.setStyle(fieldStyle);
            DatePicker dpDate = new DatePicker();
            dpDate.setStyle(fieldStyle);
            ComboBox<TypeArticle> cbType = new ComboBox<>();
            cbType.getItems().addAll(TypeArticle.values());
            cbType.setStyle(fieldStyle);
            ComboBox<Local> cbLocal = new ComboBox<>();
            cbLocal.setItems(FXCollections.observableArrayList(ac.getAllLocaux()));
            cbLocal.setStyle(fieldStyle);
            cbLocal.setConverter(new javafx.util.StringConverter<Local>() {
                @Override
                public String toString(Local local) {
                    return local != null ? local.getNomLocal() : "";
                }
                @Override
                public Local fromString(String string) { return null; }
            });

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));
            grid.add(createLabel("Mot-clé :"), 0, 0);
            grid.add(txtMotCle, 1, 0);
            grid.add(createLabel("Date péremption :"), 0, 1);
            grid.add(dpDate, 1, 1);
            grid.add(createLabel("Type :"), 0, 2);
            grid.add(cbType, 1, 2);
            grid.add(createLabel("Local :"), 0, 3);
            grid.add(cbLocal, 1, 3);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

            dialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    String mot = txtMotCle.getText();
                    Date date = dpDate.getValue() != null ? Date.valueOf(dpDate.getValue()) : null;
                    TypeArticle type = cbType.getValue();
                    Integer idLocal = cbLocal.getValue() != null ? cbLocal.getValue().getIdLocal() : null;
                    return ac.rechercherArticle(mot, date, type, idLocal);
                }
                return null;
            });

            dialog.showAndWait().ifPresent(resultArticles -> {
                if (resultArticles != null) {
                    articlesList.setAll(resultArticles);
                }
            });
        });

        btnReset.setOnAction(e -> {
            articlesList.setAll(ac.getAllArticles());
            clearFields(txtReference, txtDesignation, txtDescription, txtCodeBarres,
                    txtStockMinimal, comboType, datePicker, chkCritique, comboLocal);
        });

        btnAlerte.setOnAction(e -> articlesList.setAll(ac.getArticlesAlerte()));
        btnAlerteStock.setOnAction(e -> articlesList.setAll(ac.getArticlesStockAlerte()));
        btnCritique.setOnAction(e -> articlesList.setAll(ac.getArticlesConsommablesCritiques()));





        btnRechercherMvt.setOnAction(e -> {
            Dialog<List<Article>> dialog = new Dialog<>();
            dialog.setTitle("Recherche par mouvement");

            VBox dialogBox = new VBox(10);
            dialogBox.setPadding(new Insets(20));

            ComboBox<String> cbTypeMvt = new ComboBox<>();
            cbTypeMvt.getItems().addAll("ENTREE", "SORTIE", "AJUSTEMENT");
            cbTypeMvt.setPromptText("Sélectionner un type");
            cbTypeMvt.setStyle(fieldStyle);

            DatePicker dpDebut = new DatePicker();
            dpDebut.setPromptText("jj/mm/aaaa");
            dpDebut.setStyle(fieldStyle);

            DatePicker dpFin = new DatePicker();
            dpFin.setPromptText("jj/mm/aaaa");
            dpFin.setStyle(fieldStyle);

            dialogBox.getChildren().addAll(
                    createLabel("Type Mouvement :"), cbTypeMvt,
                    createLabel("Date début :"), dpDebut,
                    createLabel("Date fin :"), dpFin
            );

            dialog.getDialogPane().setContent(dialogBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

            dialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    String type = cbTypeMvt.getValue();

                    // Conversion explicite en java.sql.Date
                    java.sql.Date debut = null;
                    java.sql.Date fin = null;

                    if (dpDebut.getValue() != null) {
                        debut = java.sql.Date.valueOf(dpDebut.getValue());
                    }
                    if (dpFin.getValue() != null) {
                        fin = java.sql.Date.valueOf(dpFin.getValue());
                    }

                    // Validation
                    if (type == null && debut == null && fin == null) {
                        showAlert("Erreur", "Veuillez fournir au moins un critère de recherche");
                        return null;
                    }

                    return ac.getArticlesByMouvement(type, debut, fin);
                }
                return null;
            });

            dialog.showAndWait().ifPresent(resultArticles -> {
                if (resultArticles != null && !resultArticles.isEmpty()) {
                    articlesList.setAll(resultArticles);
                } else if (resultArticles != null && resultArticles.isEmpty()) {
                    showAlert("Information", "Aucun article trouvé pour ces critères");
                    articlesList.clear();
                }
            });
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtReference.setText(newSel.getReference());
                txtDesignation.setText(newSel.getDesignation());
                txtDescription.setText(newSel.getDescription());
                txtCodeBarres.setText(newSel.getCodeBarres());
                txtStockMinimal.setText(String.valueOf(newSel.getStockMinimal()));
                comboType.setValue(newSel.getType());
                if (newSel.getDatePeremption() != null)
                    datePicker.setValue(new java.sql.Date(newSel.getDatePeremption().getTime()).toLocalDate());
                chkCritique.setSelected(newSel.isEstCritique());

                if (newSel.getIdlocal() != null) {
                    Local local = trouverLocalParId(newSel.getIdlocal());
                    comboLocal.setValue(local);
                } else {
                    comboLocal.setValue(null);
                }
            }
        });


        VBox mainLayout = new VBox(15, tpForm, tpActions, tpTable);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: " + BACKGROUND_COLOR + ";");

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        stage.setScene(new Scene(scrollPane, 1000, 750));
        stage.show();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        label.setStyle("-fx-text-fill: #4A5568;");
        return label;
    }

    private Button createMainButton(String text, String gradient) {
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

    private Local trouverLocalParId(int idLocal) {
        List<Local> locaux = ac.getAllLocaux();
        for (Local local : locaux) {
            if (local.getIdLocal() == idLocal) {
                return local;
            }
        }
        return null;
    }

    private void clearFields(TextField ref, TextField des, TextField desc, TextField cb,
                             TextField stock, ComboBox<TypeArticle> type, DatePicker date,
                             CheckBox critique, ComboBox<Local> comboLocal) {
        ref.clear();
        des.clear();
        desc.clear();
        cb.clear();
        stock.clear();
        type.setValue(null);
        date.setValue(null);
        critique.setSelected(false);
        comboLocal.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}