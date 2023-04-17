package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import server.models.Course;

import java.util.List;

public class ClientVue extends Application {
    private TableView<Course> tableCours;
    private TextField saisiesTexte[] = {new TextField(), new TextField(), new TextField(), new TextField()};

    public void chargerCours(Course cours) {
        tableCours.getItems().add(cours);
    }

    public void enleverListe() {
        tableCours.getItems().clear();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initialiser MVC
        ClientControleur controleur = new ClientControleur(this, new ClientModele("127.0.0.1", 1337));

        // Fenetre
        stage.setTitle("Inscription UdeM");
        HBox root = new HBox();
        root.setSpacing(30);
        root.setPadding(new Insets(30,30,30,30));

        // Partie liste de cours
        VBox listeCours = new VBox();
        listeCours.setSpacing(30);
        listeCours.setAlignment(Pos.CENTER);
        Text titreListeCours = new Text("Liste des cours");
        titreListeCours.setFont(Font.font(20));
        listeCours.getChildren().add(titreListeCours);

        // Table des cours
        tableCours = new TableView<Course>();
        TableColumn codeColonne = new TableColumn<Course, String>("Code");
        TableColumn coursColonne = new TableColumn<Course, String>("Cours");
        codeColonne.setCellValueFactory(new PropertyValueFactory<Course, String>("code"));
        coursColonne.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
        tableCours.setPrefWidth(400);
        tableCours.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        codeColonne.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        coursColonne.setMaxWidth(1f * Integer.MAX_VALUE * 70);

        tableCours.getColumns().addAll(codeColonne,coursColonne);
        listeCours.getChildren().add(tableCours);

        // Bouton charger et selection de session
        HBox boutons = new HBox();
        ComboBox sessions = new ComboBox();
        sessions.getItems().addAll("Automne", "Hiver", "Ete");
        sessions.getSelectionModel().selectFirst();
        Button boutonCharger = new Button("charger");
        boutons.getChildren().addAll(sessions, boutonCharger);
        boutons.setSpacing(50);

        boutonCharger.setOnAction((action) -> {
            controleur.chargerCoursSession(sessions.getValue().toString());
        });

        listeCours.getChildren().add(boutons);

        root.getChildren().add(listeCours);

        root.getChildren().add(new Separator(Orientation.VERTICAL));

        // Partie formulaire
        VBox formulaire = new VBox();
        formulaire.setSpacing(50);
        formulaire.setAlignment(Pos.CENTER);
        Text titreFormulaire = new Text("Formulaire d'inscription");
        titreFormulaire.setFont(Font.font(20));
        formulaire.getChildren().add(titreFormulaire);

        // Zone de saisie de texte
        Text prenomTexte = new Text("Prénom");
        Text nomTexte = new Text("Nom");
        Text emailTexte = new Text("Email");
        Text matriculeTexte = new Text("Matricule");
        GridPane saisies = new GridPane();
        saisies.setVgap(8);
        saisies.setHgap(15);
        saisies.setAlignment(Pos.CENTER);
        saisies.addColumn(0,prenomTexte,nomTexte,emailTexte,matriculeTexte);
        saisies.addColumn(1,saisiesTexte);
        formulaire.getChildren().add(saisies);

        // Bouton envoyer
        Button boutonEnvoyer = new Button("envoyer");
        formulaire.getChildren().add(boutonEnvoyer);

        boutonEnvoyer.setOnAction((action) -> {
            Course cours = tableCours.getSelectionModel().getSelectedItem();
            String prenom = saisiesTexte[0].getText();
            String nom = saisiesTexte[1].getText();
            String email = saisiesTexte[2].getText();
            String matricule = saisiesTexte[3].getText();

            controleur.inscription(prenom, nom, email, matricule, cours);
        });

        root.getChildren().add(formulaire);

        // Afficher la scene
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void confirmerInscription(String prenom, String nom, Course cours) {
        for (TextField saisie : saisiesTexte) {
            saisie.clear();
        }
    }

    public void echecInscription() {

    }

    public void erreurFormulaire(String texteErreur) {
        
    }

    public static void main(String[] args) {
        ClientVue.launch(args);
    }
}
