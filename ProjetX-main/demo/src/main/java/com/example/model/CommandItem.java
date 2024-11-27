package com.example.model;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandItem {
    private final IntegerProperty id;
    private final ObjectProperty<Produit> produit;
    private final BooleanProperty etat;
    private final IntegerProperty quantite;
    private final ListProperty<Supplement> listSupplements;

    public CommandItem() {
        this.id = new SimpleIntegerProperty();
        this.produit = new SimpleObjectProperty<>();
        this.etat = new SimpleBooleanProperty();
        this.quantite = new SimpleIntegerProperty();
        this.listSupplements = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    // Getter et Setter pour id
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Getter et Setter pour produit
    public Produit getProduit() {
        return produit.get();
    }

    public void setProduit(Produit produit) {
        this.produit.set(produit);
    }

    public ObjectProperty<Produit> produitProperty() {
        return produit;
    }

    // Getter et Setter pour etat
    public boolean isEtat() {
        return etat.get();
    }

    public void setEtat(boolean etat) {
        this.etat.set(etat);
    }

    public BooleanProperty etatProperty() {
        return etat;
    }

    // Getter et Setter pour quantite
    public int getQuantite() {
        return quantite.get();
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    // Getter et Setter pour listSupplements
    public ObservableList<Supplement> getListSupplements() {
        return listSupplements.get();
    }

    public void setListSupplements(ObservableList<Supplement> listSupplements) {
        this.listSupplements.set(listSupplements);
    }

    public ListProperty<Supplement> listSupplementsProperty() {
        return listSupplements;
    }
}
