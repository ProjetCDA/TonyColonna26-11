package com.example.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.CommandItem;
import com.example.model.Supplement;

import javafx.collections.ObservableList;

public class DataCommandItem{

   public static void insertBddProduct( ObservableList<CommandItem> productsSelection, int idcommand) {
    String query = "INSERT INTO commande_produit_supplement (id_commande, id_produit, id_supplement, supplement_associer) VALUES (?,?,?, null)";

    try (Connection conn = DataBase.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)){
        for (CommandItem commandItem : productsSelection) {
            int productId = commandItem.getProduit().getId();
            int quantite = commandItem.getQuantite();
            for (int i = 0; i < quantite; i++) {
                

            if (commandItem.getListSupplements() != null && !commandItem.getListSupplements().isEmpty()) {
                for (Supplement supplement : commandItem.getListSupplements()) {
                    int supplementId = supplement.getId();
                    preparedStatement.setInt(1, idcommand);
                    preparedStatement.setInt(2, productId);
                    preparedStatement.setInt(3, supplementId);
                    preparedStatement.addBatch();
                }
            } else {
                preparedStatement.setInt(1, idcommand);
                preparedStatement.setInt(2, productId);
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
                preparedStatement.addBatch();
            }
        }
        }

        int[] result = preparedStatement.executeBatch();
        System.out.println("Insertion terminée : " + result.length + " lignes ajoutées.");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Erreur lors de l'insertion dans la BDD : " + e.getMessage());
    }
}

}