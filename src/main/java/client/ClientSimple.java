package client;

import server.models.Course;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientSimple {
    private String ip;
    private int port;
    private Socket serveur;
    private ObjectOutputStream os;
    private static String[] sessions = {"Automne", "Hiver", "Ete"};
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
        serveur = new Socket(this.ip, this.port);
        os = new ObjectOutputStream(serveur.getOutputStream());
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
        String cours = sc.nextLine();

        try {
            this.connect();
            os.writeObject("INSCRIRE " + prenom + " " + nom + " " + email + " " + matricule + " " + cours + "\n");
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
        for (int i = 1; i <= sessions.length; i++) {
            System.out.println(i + ". " + sessions[i-1]);
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("> Choix: ");
        int choixSession = sc.nextInt();
        sc.nextLine();

        while(choixSession > sessions.length) {
            System.out.println("Veuillez entrer un choix valide.");
            System.out.print("> Choix: ");
            choixSession = sc.nextInt();
            sc.nextLine();
        }

        String session = sessions[choixSession - 1];
        System.out.println("Les cours offerts pendant la session d'" + session.toLowerCase() + " sont:");
        try {
            this.connect();
            os.writeObject("CHARGER " + session + "\n");
            os.flush();
            //this.disconnect();
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
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
