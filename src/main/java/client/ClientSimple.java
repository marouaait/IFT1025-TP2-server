package client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSimple {
    private String ip;
    private int port;
    private Socket serveur;
    private ObjectOutputStream os;

    public ClientSimple(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private void connect() throws IOException {
        serveur = new Socket(this.ip, this.port);
        os = new ObjectOutputStream(serveur.getOutputStream());
    }

    private void disconnect() throws  IOException{
        serveur.close();
        os.close();
    }

    private void afficherCours(String session) {
        try {
            this.connect();
            os.writeObject("CHARGER " + session + "\n");
            os.flush();
            this.disconnect();
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inscrireCours(String prenom, String nom, String email, String matricule, String cours) {
        try {
            this.connect();
            os.writeObject("INSCRIRE " + prenom + " " + nom + " " + email + " " + matricule + " " + cours + "\n");
            os.flush();
            this.disconnect();
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public static void main(String[] args) {
        ClientSimple client = new ClientSimple("127.0.0.1", 1337);
        client.afficherCours("AUTOMNE2022");
        client.inscrireCours("Maroua", "Ait El Baz", "marouaelbaz", "123", "456");
    }
}
