package com.example.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.example.model.Categorie;
import com.example.model.Produit;
import com.example.model.SousCategorie;
import com.example.model.Supplement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataProduits {

    public static ObservableList<Categorie> getCategories() throws SQLException {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM categorie;";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(new Categorie(
                    rs.getInt("id_categorie"),
                    rs.getString("nom"),
                     rs.getString("couleur_css_hexadecimal") 
                ));
            }
        }
        System.out.println(categories);
        return categories;
    }

    public static ObservableList<SousCategorie> getSousCategories(int categorieId) throws SQLException {
        ObservableList<SousCategorie> sousCategories = FXCollections.observableArrayList();
        String query = "SELECT * FROM sous_categorie WHERE id_categorie = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, categorieId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sousCategories.add(new SousCategorie(
                    rs.getInt("id_sous_categorie"),
                    rs.getString("nom"),
                    rs.getInt("id_categorie"),
                    rs.getString("couleur_css_hexadecimal") 
                ));
            }
        }
        return sousCategories;
    }



    // public static ObservableList<Produit> getProductsBySousCategorie(int sousCategorieId) throws SQLException {
    //     ObservableList<Produit> produits = FXCollections.observableArrayList(); 
    //     String query = "SELECT * FROM produit WHERE id_sous_categorie = ?";

    //     try (Connection conn = DataBase.getConnection();
    //          PreparedStatement stmt = conn.prepareStatement(query)) {

    //         stmt.setInt(1, sousCategorieId);
    //         ResultSet rs = stmt.executeQuery();
    //         while (rs.next()) {
    //             produits.add(new Produit(
    //                 rs.getInt("id_produit"),
    //                 rs.getString("nom"),
    //                 rs.getInt("id_sous_categorie"),
    //                 rs.getString("couleur_css_hexadecimal"), null;
    //             ));
    //         }
    //     }
    //     return produits;
    // }

    public static ObservableList<Produit> getProductsWithSupplementsBySousCategorie(int sousCategorieId) throws SQLException {
        ObservableList<Produit> produits = FXCollections.observableArrayList(); 
        String query = "SELECT p.id_produit, p.nom AS produit_nom, p.id_sous_categorie, p.couleur_css_hexadecimal, s.id_supplement, s.nom AS supplement_nom, s.prix "
        + "FROM produit p "
        + "LEFT JOIN produits_supplements ps ON p.id_produit = ps.id_produit "
        + "LEFT JOIN supplement s ON ps.id_supplement = s.id_supplement "
        + "WHERE p.id_sous_categorie = ?;";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, sousCategorieId);
            ResultSet rs = stmt.executeQuery();
    
            Map<Integer, Produit> produitMap = new HashMap<>(); // Pour éviter les doublons de produits
    
            while (rs.next()) {
                int produitId = rs.getInt("id_produit");
                String produitNom = rs.getString("produit_nom");
                int idProduitSousCategorie = rs.getInt("id_sous_categorie");
                String couleur = rs.getString("couleur_css_hexadecimal");

                Produit produit = produitMap.computeIfAbsent(produitId, id -> new Produit(
                    id,
                    produitNom,
                    idProduitSousCategorie,
                    couleur,
                    FXCollections.observableArrayList() // Liste des suppléments à remplir
                ));
    
                // Ajouter le supplément s'il existe dans le résultat
                int supplementId = rs.getInt("id_supplement");
                if (supplementId > 0) { // S'assurer qu'il y a un supplément
                    Supplement supplement = new Supplement(
                        supplementId,
                        rs.getString("supplement_nom"),
                        rs.getDouble("prix")
                    );
                    produit.getSupplements().add(supplement);
                }
            }
    
            produits.addAll(produitMap.values()); // Ajouter tous les produits à la liste finale
        }
    
        return produits;
    }
    

//     public static ObservableList<Produit> getProductsByCategorie(int CategorieId) throws SQLException {
//         ObservableList<Produit> produits = FXCollections.observableArrayList();
//         String query = "SELECT * FROM produit WHERE id_categorie = ?";
    
//         try (Connection conn = DataBase.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(query)) {
    
//             stmt.setInt(1, CategorieId);
//             ResultSet rs = stmt.executeQuery();
//             while (rs.next()) {
//                 produits.add(new Produit(
//                     rs.getInt("id_produit"),
//                     rs.getString("nom"),
//                     rs.getInt("id_categorie"),
//                     rs.getString("couleur_css_hexadecimal")  // Récupération de la couleur
//                 ));
//             }
//         }
//         return produits;
    
// }


}
