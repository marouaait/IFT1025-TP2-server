package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.*;

public class ClientGUI extends Application {
    public static void main(String[] args) {
        ClientGUI.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Inscription UdeM");
        TilePane root = new TilePane();

        // Partie liste de cours


        // Partie formulaire
        TilePane formulaire = new TilePane(Orientation.VERTICAL);
        formulaire.setVgap(10);
        formulaire.setTileAlignment(Pos.CENTER);
        Text titreFormulaire = new Text("Formulaire d'inscription");
        titreFormulaire.setFont(Font.font(20));
        formulaire.getChildren().add(titreFormulaire);

        // Zone de saisie de texte
        Text prenomTexte = new Text("Prénom");
        TextField prenom = new TextField();
        Text nomTexte = new Text("Nom");
        TextField nom = new TextField();
        Text emailTexte = new Text("Email");
        TextField email = new TextField();
        Text matriculeTexte = new Text("Matricule");
        TextField matricule = new TextField();
        GridPane saisies = new GridPane();
        saisies.setVgap(8);
        saisies.setHgap(15);
        saisies.setAlignment(Pos.CENTER);
        saisies.addColumn(0,prenomTexte,nomTexte,emailTexte,matriculeTexte);
        saisies.addColumn(1,prenom,nom,email,matricule);
        formulaire.getChildren().add(saisies);

        // Bouton
        Button boutonEnvoyer = new Button("envoyer");
        formulaire.getChildren().add(boutonEnvoyer);

        root.getChildren().add(formulaire);

        // Afficher la scene
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
