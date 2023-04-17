package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientModele {
    private String ip;
    private int port;
    private Socket serveur;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private ArrayList<Course> listeCours = new ArrayList<>();

    public ClientModele(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private void connect() throws IOException {
        serveur = new Socket(this.ip, this.port);
        os = new ObjectOutputStream(serveur.getOutputStream());
        is = new ObjectInputStream(serveur.getInputStream());
    }

    private void disconnect() throws  IOException {
        os.close();
        serveur.close();
    }

    public boolean inscrireCours(String prenom, String nom, String email, String matricule, Course cours) {
        RegistrationForm formulaire = new RegistrationForm(prenom, nom, email, matricule, cours);

        try {
            this.connect();
            // Envoyer la requête avec le formulaire
            os.writeObject("INSCRIRE ");
            os.writeObject(formulaire);
            os.flush();
            // Recevoir la confirmation du serveur
            // On assume que si on ne reçoit pas de confirmation l'inscription a échoué
            boolean confirmation = (boolean) is.readObject();
            this.disconnect();

            return confirmation;
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            System.out.println("Erreur avec le flux serveur");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur avec la confirmation du serveur");
        }
        return false;
    }

    public boolean consulterCours(String session) {
        try {
            this.connect();
            os.writeObject("CHARGER " + session);
            os.flush();
            listeCours = (ArrayList) is.readObject();
            this.disconnect();
            return true;
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            System.out.println("Erreur avec le flux serveur.");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur lors du retrait de la liste de cours du serveur");
        }
        return false;
    }

    public ArrayList<Course> getListeCours() {
        return new ArrayList<>(listeCours);
    }
}
