package com.example.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SousCategorie {
    private int id;
    private String nom;
    private int idCategorie;
    private final StringProperty couleur;

    // Constructeur avec une valeur initiale pour la couleur
    public SousCategorie(int id, String nom, int idCategorie, String couleur) {
        this.id = id;
        this.nom = nom;
        this.idCategorie = idCategorie;
        this.couleur = new SimpleStringProperty(couleur); // Initialisation de la couleur avec la valeur passée
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getCouleur() {
        return couleur.get(); // Retourner la couleur hexadécimale
    }

    public void setCouleur(String couleur) {
        this.couleur.set(couleur); // Permet de mettre à jour la couleur
    }

    // Méthode toString pour afficher le nom
    @Override
    public String toString() {
        return nom;
    }

    // Pour obtenir la propriété couleur
    public StringProperty couleurProperty() {
        return couleur;
    }
}
