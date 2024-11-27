package com.example.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.model.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataUser {

    public static boolean authenticate(String identifiant) throws SQLException {
        String query = "SELECT id_user, nom FROM user WHERE identifiant = ?";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, identifiant);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                // Met à jour les informations de la session
                UserSession session = UserSession.getInstance();
                session.setIdUser(rs.getInt("id_user"));
                session.setIdentifiant(identifiant);
                session.setNom(rs.getString("nom"));
                return true; // Authentification réussie
            }
        }
        return false; // Identifiant invalide
    }

    public static Integer createTableWithSpecificId(int tableId, int userId) throws SQLException {
        // Vérifier si la table est disponible
        String checkQuery = "SELECT id_table_client FROM table_client WHERE id_table_client = ? AND id_table_client NOT IN (SELECT id_table_client FROM commande)";
        String insertQuery = "INSERT INTO commande (id_table_client, id_user) VALUES (?, ?)";
        String lastInsertIdSQL = "SELECT LAST_INSERT_ID()";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
    
            // Vérifier la disponibilité de la table
            checkStmt.setInt(1, tableId);
            ResultSet rs = checkStmt.executeQuery();
    
            if (rs.next()) {
                // La table est disponible, effectuer l'insertion
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, tableId);
                    insertStmt.setInt(2, userId);
                    insertStmt.executeUpdate();
                }
    
                // Récupérer le dernier ID inséré
                try (Statement lastInsertStmt = conn.createStatement();
                     ResultSet lastInsertRs = lastInsertStmt.executeQuery(lastInsertIdSQL)) {
    
                    if (lastInsertRs.next()) {
                        return lastInsertRs.getInt(1); // Retourne le dernier ID inséré
                    }
                }
            }
            // Si la table n'est pas disponible ou aucune insertion n'a été faite
            return null;
        }
    }
    
    

    public static ObservableList<String> getUserTables(int userId) throws SQLException {
        ObservableList<String> tables = FXCollections.observableArrayList();
        String query = "SELECT id_table_client FROM commande WHERE id_user = ?";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int tableId = rs.getInt("id_table_client");
                tables.add("Table " + tableId);
            }
        }
        return tables;
    }


    
    
    
    
}
