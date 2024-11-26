package com.example.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Categorie {
     private final IntegerProperty id;
     private final StringProperty name;
     private final StringProperty couleur;
 
     public Categorie(int id, String name, String couleur) {
         this.id = new SimpleIntegerProperty(id);
         this.name = new SimpleStringProperty(name);
         this.couleur = new SimpleStringProperty(couleur); // Nouvelle propriété pour la couleur
     }
 
     public int getId() {
         return id.get();
     }
 
     public String getName() {
         return name.get();
     }
 
     public String getCouleur() {
         return couleur.get(); // Retourner la couleur hexadécimale
     }
 
     // Getters pour id et name
     public IntegerProperty idProperty() {
         return id;
     }
 
     public StringProperty nameProperty() {
         return name;
     }
 
     public StringProperty couleurProperty() {
         return couleur;
     }
 }
 