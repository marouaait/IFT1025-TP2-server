package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * La classe ClientSimple est un client en ligne de commande permettant d'envoyer des requêtes de chargement de cours
 * et d'inscription au serveur.
 */
public class ClientSimple {
    private String ip;
    private int port;
    private Socket serveur;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private static String[] sessionsOffertes = {"Automne", "Hiver", "Ete"};
    private ArrayList<Course> listeCours = new ArrayList<>();
    private String session;
    private Scanner sc = new Scanner(System.in);

    /**
     * Constructeur de la classe ClientSimple
     * @param ip Adresse ip à laquelle la connexion se fait
     * @param port Port sur lequel la connexion se fait
     */
    public ClientSimple(String ip, int port) {
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
     * Déconnecter du serveur en fermant le socket et les flux serveurs
     * @throws IOException
     */
    private void disconnect() throws  IOException {
        os.close();
        is.close();
        serveur.close();
    }

    /**
     *  Envoyer une requête d'inscription au serveur en demandant la saisie de l'utilisateur en ligne de commande.
     */
    private void inscrireCours() {
        // Demander les informations
        System.out.print("\nVeuillez saisir votre prénom: ");
        String prenom = sc.nextLine();
        System.out.print("Veuillez saisir votre nom: ");
        String nom = sc.nextLine();
        System.out.print("Veuillez saisir votre email: ");
        String email = sc.nextLine();
        System.out.print("Veuillez saisir votre matricule: ");
        String matricule = sc.nextLine();
        System.out.print("Veuillez saisir le code du cours: ");
        String codeCours = sc.nextLine();

        // Trouver le cours en utilisant le code fourni
        int i = 0;
        for (; i < listeCours.size(); i++) {
            if (listeCours.get(i).getCode().equals(codeCours)) {
                break;
            }
        }

        // Si on a parcouru toute la liste sans trouver c'est que le cours n'est pas charge
        if (i == listeCours.size()) {
            System.out.println("Ce cours n'est pas offert pour la session d'" + session.toLowerCase());
            return;
        }

        // Creer le formulaire et envoyer la requete d'inscription
        RegistrationForm formulaire = new RegistrationForm(prenom, nom, email, matricule, listeCours.get(i));

        try {
            this.connect();
            os.writeObject("INSCRIRE ");
            os.writeObject(formulaire);
            os.flush();
            boolean confirmation = (boolean) is.readObject();
            this.disconnect();
            // Afficher le succes ou l'echec de la requete
            if (confirmation) {
                System.out.println("\nFélicitations! Inscription réussie de " + prenom + " au cours " + codeCours);
            } else {
                System.out.println("Inscription échouée");
            }
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            System.out.println("Erreur avec le flux serveur");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur avec la confirmation du serveur");
        }
    }

    /**
     * Envoyer au serveur une requête de chargement de cours en demandant la session voulue à l'utilisateur
     */
    public void consulterCours() {
        // Demander et afficher les sessions offertes
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        for (int i = 1; i <= sessionsOffertes.length; i++) {
            System.out.println(i + ". " + sessionsOffertes[i-1]);
        }

        // Saisir l'entree de l'utilisateur
        Scanner sc = new Scanner(System.in);
        System.out.print("> Choix: ");
        int choixSession = sc.nextInt();
        sc.nextLine();

        // S'assurer que le choix est valide
        while(choixSession > sessionsOffertes.length) {
            System.out.println("Veuillez entrer un choix valide.");
            System.out.print("> Choix: ");
            choixSession = sc.nextInt();
            sc.nextLine();
        }

        // Afficher les cours offerts en envoyant une requete de chargement au serveur avec la session fournie
        session = sessionsOffertes[choixSession - 1];
        System.out.println("Les cours offerts pendant la session d'" + session.toLowerCase() + " sont:");
        try {
            this.connect();
            os.writeObject("CHARGER " + session);
            os.flush();
            // Recuperer la liste des cours du serveur et afficher le code et le nom pour chaque cours
            listeCours = (ArrayList) is.readObject();
            for (int i = 0; i < listeCours.size(); i++) {
                System.out.println((i+1) + ". " + listeCours.get(i).getCode() + "\t" + listeCours.get(i).getName());
            }
            this.disconnect();
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            System.out.println("Erreur avec le flux serveur");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur lors du retrait de la liste de cours du serveur");
        }
    }

    /**
     * Démarrer le menu du client. Le menu a trois options: consulter des cours, effectuer une inscription et quitter le
     * client. L'utilisateur interagit en ligne de commande.
     */
    public void menu() {
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");

        // Commencer par demander les cours a afficher
        consulterCours();

        // Boucle principale du menu
        while(true) {
            // Offrir les options au client
            System.out.println("> Choix:\n" +
                    "1. Consulter les cours offerts pour une autre session\n" +
                    "2. Inscription à un cours\n" +
                    "3. Quitter\n" +
                    "> Choix: ");

            int choix = sc.nextInt();
            sc.nextLine();

            if (choix == 1) {
                consulterCours();
            } else if (choix == 2) {
                inscrireCours();
            } else if (choix == 3) {
                break;
            } else {
                System.out.println("Veuillez entrer un choix valide.");
            }
        }

        // Fermer l'application
        System.out.println("Au revoir!");
    }

    /**
     * Démarrer une application client en ligne de commande.
     * @param args
     */
    public static void main(String[] args) {
        ClientSimple client = new ClientSimple("127.0.0.1", 1337);
        client.menu();
    }
}
