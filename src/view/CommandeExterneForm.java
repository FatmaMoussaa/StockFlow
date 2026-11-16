package view;

import controller.*;
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

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeExterneForm {

    private CommandeExterneController cc = new CommandeExterneController();
    private StockController sc = new StockController();
    private TraceController tc = new TraceController();

    private ObservableList<CommandeExterne> commandesList = FXCollections.observableArrayList();
    private ObservableList<LigneCommande> lignesList = FXCollections.observableArrayList();


    private static final String GRADIENT_PRIMARY = "linear-gradient(to right, #667eea, #764ba2)";
    private static final String GRADIENT_SUCCESS = "linear-gradient(to right, #4facfe, #00f2fe)";
    private static final String GRADIENT_DANGER = "linear-gradient(to right, #fa709a, #fee140)";
    private static final String GRADIENT_WARNING = "linear-gradient(to right, #f093fb, #f5576c)";
    private static final String GRADIENT_INFO = "linear-gradient(to right, #4ECDC4, #44A08D)";
    private static final String BACKGROUND_COLOR = "#F9FAFB";
    private static final String CARD_COLOR = "#FFFFFF";
    private static final String VIOLET_MAIN = "#667eea";

    public void afficher(List<Fournisseur> fournisseurs, List<Article> articles, List<Local> locaux) {
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("✨ Gestion Commandes Externes - StockFlow ✨");


        ComboBox<Fournisseur> cbFournisseur = new ComboBox<>();
        cbFournisseur.getItems().addAll(fournisseurs);
        cbFournisseur.setPromptText("Sélectionner un fournisseur");

        TextField txtMontant = new TextField();
        txtMontant.setPromptText("Montant");

        String fieldStyle = "-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;";
        cbFournisseur.setStyle(fieldStyle);
        txtMontant.setStyle(fieldStyle);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        formGrid.add(createStyledLabel("Fournisseur :"), 0, 0);
        formGrid.add(cbFournisseur, 1, 0);
        formGrid.add(createStyledLabel("Montant :"), 0, 1);
        formGrid.add(txtMontant, 1, 1);

        TitledPane tpForm = new TitledPane("📝 Détails de la Commande", formGrid);
        tpForm.setExpanded(true);
        tpForm.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        ComboBox<Article> cbArticle = new ComboBox<>();
        cbArticle.getItems().addAll(articles);
        cbArticle.setPromptText("Sélectionner un article");
        cbArticle.setStyle(fieldStyle);

        cbArticle.setConverter(new javafx.util.StringConverter<Article>() {
            @Override
            public String toString(Article article) {
                return article != null ? article.getReference() : "";
            }
            @Override
            public Article fromString(String string) {
                return null;
            }
        });

        TextField txtQuantite = new TextField();
        txtQuantite.setPromptText("Quantité");
        txtQuantite.setStyle(fieldStyle);

        Button btnAjouterLigne = createSmallButton("➕ Ajouter Ligne", GRADIENT_SUCCESS);
        Button btnSupprimerLigne = createSmallButton("🗑️ Supprimer Ligne", GRADIENT_DANGER);

        VBox articleBox = new VBox(5, createStyledLabel("Article :"), cbArticle);
        VBox quantiteBox = new VBox(5, createStyledLabel("Quantité :"), txtQuantite);
        VBox buttonsBox = new VBox(5, btnAjouterLigne, btnSupprimerLigne);

        HBox ligneControls = new HBox(15, articleBox, quantiteBox, buttonsBox);
        ligneControls.setPadding(new Insets(15));
        ligneControls.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");


        TableView<LigneCommande> tableLignes = new TableView<>();
        tableLignes.setStyle("-fx-background-color: transparent;");

        TableColumn<LigneCommande, String> colArticle = new TableColumn<>("Article");
        colArticle.setCellValueFactory(cell -> {
            Article a = articles.stream()
                    .filter(ar -> ar.getIdArticle() == cell.getValue().getIdArticle())
                    .findFirst().orElse(null);

            return new ReadOnlyStringWrapper(a != null ? a.getReference() : "Inconnu");
        });

        TableColumn<LigneCommande, Integer> colQuantite = new TableColumn<>("Quantité");
        colQuantite.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantite()));

        TableColumn<LigneCommande, String> colLocal = new TableColumn<>("Local");
        colLocal.setCellValueFactory(cell -> {
            Local l = locaux.stream()
                    .filter(loc -> loc.getIdLocal() == cell.getValue().getIdLocal())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(l != null ? l.getNomLocal() : "Inconnu");
        });


        colArticle.setStyle("-fx-alignment: CENTER-LEFT;");
        colQuantite.setStyle("-fx-alignment: CENTER;");
        colLocal.setStyle("-fx-alignment: CENTER-LEFT;");


        colArticle.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.50));
        colQuantite.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.20));
        colLocal.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.30));

        tableLignes.getColumns().addAll(colArticle, colQuantite, colLocal);
        tableLignes.setItems(lignesList);


        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 12;";
        for (TableColumn<?, ?> col : tableLignes.getColumns()) {
            col.setStyle(headerStyle);
        }


        tableLignes.setRowFactory(tv -> new TableRow<LigneCommande>() {
            @Override
            protected void updateItem(LigneCommande item, boolean empty) {
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

        TitledPane tpLignes = new TitledPane("📋 Lignes de Commande", lignesContainer);
        tpLignes.setExpanded(true);
        tpLignes.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        TableView<CommandeExterne> tableCommandes = new TableView<>();
        tableCommandes.setStyle("-fx-background-color: transparent;");

        TableColumn<CommandeExterne, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getIdCommande()));
        colId.setStyle("-fx-alignment: CENTER;");

        TableColumn<CommandeExterne, String> colFourn = new TableColumn<>("Fournisseur");
        colFourn.setCellValueFactory(cell -> {
            Fournisseur f = fournisseurs.stream()
                    .filter(fr -> fr.getIdFournisseur() == cell.getValue().getIdFournisseur())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(f != null ? f.getNomFournisseur() : "Inconnu");
        });
        colFourn.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<CommandeExterne, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getStatut().toString()));
        colStatut.setStyle("-fx-alignment: CENTER;");
        colStatut.setCellFactory(column -> new TableCell<CommandeExterne, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("VALIDEE".equals(item)) {
                        setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    } else if ("REJETEE".equals(item)) {
                        setStyle("-fx-text-fill: #f5576c; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-text-fill: #f093fb; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    }
                    setAlignment(Pos.CENTER);
                }
            }
        });


        colId.prefWidthProperty().bind(tableCommandes.widthProperty().multiply(0.15));
        colFourn.prefWidthProperty().bind(tableCommandes.widthProperty().multiply(0.55));
        colStatut.prefWidthProperty().bind(tableCommandes.widthProperty().multiply(0.30));

        tableCommandes.getColumns().addAll(colId, colFourn, colStatut);
        tableCommandes.setItems(commandesList);
        commandesList.setAll(cc.getAllCommandes());


        for (TableColumn<?, ?> col : tableCommandes.getColumns()) {
            col.setStyle(headerStyle);
        }


        tableCommandes.setRowFactory(tv -> new TableRow<CommandeExterne>() {
            @Override
            protected void updateItem(CommandeExterne item, boolean empty) {
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

        VBox.setVgrow(tableCommandes, Priority.ALWAYS);

        VBox commandesContainer = new VBox(tableCommandes);
        commandesContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        VBox.setVgrow(commandesContainer, Priority.ALWAYS);

        TitledPane tpCommandes = new TitledPane("📋 Liste des Commandes", commandesContainer);
        tpCommandes.setExpanded(true);
        tpCommandes.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        Button btnAjouter = createStyledButton("➕ Ajouter", GRADIENT_SUCCESS);
        Button btnModifier = createStyledButton("✏️ Modifier", GRADIENT_PRIMARY);
        Button btnSupprimer = createStyledButton("🗑️ Supprimer", GRADIENT_DANGER);
        Button btnValider = createStyledButton("✅ Valider", GRADIENT_INFO);
        Button btnRejeter = createStyledButton("❌ Rejeter", GRADIENT_WARNING);

        HBox commandeButtons = new HBox(12, btnAjouter, btnModifier, btnSupprimer, btnValider, btnRejeter);
        commandeButtons.setAlignment(Pos.CENTER);
        commandeButtons.setPadding(new Insets(10));

        VBox actionsContainer = new VBox(commandeButtons);
        actionsContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TitledPane tpActions = new TitledPane("⚡ Actions Commande", actionsContainer);
        tpActions.setExpanded(true);
        tpActions.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        btnAjouterLigne.setOnAction(e -> {
            if(cbArticle.getValue() != null && !txtQuantite.getText().isEmpty()){
                LigneCommande ligne = new LigneCommande();
                ligne.setIdArticle(cbArticle.getValue().getIdArticle());
                ligne.setIdLocal(cbArticle.getValue().getIdlocal());
                ligne.setQuantite(Integer.parseInt(txtQuantite.getText()));
                lignesList.add(ligne);
                txtQuantite.clear();
                cbArticle.setValue(null);
            }
        });

        btnSupprimerLigne.setOnAction(e -> {
            LigneCommande selected = tableLignes.getSelectionModel().getSelectedItem();
            if(selected != null) lignesList.remove(selected);
        });

        tableCommandes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if(newSel != null){
                cbFournisseur.setValue(
                        fournisseurs.stream()
                                .filter(f -> f.getIdFournisseur() == newSel.getIdFournisseur())
                                .findFirst().orElse(null)
                );
                lignesList.setAll(newSel.getLignesCommande());
                txtMontant.setText(String.valueOf(newSel.getMontant()));
            }
        });

        btnAjouter.setOnAction(e -> ajouterCommande(cbFournisseur, txtMontant));
        btnModifier.setOnAction(e -> {
            CommandeExterne selected = tableCommandes.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.setIdFournisseur(cbFournisseur.getValue().getIdFournisseur());
                    selected.setMontant(Double.parseDouble(txtMontant.getText()));
                    selected.setLignesCommande(tableLignes.getItems());
                    boolean ok = cc.modifierCommande(selected);
                    if (ok) {
                        commandesList.setAll(cc.getAllCommandes());
                        showSuccess("✅ Succès", "Commande modifiée avec succès");
                        tc.ajouterTrace("Modification CE", "Magasinier", "Commande ID=" + selected.getIdCommande());
                    } else {
                        showAlert("❌ Erreur", "Erreur lors de la modification !");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnSupprimer.setOnAction(e -> supprimerCommande(tableCommandes));
        btnValider.setOnAction(e -> {
            try {
                validerCommande(tableCommandes);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnRejeter.setOnAction(e -> rejeterCommande(tableCommandes));

        // ===== LAYOUT PRINCIPAL =====
        VBox mainLayout = new VBox(15, tpForm, ligneControls, tpLignes, tpActions, tpCommandes);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: " + BACKGROUND_COLOR + ";");

        VBox.setVgrow(tpLignes, Priority.ALWAYS);
        VBox.setVgrow(tpCommandes, Priority.ALWAYS);

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


    private void ajouterCommande(ComboBox<Fournisseur> cbFournisseur, TextField txtMontant){
        if(cbFournisseur.getValue()==null || lignesList.isEmpty()) return;
        CommandeExterne c = new CommandeExterne();
        c.setIdFournisseur(cbFournisseur.getValue().getIdFournisseur());
        c.setMontant(txtMontant.getText().isEmpty()?0:Double.parseDouble(txtMontant.getText()));
        c.setStatut(StatutCommande.EN_ATTENTE);
        c.setLignesCommande(new ArrayList<>(lignesList));
        if(cc.ajouterCommande(c)){
            tc.ajouterTrace("Ajout CE","Magasinier","Commande "+c.getIdCommande());
            commandesList.setAll(cc.getAllCommandes());
            lignesList.clear(); txtMontant.clear(); cbFournisseur.setValue(null);
            showSuccess("✅ Succès", "Commande ajoutée avec succès !");
        }
    }

    private void supprimerCommande(TableView<CommandeExterne> table){
        CommandeExterne selected = table.getSelectionModel().getSelectedItem();
        if(selected != null){
            cc.supprimerCommande(selected.getIdCommande());
            commandesList.setAll(cc.getAllCommandes());
            showSuccess("✅ Succès", "Commande supprimée avec succès !");
        }
    }

    private void validerCommande(TableView<CommandeExterne> table) throws SQLException {
        CommandeExterne selected = table.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getStatut() == StatutCommande.EN_ATTENTE) {
            CommandeExterne full = cc.getCommandeById(selected.getIdCommande());
            ControleController controleCtrl = new ControleController();
            boolean ok = true;
            Timestamp dateNow = Timestamp.valueOf(LocalDateTime.now());

            for (LigneCommande ligne : full.getLignesCommande()) {
                ChoiceDialog<EtatArticle> dialogEtat = new ChoiceDialog<>(EtatArticle.BON, EtatArticle.values());
                dialogEtat.setTitle("Contrôle produit");
                dialogEtat.setHeaderText("Produit : " + ligne.getIdArticle());
                EtatArticle etatReel = dialogEtat.showAndWait().orElse(EtatArticle.BON);

                TextInputDialog dialogQuantite = new TextInputDialog(String.valueOf(ligne.getQuantite()));
                dialogQuantite.setTitle("Quantité reçue");
                dialogQuantite.setHeaderText("Produit : " + ligne.getIdArticle());
                dialogQuantite.setContentText("Quantité reçue :");

                int quantiteRecue;
                try {
                    quantiteRecue = Integer.parseInt(dialogQuantite.showAndWait().orElse("0"));
                } catch (NumberFormatException e) {
                    quantiteRecue = 0;
                }

                Controle controle = new Controle(
                        dateNow, quantiteRecue, etatReel, "Réception commande externe",
                        ligne.getIdArticle(), null, ligne.getIdLocal(), null, full.getIdCommande()
                );

                String statutControle = (etatReel == EtatArticle.PARTIEL || etatReel == EtatArticle.DEFECTUEUX) ? "ECHEC" : "OK";
                tc.ajouterTrace("Contrôle CE", "Magasinier", "Article " + ligne.getIdArticle() + " - Quantité reçue: " + quantiteRecue + " - État: " + etatReel + " - Statut contrôle: " + statutControle);

                if (etatReel == EtatArticle.PARTIEL || etatReel == EtatArticle.DEFECTUEUX) {
                    showAlert("❌ Erreur", "Le produit " + ligne.getIdArticle() + " est partiel ou défectueux. Contrôle échoué !");
                    ok = false;
                    break;
                }

                if (quantiteRecue < ligne.getQuantite()) {
                    showAlert("❌ Erreur", "Quantité reçue insuffisante pour le produit " + ligne.getIdArticle());
                    ok = false;
                    break;
                }

                if (!controleCtrl.traiterControleExterne(controle)) {
                    ok = false;
                    break;
                }
            }

            if (!ok) {
                showAlert("❌ Erreur", "Erreur : contrôle produit échoué !");
                return;
            }

            full.setStatut(StatutCommande.VALIDEE);
            if (cc.modifierCommande(full)) {
                tc.ajouterTrace("Validation CE", "Magasinier", "Commande externe #" + full.getIdCommande() + " validée");
                commandesList.setAll(cc.getAllCommandes());
                showSuccess("✅ Succès", "Commande validée avec succès !");
            }
        }
    }

    private void rejeterCommande(TableView<CommandeExterne> table){
        CommandeExterne selected = table.getSelectionModel().getSelectedItem();
        if(selected != null){
            selected.setStatut(StatutCommande.REJETEE);
            cc.modifierCommande(selected);
            commandesList.setAll(cc.getAllCommandes());
            showSuccess("✅ Succès", "Commande rejetée !");
        }
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
}