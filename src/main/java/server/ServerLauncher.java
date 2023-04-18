package server;

/**
 * La classe ServerLauncher sert à demarrer une instance de la classe Server sur un port spécifique.
 */
public class ServerLauncher {
    /**
     * Le port sur lequel le serveur s'ouvre. Par défaut, le port est le 1337.
     */
    public final static int PORT = 1337;

    /**
     * Lancer le serveur sur le port.
     * @param args
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}