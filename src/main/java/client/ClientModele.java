package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * La classe ClientModele correspond a la composante Modele du pattern MVC et ClientModele communique directement avec
 * le serveur en lui passant des requetes et en retirant l'information recue du serveur.
 */
public class ClientModele {
    private String ip;
    private int port;
    private Socket serveur;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private ArrayList<Course> listeCours = new ArrayList<>();

    /**
     * Constructeur de la classe ClientModele
     * @param ip Adresse ip a laquelle la connexion se fait
     * @param port Port sur lequel la connexion se fait
     */
    public ClientModele(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Connecter au serveur et initialiser les flux serveurs
     * @throws IOException
     */
    private void connect() throws IOException {
        serveur = new Socket(this.ip, this.port);
        os = new ObjectOutputStream(serveur.getOutputStream());
        is = new ObjectInputStream(serveur.getInputStream());
    }

    /**
     * Deconnecter du serveur en fermant le socket et les flux serveurs
     * @throws IOException
     */
    private void disconnect() throws  IOException {
        os.close();
        is.close();
        serveur.close();
    }

    /**
     * Envoyer une requete d'inscription au serveur et retourner le succes ou l'echec de la requete.
     * @param prenom Prenom de la personne
     * @param nom Nom de famille de la personne
     * @param email Email de la personne
     * @param matricule Matricule de la personne
     * @param cours Cours auquel la personne s'inscrit
     * @return Booleen indiquant la reussite ou l'echec de la requete
     */
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

    /**
     * Envoyer au serveur une requete de chargement de cours pour la session donnee et
     * retourner le succes ou l'echec de la requete
     * @param session Session pour laquelle le chargement des cours se fait
     * @return Booleen indiquant la reussite ou l'echec de la requete
     */
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

    /**
     * Retourner une copie de la liste des cours charges.
     * @return Copie de la liste des cours charges
     */
    public ArrayList<Course> getListeCours() {
        return new ArrayList<>(listeCours);
    }
}
