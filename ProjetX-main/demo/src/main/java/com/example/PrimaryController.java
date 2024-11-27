package com.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.example.DAO.DataCommandItem;
import com.example.DAO.DataProduits;
import com.example.DAO.DataUser;
import com.example.model.Categorie;
import com.example.model.CommandItem;
import com.example.model.Produit;
import com.example.model.SousCategorie;
import com.example.model.Supplement;
import com.example.model.UserSession;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class PrimaryController {

    private Stage stage;
    
    public void setStage(Stage stage) {
    this.stage = stage;
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToTroisiemary() throws IOException {
        App.setRoot("troisiemary");
    }
    @FXML
    private Label idUtilisateur;

    @FXML
    public void initialize() {
        updateMenu();
        UserSession session = UserSession.getInstance();
        String nom = session.getNom();
        String message = nom != null ? "Bienvenue, " + nom : "Utilisateur non connecté";
        idUtilisateur.setText(message);
        listing();
    }


        //CREATION D UNE NOUVELLE TABLE//
@FXML
private void nouvelleTable(ActionEvent event) {
    Dialog<String> dialog = new Dialog<>();
    Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    dialog.initOwner(currentStage);

    dialog.setTitle("Créer une nouvelle commande");
    dialog.setHeaderText("Entrez le numéro de la table (ex : 4)");

    Label label = new Label("Numéro de table");
    TextField textField = new TextField();

    ButtonType validerButtonType = new ButtonType("Valider");
    dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, ButtonType.CANCEL);

    VBox vbox = new VBox(10, label, textField);
    dialog.getDialogPane().setContent(vbox);

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == validerButtonType) {
            return textField.getText();
        }
        return null;
    });

    dialog.showAndWait().ifPresent(response -> {
        try {
            int tableId = Integer.parseInt(response);
            int userId = UserSession.getInstance().getIdUser();
            Integer idcommand = DataUser.createTableWithSpecificId(tableId, userId);
            UserSession.getInstance().setId_commande(idcommand);
            System.out.println(UserSession.getInstance().getId_commande());

            if (idcommand != null) {
                System.out.println("Table " + tableId + " associée avec succès !");
                System.out.println("idcommand = " + idcommand);
            } else {
                System.out.println("Table " + tableId + " déjà occupée ou inexistante.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un numéro de table valide.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
    
}

    //CREATION D UNE NOUVELLE TABLE//

    //GESTION DES CATEGORIES//


    @FXML
    private FlowPane categoriesContainer;
    @FXML
    private VBox detailsContainer;

    private final ObservableList<Categorie> categories = FXCollections.observableArrayList();
    private final ObservableList<SousCategorie> sousCategories = FXCollections.observableArrayList();
    private final ObservableList<Produit> produits = FXCollections.observableArrayList();
    private final ObservableList<Supplement> supplements = FXCollections.observableArrayList();
    private final ObservableList<CommandItem> productsSelection = FXCollections.observableArrayList();
    

    //CHARGE LES MENUS STATIQUE//

    public void updateMenu() {
        categoriesContainer.getChildren().clear();

        try {
            categories.setAll(DataProduits.getCategories());

            for (Categorie category : categories) {
                Button button = new Button(category.getName());
                button.setMinWidth(100);
                button.setMinHeight(45);

                String hexColor = category.getCouleur();
                button.setStyle("-fx-base: " + hexColor + ";");

                button.setOnAction(e -> updateDetailsForCategory(category.getId()));

                categoriesContainer.getChildren().add(button);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //CHARGE LES MENUS STATIQUE//

    //CHARGE LES SOUS CATEGORIE//

    public void updateDetailsForCategory(int categoryId) {
        detailsContainer.getChildren().clear();
    
        try {
            // Vérifie si la catégorie a des sous-catégories
            sousCategories.setAll(DataProduits.getSousCategories(categoryId));
    
            if (!sousCategories.isEmpty()) {
                // Affiche les sous-catégories si elles existent
                FlowPane pane = new FlowPane();
    
                pane.setHgap(10); // Espacement horizontal entre les éléments
                pane.setVgap(10); // Espacement vertical entre les éléments
                pane.setAlignment(Pos.CENTER); // Alignement global du contenu
                pane.setPadding(new Insets(10)); // Ajoute une marge autour du FlowPane
    
                for (SousCategorie sousCategorie : sousCategories) {
                    Button button = new Button(sousCategorie.getNom());
                    button.setMinWidth(100);
                    button.setMinHeight(45);
    
                    // Charge les produits de la sous-catégorie
                    button.setOnAction(e -> updateDetailsForSousCategory(sousCategorie.getId()));
    
                    pane.getChildren().add(button);
    
                }
    
                detailsContainer.getChildren().add(pane);
            } else {
                // Pas de sous-catégories : affiche les produits de la catégorie directement
                // updateDetailsForNoSub(categoryId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //CHARGE LES SOUS CATEGORIE//

    //CHARGE LES PRODUITS SANS SOUS CATEGORIES//

    // public void updateDetailsForNoSub(int categoryId) {
    //     detailsContainer.getChildren().clear();
    
    //     try {
    //         // Charge uniquement les produits de la catégorie ou sous-catégorie donnée
    //         produits.setAll(DataProduits.getProductsByCategorie(categoryId));
    
    //         GridPane grid = createGrid();
    
    //         int row = 0;
    //         int col = 0;
    
    //         for (Produit produit : produits) {
    //             Button button = new Button(produit.getName());
    //             button.setMinWidth(100);
    //             button.setMinHeight(45);
    
    //             // Gère la sélection du produit
    //             button.setOnAction(e -> {
    //                 try {
    //                     handleProductSelection(produit, produit.getId());
    //                 } catch (SQLException e1) {
    //                     // TODO Auto-generated catch block
    //                     e1.printStackTrace();
    //                 }
    //             });
    //             grid.add(button, col, row);
    //             col++;
    //             if (col == 3) {
    //                 col = 0;
    //                 row++;
    //             }
    //         }
    //         detailsContainer.getChildren().add(grid);
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    //CHARGE LES PRODUITS SANS SOUS CATEGORIES//
    
    //CHARGE LES PRODUITS AVEC SOUS CATEGORIE//

    public void updateDetailsForSousCategory(int sousCategorieId) {
        detailsContainer.getChildren().clear();
    
        try {
            // Charge uniquement les produits de la catégorie ou sous-catégorie donnée
            produits.setAll(DataProduits.getProductsWithSupplementsBySousCategorie(sousCategorieId));
    
            GridPane grid = createGrid();
            grid.setHgap(20);
            grid.setVgap(15);
    
            int row = 0;
            int col = 0;
    
            for (Produit produit : produits) {
                Button button = new Button(produit.getName());
                button.setMaxWidth(150);
                button.setMinHeight(45);
    
                // Gère la sélection du produit
                button.setOnAction(e -> {
                    if (!produit.getSupplements().isEmpty()) {
                        showSupplement(produit.getSupplements(), produit.getId());
                    }
                    else{
                        insertLocalProduct(produit.getId(), produit.getSupplements());
                    }});

                grid.add(button, col, row);
                
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER); 
            vbox.setSpacing(20); 
    
            vbox.getChildren().add(grid);
    
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER); 
            hbox.getChildren().add(vbox);  
    
            hbox.setPrefSize(800, 200); 

            detailsContainer.getChildren().add(hbox);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    //GESTION DES SUPPLEMENTS//

   @FXML
    private void showSupplement(ObservableList<Supplement> supplements, int idProduit) {
    ObservableList<Supplement> supplementsSelectionnes = FXCollections.observableArrayList();
    Dialog<Void> dialog = new Dialog<>();
    dialog.initOwner(stage);

    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);

    Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
    cancelButton.setText("Annuler");
    cancelButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 8px;");
    cancelButton.setOnAction(e -> {
        System.out.println("Aucun supplément sélectionné");
        dialog.close();
    });

        Label produitLabel = new Label("Produit : " + idProduit);
        produitLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
    
        TableView<Supplement> tableView = new TableView<>();
        tableView.setEditable(false);
    
        TableColumn<Supplement, String> nomColonne = new TableColumn<>("Supplément");
        nomColonne.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
    
        tableView.getColumns().add(nomColonne);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    
        tableView.setMaxHeight(200);
        tableView.setPrefHeight(200);
        tableView.setMaxWidth(300);
        tableView.setPrefWidth(300);

    
        tableView.setItems(supplementsSelectionnes);


    Label label = new Label("Ajoutez des suppléments :");
    label.getStyleClass().add("labelNewTable");

    HBox hbox = new HBox(10);
    hbox.setStyle("-fx-alignment: center;");

    for (Supplement supplement : supplements) {
        String nomSupplement = supplement.getNom();
        Button buttonSupplement = new Button(nomSupplement);
        hbox.getChildren().add(buttonSupplement);

        buttonSupplement.setOnAction(e -> {
            System.out.println("Supplément ajouté : " + nomSupplement);
            supplementsSelectionnes.add(supplement);
        });
    }

    Button validateButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
    validateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 8px;");
    validateButton.setOnAction(e -> {
    insertLocalProduct(idProduit, supplementsSelectionnes);
    System.out.println(productsSelection);
    System.out.println("Validation des suppléments sélectionnés :");
    supplementsSelectionnes.forEach(supplement -> 
        System.out.println("- " + supplement.getNom())
    );
    dialog.close();
    });

    VBox vbox = new VBox(10, produitLabel, tableView, label, hbox);
    vbox.setPadding(new Insets(10));
    vbox.setStyle("-fx-alignment: center;");

    dialog.getDialogPane().setContent(vbox);
    dialog.showAndWait();
}

    //GESTION DES SUPPLEMENTS//

    //AJOUT DES PRODUITS EN LOCALE//
    
    private void insertLocalProduct(int idProduit, ObservableList<Supplement> supplementsSelectionnes) {
    boolean produitTrouve = false;

    for (Produit produit : produits) {
        if (produit.getId() == idProduit) {
            produitTrouve = true;
            CommandItem existingItem = null;
            for (CommandItem item : productsSelection) {
                if (item.getProduit().getId() == produit.getId() && areSupplementsEqual(item.getListSupplements(), supplementsSelectionnes)) {
                    existingItem = item;
                    break;
                }
            }
        if (existingItem !=null) {
            existingItem.setQuantite(existingItem.getQuantite() + 1);
            System.out.println("Produit ajouté : " + produit.getName() + " (sans supplément) et quantité= " + existingItem.getQuantite());
        }
        else{
            CommandItem commandItem = new CommandItem();
            commandItem.setProduit(produit);
            commandItem.setQuantite(1);
            commandItem.setListSupplements(supplementsSelectionnes);

            productsSelection.add(commandItem);
            System.out.println("Produit ajouté : " + produit.getName() + " (sans supplément) et quantité= " + commandItem.getQuantite());
        }
        

            
  
            if (supplementsSelectionnes != null && !supplementsSelectionnes.isEmpty()) {
                System.out.println("Produit ajouté : " + produit.getName() + " Supplément(s) : " + supplementsSelectionnes.stream()
                .map(Supplement::getNom)  // Afficher uniquement le nom des suppléments
                .collect(Collectors.joining(", ")));
        } 
        else {
            System.out.println("Produit ajouté : " + produit.getName() + " (sans supplément)");
    }
            break;
        }
    }

    if (!produitTrouve) {
        System.out.println("Produit avec ID " + idProduit + " introuvable.");
    }
    afficherProductsSelection();
    
}

private boolean areSupplementsEqual(ObservableList<Supplement> list1, ObservableList<Supplement> list2) {
    if (list1 == null && list2 == null) {
        return true;
    }
    if (list1 == null || list2 == null) {
        return false;
    }

    return new HashSet<>(list1).equals(new HashSet<>(list2));
}

private void afficherProductsSelection() {
    if (productsSelection.isEmpty()) {
        System.out.println("Aucun produit dans la sélection.");
        return;
    }

    System.out.println("Liste des produits sélectionnés :");
    for ( CommandItem item : productsSelection) {
        System.out.println("Produit ID: " + item.getId());
        System.out.println("Nom: " + item.getProduit().getName());
        System.out.println("Couleur: " + item.getProduit().getCouleur());
        System.out.println("ID Sous-catégorie: " + item.getProduit().getCategoryId());

        if (item.getProduit().getSupplements().isEmpty()) {
            System.out.println("Aucun supplément associé.");
        } else {
            System.out.println("Suppléments associés :");
            for (Supplement supplement : item.getProduit().getSupplements()) {
                System.out.println("    - " + supplement.getNom() + " (Prix: " + supplement.getPrix() + ")");
            }
        }
        System.out.println();
    }
}

@FXML
private TableView<CommandItem> tableView;
@FXML
private TableColumn<CommandItem, String> nameColumn;
@FXML
private TableColumn<CommandItem, Integer> quantityColumn;

@FXML
private void listing() {
    
    nameColumn.setCellValueFactory(cellData -> 
        cellData.getValue().getProduit() != null
            ? new SimpleStringProperty(cellData.getValue().getProduit().getName())
            : new SimpleStringProperty(""));
    
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
    quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));


    tableView.setItems(productsSelection);

    tableView.setEditable(true);
}

@FXML
private Button envoie;

@FXML
private void sendCommand(){
    System.out.println("Envoie appuyé");
    DataCommandItem.insertBddProduct(productsSelection, UserSession.getInstance().getId_commande());
    
}


}
