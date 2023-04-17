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

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
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

    public boolean inscrireCours(String prenom, String nom, String email, String matricule, String codeCours) {
        int i = 0;
        for (; i < listeCours.size(); i++) {
            if (listeCours.get(i).getCode().equals(codeCours)) {
                break;
            }
        }

        if (i == listeCours.size()) {
            return false;
        }

        RegistrationForm formulaire = new RegistrationForm(prenom, nom, email, matricule, listeCours.get(i));

        try {
            this.connect();
            os.writeObject("INSCRIRE ");
            os.writeObject(formulaire);
            os.flush();
            this.disconnect();
            return true;
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void consulterCours(String session) {
        try {
            this.connect();
            os.writeObject("CHARGER " + session);
            os.flush();
            listeCours = (ArrayList) is.readObject();
            this.disconnect();
        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Course> getListeCours() {
        return new ArrayList<>(listeCours);
    }
}
