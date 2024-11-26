package com.example.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.Supplement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataSupplement {
    
    public static ObservableList<Supplement> getAllSupplements() throws SQLException{
        ObservableList<Supplement> allSupplements = FXCollections.observableArrayList();
        String query = "SELECT * FROM supplement";
        try(Connection conn = DataBase.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            allSupplements.add(new Supplement(
                rs.getInt("id_supplement"),
                rs.getString("nom"),
                rs.getDouble("prix")
                ));
        }    
        }
        return allSupplements;
    }

    
    public static ObservableList<Supplement> getSupplementByProduit(int produit) throws SQLException {
        ObservableList<Supplement> supplementByProduit = FXCollections.observableArrayList();
        String query = "SELECT supplement.id_supplement, supplement.nom, supplement.prix FROM supplement INNER JOIN produits_supplements INNER JOIN produit ON produits_supplements.id_supplement = supplement.id_supplement AND produits_supplements.id_produit = produit.id_produit WHERE produit.id_produit = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, produit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                supplementByProduit.add(new Supplement(
                    rs.getInt("id_supplement"),
                    rs.getString("nom"),
                    rs.getDouble("prix")
                ));
            }
        }
        return supplementByProduit;
    }
}
