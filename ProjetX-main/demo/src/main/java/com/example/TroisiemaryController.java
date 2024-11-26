package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TroisiemaryController {
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void ToutEncaisser(ActionEvent event) {

        Dialog<String> dialog = new Dialog<>();

Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        dialog.initOwner(currentStage);

        dialog.setTitle("Confirmation");
        dialog.setHeaderText("Tu veux vraiment faire ça, ma gueule ?");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Button buttonValider = new Button("Valider");
        Button buttonAnnuler = new Button("Annuler");

        buttonValider.setOnAction(e -> {
            dialog.setResult("Validé");
            dialog.close();
        });

        buttonAnnuler.setOnAction(e -> {
            dialog.setResult("Annulé");
            dialog.close();
        });

        HBox hbox = new HBox(10, buttonValider, buttonAnnuler);
        hbox.setStyle("-fx-alignment: center;");

        VBox vbox = new VBox(10, new Label("Confirmez l'encaissement des commandes."), hbox);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.CANCEL) {
                return "Annulé par fermeture";
            }
            return dialog.getResult();
        });

        dialog.showAndWait().ifPresent(response -> {
            System.out.println("Résultat du Dialog : " + response);
        });
    }

}
