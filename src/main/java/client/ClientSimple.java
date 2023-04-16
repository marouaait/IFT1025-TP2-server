package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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

    public ClientSimple(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    private void connect() throws IOException {
        System.out.println("A");
        serveur = new Socket(this.ip, this.port);
        os = new ObjectOutputStream(serveur.getOutputStream());
        System.out.println("B");
        is = new ObjectInputStream(serveur.getInputStream());
        System.out.println("C");
    }

    private void disconnect() throws  IOException {
        os.close();
        serveur.close();
    }

    private void inscrireCours() {
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

        int i = 0;
        for (; i < listeCours.size(); i++) {
            if (listeCours.get(i).getCode().equals(codeCours)) {
                break;
            }
        }

        if (i == listeCours.size()) {
            System.out.println("Ce cours n'est pas offert pour la session d'" + session);
            return;
        }

        RegistrationForm formulaire = new RegistrationForm(prenom, nom, email, matricule, listeCours.get(i));

        try {
            this.connect();
            os.writeObject("INSCRIRE ");
            os.flush();
            this.connect();
            System.out.println("2");
            os.writeObject(formulaire);
            os.flush();
            //this.disconnect();
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void consulterCours() {
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        for (int i = 1; i <= sessionsOffertes.length; i++) {
            System.out.println(i + ". " + sessionsOffertes[i-1]);
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("> Choix: ");
        int choixSession = sc.nextInt();
        sc.nextLine();

        while(choixSession > sessionsOffertes.length) {
            System.out.println("Veuillez entrer un choix valide.");
            System.out.print("> Choix: ");
            choixSession = sc.nextInt();
            sc.nextLine();
        }

        session = sessionsOffertes[choixSession - 1];
        System.out.println("Les cours offerts pendant la session d'" + session.toLowerCase() + " sont:");
        try {
            this.connect();
            os.writeObject("CHARGER " + session);
            os.flush();
            listeCours = (ArrayList) is.readObject();
            for (int i = 0; i < listeCours.size(); i++) {
                System.out.println((i+1) + ". " + listeCours.get(i).getCode() + "\t" + listeCours.get(i).getName());
            }
            //this.disconnect();
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void menu() {
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");

        consulterCours();

        while(true) {
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

        System.out.println("Au revoir!");
    }

    public static void main(String[] args) {
        ClientSimple client = new ClientSimple("127.0.0.1", 1337);
        client.menu();
    }
}
