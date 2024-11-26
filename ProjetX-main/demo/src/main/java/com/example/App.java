package com.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.example.DAO.DataUser;
import com.example.model.UserSession;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Charger l'FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();  // Charger la vue depuis l'FXML
    
        // Récupérer le contrôleur de la scène
        PrimaryController controller = loader.getController();
        controller.setStage(stage);  // Passer le stage au contrôleur
    
        // Créer la scène et l'initialiser
        scene = new Scene(root, 450, 785);
    
        Image icon = new Image(getClass().getResourceAsStream("/com/example/img/logoApp3.png"));
        stage.getIcons().add(icon);
    
        // Ajouter la feuille de style
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    
        // Initialiser la scène et afficher
        stage.setScene(scene);
        stage.show();
    
        // Montrer le dialogue de démarrage (si nécessaire)
        showStartupDialog(stage);
    }

    private void showStartupDialog(Stage stage) {
        
        Dialog<String> dialog = new Dialog<>();
        dialog.initOwner(stage);

        dialog.setTitle("Connexion");
        dialog.setHeaderText(null);

        // Création des éléments de la boîte de dialogue
        Label label = new Label("IDENTIFIANT");
        label.getStyleClass().add("labelIdentifiant");

        TextField textField = new TextField();
        textField.setPromptText("Entrez votre identifiant");

        Button buttonValider = new Button("Valider");
        buttonValider.setOnAction(e -> {
            String input = textField.getText();
            if (input.isEmpty()) {
                System.out.println("Identifiant vide !");
            } else {
                try {
                    // Vérifie l'identifiant dans la base
                    if (DataUser.authenticate(input)) {
                        String nom = UserSession.getInstance().getNom();
                        System.out.println("Connexion réussie : " + nom);

                        dialog.setResult(input);
                        dialog.close();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                String nom = UserSession.getInstance().getNom();
                                String message = nom != null ? "Bienvenue, " + nom : "Utilisateur non connecté";
                                ((Label) scene.lookup("#idUtilisateur")).setText(message);
                            }
                        });
                    } else {
                        System.out.println("Identifiant invalide !");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox vbox = new VBox(10, label, textField, buttonValider);
        vbox.getStyleClass().add("containerVBox");

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().setPrefWidth(300);
        dialog.getDialogPane().setPrefHeight(200);

        ButtonType closeButtonType = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);
        dialog.getDialogPane().lookupButton(closeButtonType).setVisible(false);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == closeButtonType) {
                return null;
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Utilisateur connecté : " + result.get());
        } else {
            System.out.println("La boîte de dialogue a été fermée avec la croix.");
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
