package com.example;

import java.io.IOException;
import java.sql.SQLException;

import com.example.DAO.DataUser;
import com.example.model.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void switchToTroisiemary() throws IOException {
        App.setRoot("troisiemary");
    }

    @FXML
    private ListView<String> commandListView;

    @FXML
public void initialize() {
    int userId = UserSession.getInstance().getIdUser(); // Récupère l'utilisateur connecté

    try {
        // Appel de la méthode DAO pour récupérer les tables associées
        ObservableList<String> tables = DataUser.getUserTables(userId);

        // Vérifier si l'utilisateur a des tables associées
        if (tables.isEmpty()) {
            tables.add("Aucune table associée !");
        }

        // Charger les tables dans la ListView
        commandListView.setItems(tables);

    } catch (SQLException e) {
        e.printStackTrace();
        // En cas d'erreur, afficher un message dans la liste
        commandListView.setItems(FXCollections.observableArrayList("Erreur lors du chargement des tables."));
    }
}

}
