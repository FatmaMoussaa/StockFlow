package view;

import controller.*;
import javafx.util.StringConverter;
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
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static controller.StockController.*;
import static controller.TraceController.ajouterTrace2;
import static model.TypeMouvement.ENTREE;
import static model.TypeMouvement.SORTIE;

public class CommandeInterneForm {

    private CommandeInterneController cc = new CommandeInterneController();
    private StockController sc = new StockController();
    private TraceController tc = new TraceController();

    private ObservableList<CommandeInterne> commandesList = FXCollections.observableArrayList();
    private ObservableList<LigneCommandeInterne> lignesList = FXCollections.observableArrayList();

    private static final String GRADIENT_PRIMARY = "linear-gradient(to right, #667eea, #764ba2)";
    private static final String GRADIENT_SUCCESS = "linear-gradient(to right, #4facfe, #00f2fe)";
    private static final String GRADIENT_DANGER = "linear-gradient(to right, #fa709a, #fee140)";
    private static final String GRADIENT_WARNING = "linear-gradient(to right, #f093fb, #f5576c)";
    private static final String GRADIENT_INFO = "linear-gradient(to right, #4ECDC4, #44A08D)";
    private static final String BACKGROUND_COLOR = "#F9FAFB";
    private static final String CARD_COLOR = "#FFFFFF";
    private static final String VIOLET_MAIN = "#667eea";

    public boolean validerCommandeInterne(CommandeInterne commande) {
        boolean success = false;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            StockController sc = new StockController();

            for (LigneCommandeInterne ligne : commande.getLignesCommande()) {
                System.out.println("---- Traitement ligne ----");
                int stockSource = getQuantiteActuelle2(ligne.getIdArticle(), ligne.getIdLocalSource(), conn);
                if (stockSource < ligne.getQuantite()) {
                    conn.rollback();
                    System.err.println("Stock insuffisant pour l'article ID " + ligne.getIdArticle());
                    System.out.println("❌ Stock insuffisant");
                    return false;
                }

                updateStock2(ligne.getIdArticle(), ligne.getIdLocalSource(), -ligne.getQuantite(), conn);
                System.out.println("❌ updateStock DESTINATION échoué");

                updateStock2(ligne.getIdArticle(), ligne.getIdLocalDestination(), ligne.getQuantite(), conn);
                System.out.println("❌ Mouvement ENTREE échoué");

                Mouvement mSortie = new Mouvement();
                mSortie.setIdArticle(ligne.getIdArticle());
                mSortie.setIdLocal(ligne.getIdLocalSource());
                mSortie.setQuantite(-ligne.getQuantite());
                mSortie.setTypeMouvement(SORTIE);
                mSortie.setObservation("Transfert vers local " + ligne.getIdLocalDestination());
                mSortie.setIdCommandeInterne(commande.getIdCommande());
                sc.addMouvement2(mSortie, conn);

                Mouvement mEntree = new Mouvement();
                mEntree.setIdArticle(ligne.getIdArticle());
                mEntree.setIdLocal(ligne.getIdLocalDestination());
                mEntree.setQuantite(ligne.getQuantite());
                mEntree.setTypeMouvement(ENTREE);
                mEntree.setObservation("Transfert depuis local " + ligne.getIdLocalSource());
                mEntree.setIdCommandeInterne(commande.getIdCommande());
                sc.addMouvement2(mEntree, conn);
            }
            System.out.println(">>> J'arrive à la mise à jour du statut");
            String sqlUpdate = "UPDATE CommandeInterne SET statut=? WHERE idCommande=?";
            try (PreparedStatement pst = conn.prepareStatement(sqlUpdate)) {
                pst.setString(1, StatutCommande.VALIDEE.toString());
                pst.setInt(2, commande.getIdCommande());
                pst.executeUpdate();
                int rows = pst.executeUpdate();
                System.out.println("Lignes affectées = " + rows);
            }
            System.out.println(">>> J'arrive à la mise à jour du statut");
            TraceController tc = new TraceController();
            tc.ajouterTrace2(
                    "Validation Commande Interne",
                    "Magasinier",
                    "Commande interne ID " + commande.getIdCommande() + " validée",
                    conn
            );

            conn.commit();
            success = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            success = false;
        }

        return success;
    }

    public void afficher(List<Service> services, List<Article> articles, List<Local> locaux) throws SQLException {
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("✨ Gestion Commandes Internes - StockFlow ✨");

        String fieldStyle = "-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-background-color: white;";

        ComboBox<Service> cbService = new ComboBox<>();
        cbService.getItems().addAll(services);
        cbService.setPromptText("Sélectionner un service");
        cbService.setStyle(fieldStyle);

        VBox formBox = new VBox(5, createStyledLabel("Service :"), cbService);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TitledPane tpForm = new TitledPane("📝 Détails de la Commande", formBox);
        tpForm.setExpanded(true);
        tpForm.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");

        TableView<LigneCommandeInterne> tableLignes = new TableView<>();
        tableLignes.setStyle("-fx-background-color: transparent;");

        TableColumn<LigneCommandeInterne, String> colArticle = new TableColumn<>("Article");
        colArticle.setCellValueFactory(cell -> {
            Article a = articles.stream()
                    .filter(ar -> ar.getIdArticle() == cell.getValue().getIdArticle())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(a != null ? a.getReference() : "Inconnu");
        });
        colArticle.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<LigneCommandeInterne, Integer> colQuantite = new TableColumn<>("Quantité");
        colQuantite.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantite()));
        colQuantite.setStyle("-fx-alignment: CENTER;");

        TableColumn<LigneCommandeInterne, String> colSource = new TableColumn<>("Source");
        colSource.setCellValueFactory(cell -> {
            Local l = locaux.stream()
                    .filter(loc -> loc.getIdLocal() == cell.getValue().getIdLocalSource())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(l != null ? l.getNomLocal() : "Inconnu");
        });
        colSource.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<LigneCommandeInterne, String> colDest = new TableColumn<>("Destination");
        colDest.setCellValueFactory(cell -> {
            Local l = locaux.stream()
                    .filter(loc -> loc.getIdLocal() == cell.getValue().getIdLocalDestination())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(l != null ? l.getNomLocal() : "Inconnu");
        });
        colDest.setStyle("-fx-alignment: CENTER-LEFT;");

        colArticle.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.35));
        colQuantite.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.15));
        colSource.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.25));
        colDest.prefWidthProperty().bind(tableLignes.widthProperty().multiply(0.25));

        tableLignes.getColumns().addAll(colArticle, colQuantite, colSource, colDest);
        tableLignes.setItems(lignesList);

        String headerStyle = "-fx-background-color: " + VIOLET_MAIN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 12;";
        for (TableColumn<?, ?> col : tableLignes.getColumns()) {
            col.setStyle(headerStyle);
        }

        tableLignes.setRowFactory(tv -> new TableRow<LigneCommandeInterne>() {
            @Override
            protected void updateItem(LigneCommandeInterne item, boolean empty) {
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

        ComboBox<Article> cbArticle = new ComboBox<>();
        cbArticle.getItems().addAll(articles);
        cbArticle.setPromptText("Sélectionner un article");
        cbArticle.setStyle(fieldStyle);
        cbArticle.setConverter(new StringConverter<Article>() {
            @Override
            public String toString(Article article) {
                return article != null ? article.getReference() : "";
            }
            @Override
            public Article fromString(String string) {
                return null;
            }
        });

        ComboBox<Local> cbLocalDestination = new ComboBox<>();
        cbLocalDestination.getItems().addAll(locaux);
        cbLocalDestination.setPromptText("Sélectionner une destination");
        cbLocalDestination.setStyle(fieldStyle);

        TextField txtQuantite = new TextField();
        txtQuantite.setPromptText("Quantité");
        txtQuantite.setStyle(fieldStyle);

        Button btnAjouterLigne = createSmallButton("➕ Ajouter Ligne", GRADIENT_SUCCESS);
        Button btnSupprimerLigne = createSmallButton("🗑️ Supprimer Ligne", GRADIENT_DANGER);

        VBox articleBox = new VBox(5, createStyledLabel("Article :"), cbArticle);
        VBox quantiteBox = new VBox(5, createStyledLabel("Quantité :"), txtQuantite);
        VBox destBox = new VBox(5, createStyledLabel("Destination :"), cbLocalDestination);
        VBox buttonsBox = new VBox(5, btnAjouterLigne, btnSupprimerLigne);

        HBox ligneControls = new HBox(15, articleBox, quantiteBox, destBox, buttonsBox);
        ligneControls.setPadding(new Insets(15));
        ligneControls.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TableView<CommandeInterne> tableCommandes = new TableView<>();
        tableCommandes.setStyle("-fx-background-color: transparent;");

        TableColumn<CommandeInterne, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getIdCommande()));
        colId.setStyle("-fx-alignment: CENTER;");

        TableColumn<CommandeInterne, String> colService = new TableColumn<>("Service");
        colService.setCellValueFactory(cell -> {
            Service s = services.stream()
                    .filter(sr -> sr.getIdService() == cell.getValue().getIdService())
                    .findFirst().orElse(null);
            return new ReadOnlyStringWrapper(s != null ? s.getNomService() : "Inconnu");
        });
        colService.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<CommandeInterne, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getStatut().name()));
        colStatut.setStyle("-fx-alignment: CENTER;");
        colStatut.setCellFactory(column -> new TableCell<CommandeInterne, String>() {
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
        colService.prefWidthProperty().bind(tableCommandes.widthProperty().multiply(0.55));
        colStatut.prefWidthProperty().bind(tableCommandes.widthProperty().multiply(0.30));

        tableCommandes.getColumns().addAll(colId, colService, colStatut);
        tableCommandes.setItems(commandesList);
        for (TableColumn<?, ?> col : tableCommandes.getColumns()) {
            col.setStyle(headerStyle);
        }

        tableCommandes.setRowFactory(tv -> new TableRow<CommandeInterne>() {
            @Override
            protected void updateItem(CommandeInterne item, boolean empty) {
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

        tableCommandes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lignesList.setAll(newSelection.getLignesCommande());
            } else {
                lignesList.clear();
            }
        });
        commandesList.setAll(cc.getAllCommandesInternes());
        VBox.setVgrow(tableCommandes, Priority.ALWAYS);

        VBox commandesContainer = new VBox(tableCommandes);
        commandesContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        VBox.setVgrow(commandesContainer, Priority.ALWAYS);

        TitledPane tpCommandes = new TitledPane("📋 Liste des Commandes", commandesContainer);
        tpCommandes.setExpanded(true);
        tpCommandes.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");

        Button btnAjouter = createStyledButton("➕ Ajouter", GRADIENT_SUCCESS);
        Button btnValider = createStyledButton("✅ Valider", GRADIENT_INFO);
        Button btnModifier = createStyledButton("✏️ Modifier", GRADIENT_PRIMARY);
        Button btnSupprimer = createStyledButton("🗑️ Supprimer", GRADIENT_DANGER);
        Button btnRetour = createStyledButton("🔄 Retour", GRADIENT_WARNING);
        Button btnRetourLigne = createStyledButton("↩️ Retourner Ligne", GRADIENT_WARNING);

        HBox commandeButtons = new HBox(12, btnAjouter, btnValider, btnModifier, btnSupprimer, btnRetour, btnRetourLigne);
        commandeButtons.setAlignment(Pos.CENTER);
        commandeButtons.setPadding(new Insets(10));

        VBox actionsContainer = new VBox(commandeButtons);
        actionsContainer.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TitledPane tpActions = new TitledPane("⚡ Actions Commande", actionsContainer);
        tpActions.setExpanded(true);
        tpActions.setStyle("-fx-font-weight: bold; -fx-text-fill: " + VIOLET_MAIN + ";");


        btnModifier.setOnAction(e -> {
            CommandeInterne cmd = tableCommandes.getSelectionModel().getSelectedItem();
            if (cmd == null) {
                showWarning("⚠️ Attention", "Sélectionner une commande");
                return;
            }
            if (tableLignes.getItems().isEmpty()) {
                showWarning("⚠️ Attention", "Aucune ligne à modifier !");
                return;
            }
            try {
                cmd.setLignesCommande(new ArrayList<>(tableLignes.getItems()));
                boolean ok = cc.modifierCommandeInterne(cmd);
                if (!ok) {
                    showAlert("❌ Erreur", "Erreur lors de la modification !");
                    return;
                }
                showSuccess("✅ Succès", "Commande modifiée avec succès");
                commandesList.setAll(cc.getAllCommandesInternes());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("❌ Erreur", "Erreur inattendue !");
            }
        });

        btnRetourLigne.setOnAction(e -> {
            LigneCommandeInterne ligne = tableLignes.getSelectionModel().getSelectedItem();
            CommandeInterne cmd = tableCommandes.getSelectionModel().getSelectedItem();
            if (ligne == null || cmd == null) {
                showWarning("⚠️ Attention", "Sélectionner une ligne et une commande");
                return;
            }
            boolean ok = cc.retournerLigneCommande(ligne, cmd.getIdCommande());
            if (!ok) {
                showWarning("⚠️ Attention", "Cette ligne est déjà retournée !");
                return;
            }
            showSuccess("✅ Succès", "Ligne retournée avec succès");
            commandesList.setAll(cc.getAllCommandesInternes());
        });

        btnAjouterLigne.setOnAction(e -> {
            try {
                if (cbArticle.getValue() == null || cbLocalDestination.getValue() == null)
                    throw new Exception("Remplir tous les champs");
                int qte = Integer.parseInt(txtQuantite.getText());
                LigneCommandeInterne ligne = new LigneCommandeInterne();
                ligne.setIdArticle(cbArticle.getValue().getIdArticle());
                ligne.setIdLocalSource(cbArticle.getValue().getIdlocal());
                ligne.setIdLocalDestination(cbLocalDestination.getValue().getIdLocal());
                ligne.setQuantite(qte);
                lignesList.add(ligne);
                txtQuantite.clear();
                cbArticle.setValue(null);
                cbLocalDestination.setValue(null);
            } catch (Exception ex) {
                showAlert("❌ Erreur", ex.getMessage());
            }
        });

        btnSupprimerLigne.setOnAction(e -> {
            LigneCommandeInterne selected = tableLignes.getSelectionModel().getSelectedItem();
            if (selected != null) lignesList.remove(selected);
        });

        btnAjouter.setOnAction(e -> {
            try {
                if (cbService.getValue() == null || lignesList.isEmpty())
                    throw new Exception("Sélectionner un service et ajouter au moins une ligne");
                CommandeInterne c = new CommandeInterne();
                c.setIdService(cbService.getValue().getIdService());
                c.setStatut(StatutCommande.EN_ATTENTE);
                c.setLignesCommande(new ArrayList<>(lignesList));
                boolean ok = cc.ajouterCommandeInterne(c);
                if (ok) {
                    tc.ajouterTrace("Ajout CI", "Magasinier", "Commande ID " + c.getIdCommande());
                    commandesList.setAll(cc.getAllCommandesInternes());
                    lignesList.clear();
                    showSuccess("✅ Succès", "Commande ajoutée avec succès !");
                } else {
                    throw new Exception("Erreur lors de l'ajout en base");
                }
            } catch (Exception ex) {
                showAlert("❌ Erreur", ex.getMessage());
            }
        });

        btnSupprimer.setOnAction(e -> {
            CommandeInterne c = tableCommandes.getSelectionModel().getSelectedItem();
            if (c != null) {
                try {
                    if (cc.supprimerCommandeInterne(c.getIdCommande())) {
                        commandesList.setAll(cc.getAllCommandesInternes());
                        showSuccess("✅ Succès", "Commande supprimée avec succès !");
                    }
                } catch (Exception ex) {
                    showAlert("❌ Erreur", ex.getMessage());
                }
            }
        });

        btnValider.setOnAction(e -> {
            CommandeInterne commande = tableCommandes.getSelectionModel().getSelectedItem();
            if (commande != null) {
                StockController sc = new StockController();
                try {
                    boolean ok = sc.validerCommandeInterne(commande);
                    System.out.println("Retour validerCommandeInterne : " + ok);
                    if (ok) {
                        showSuccess("✅ Succès", "Commande interne ID " + commande.getIdCommande() + " validée avec succès !");
                        List<CommandeInterne> allCommandes = sc.getAllCommandes();
                        ObservableList<CommandeInterne> observableCommandes = FXCollections.observableArrayList(allCommandes);
                        tableCommandes.setItems(observableCommandes);
                        tableCommandes.getSelectionModel().select(commande);
                    } else {
                        showAlert("❌ Erreur", "Impossible de valider la commande interne. Vérifiez le stock !");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("❌ Erreur", "Une erreur est survenue lors de la validation !");
                }
            } else {
                showWarning("⚠️ Attention", "Veuillez sélectionner une commande interne à valider.");
            }
        });

        btnRetour.setOnAction(e -> {
            CommandeInterne cmdSelectionnee = tableCommandes.getSelectionModel().getSelectedItem();

            if (cmdSelectionnee == null) {
                showWarning("⚠️ Attention", "Sélectionner une commande à retourner");
                return;
            }

            List<LigneCommandeInterne> lignesNonRetournees = cmdSelectionnee.getLignesCommande().stream()
                    .filter(l -> !l.isRetourEffectue())
                    .toList();

            if (lignesNonRetournees.isEmpty()) {
                showInfo("ℹ️ Information", "Cette commande est déjà totalement retournée !");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Voulez-vous vraiment retourner les lignes non retournées de cette commande ?",
                    ButtonType.YES, ButtonType.NO);

            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {

                boolean ok = cc.retournerCommandeInterne(cmdSelectionnee);

                if (ok) {
                    showSuccess("✅ Succès", "Commande retournée avec succès");
                } else {
                    showWarning("⚠️ Erreur", "Échec du retour");
                }

                commandesList.setAll(cc.getAllCommandesInternes());
            }
        });

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

        Scene scene = new Scene(scrollPane, 1300, 800);
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

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}