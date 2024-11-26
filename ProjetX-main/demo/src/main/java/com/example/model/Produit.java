package com.example.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Produit {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty categoryId;
    private final StringProperty couleur; // Ajout de la propriété couleur
    private final ObservableList<Supplement> listSupplements;

    public Produit(int id, String name, int categoryId, String couleur, ObservableList<Supplement> listSupplements) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.categoryId = new SimpleIntegerProperty(categoryId);
        this.couleur = new SimpleStringProperty(couleur);  // Initialisation de la couleur
        this.listSupplements = new SimpleListProperty<Supplement>(listSupplements);
    }

    public ObservableList<Supplement> getSupplements() {return listSupplements;}
    public void setSupplements(ObservableList<Supplement> listSupplements) {this.listSupplements.setAll(listSupplements);}
    public ObservableList<Supplement> supplementsProperty() {return listSupplements;}

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public int getCategoryId() { return categoryId.get(); }
    public void setCategoryId(int categoryId) { this.categoryId.set(categoryId); }
    public IntegerProperty categoryIdProperty() { return categoryId; }

    public String getCouleur() { return couleur.get(); } // Méthode pour récupérer la couleur
    public void setCouleur(String couleur) { this.couleur.set(couleur); } // Méthode pour définir la couleur
    public StringProperty couleurProperty() { return couleur; } // Retourne la propriété couleur

    @Override
    public String toString() {
        return "Produit [ID=" + id + ", Nom=" + name + "]";
    }
}
