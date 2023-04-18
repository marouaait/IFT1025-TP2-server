package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * La classe Server recoit et traite les requetes des clients concernant la liste des cours et l'inscription aux cours.
 */
public class Server {
    /**
     * REGISTER_COMMAND est le nom de la commande que le client envoie pour une requete d'inscription.
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * LOAD_COMMAND est le nom de la commande que le client envoie pour une requete de chargement de cours.
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur de la classe Server.
     * @param port Le port sur lequel le serveur est demarre.
     * @throws IOException Lancee s'il y a un probleme lors de la creation du ServerSocket.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajouter un traiteur d'un evenement au serveur.
     * @param h le traiteur de l'evenement.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Lancer le serveur. Le serveur se met a l'ecoute des connexions et des requetes des clients.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lire l'objet envoye par le client et alerter les traiteurs d'evenements de la requete recue.
     * @throws IOException Lancee s'il y a un probleme avec le flux client.
     * @throws ClassNotFoundException Lancee s'il y a un probleme avec la conversion de l'object recu.
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Transformer une requete recue en paire (commande, arguments).
     * @param line La requete recue
     * @return La paire (commande, arguments)
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Deconnecter le client.
     * @throws IOException Lancee s'il y a un probleme avec la fermeture des flux
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Traiter la commande recue. Cette methode peut traiter les commandes d'inscriptions et de chargement de cours.
     * @param cmd Nom de la commande
     * @param arg Argument de la commande
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Lire le fichier cours.txt contenant la liste des cours offerts, envoyer par le flux client la liste des cours
     * offerts dans la session specifiee par le client. L'objet envoye est une Liste&lt;Course&gt;. Les exceptions sont gerees
     * par la methode. Par defaut, le fichier cours.txt devrait etre dans le dossier d'execution.
     * @param arg la session pour laquelle on veut recuperer la liste des cours
     * @see Course
     */
    public void handleLoadCourses(String arg) {
        // TODO: implémenter cette méthode
        try {
            // Ouverture du fichier cours.txt
            //Scanner sc = new Scanner(new File("src/main/java/server/data/cours.txt")); // Pour tester dans le projet IntelliJ
            Scanner sc = new Scanner(new File("cours.txt"));

            List<Course> listeCours = new ArrayList<>();
            // Trouver tous les cours dans la session donnée
            while(sc.hasNext()) {
                String line = sc.nextLine();
                String[] cours = line.split("\t");
                if (cours[2].equals(arg)) {
                    listeCours.add(new Course(cours[1], cours[0], cours[2]));
                }
            }

            // Envoyer la liste de cours au client
            try {
                objectOutputStream.writeObject(listeCours);
            } catch (IOException e) {
                System.out.println("Erreur lors de l'écriture de l'objet dans le flux");
            }

            sc.close();

        } catch (IOException e) {
            System.out.println("Erreur à l'ouverture du fichier cours.txt");
        }
    }

    /**
     * Recuperer et lire l'objet 'RegistrationForm' envoye par le client, inscrire la personne du formulaire dans un
     * ficher inscription.txt et renvoyer un booleen de confirmation dans le flux client. Les exceptions sont gerees
     * par la methode. Par defaut, le fichier inscription.txt devrait etre dans le dossier d'execution.
     */
    public void handleRegistration() {
        try {
            // Lire le formulaire du client
            RegistrationForm rf = (RegistrationForm) objectInputStream.readObject();

            // Écriture dans le fichier
            //File fichierInscription = new File("src/main/java/server/data/inscription.txt"); // Pour tester dans le projet IntelliJ
            File fichierInscription = new File("inscription.txt");
            try {
                // Verifier que le fichier inscription.txt existe
                if (!fichierInscription.isFile()) {
                    throw new IOException();
                }

                FileWriter fw = new FileWriter(fichierInscription, true);
                BufferedWriter bw = new BufferedWriter(fw);

                Course cours = rf.getCourse();
                bw.append(cours.getSession() + "\t" +
                        cours.getCode() + "\t" +
                        rf.getMatricule() + "\t" +
                        rf.getPrenom() + "\t" +
                        rf.getNom() + "\t" +
                        rf.getEmail() + "\n");

                bw.close();
                fw.close();
            } catch (IOException e) {
                System.out.println("Erreur avec le fichier inscription.txt");
                objectOutputStream.writeObject(false);
            }

            // Confirmation au client
            objectOutputStream.writeObject(true);

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture de l'objet dans le flux");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur lors du retrait du formulaire client");
        }
    }
}

