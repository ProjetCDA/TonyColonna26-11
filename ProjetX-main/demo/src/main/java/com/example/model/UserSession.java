package com.example.model;

public class UserSession {

    private static UserSession instance;

    private int idUser;
    private String identifiant;
    private String nom;

    // Constructeur privé pour le singleton
    private UserSession() {}

    // Méthode pour obtenir l'instance unique
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Getters et Setters
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Réinitialiser la session à la déconnexion
    public void clearSession() {
        idUser = 0;
        identifiant = null;
        nom = null;
    }
}
