package server;

/**
 * EventHandler est une interface fonctionnelle avec la méthode handle. L'interface est implementée par les traiteurs
 * de commande. Le traiteur traite la commande avec la méthode handle.
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Traiter la commande reçue.
     * @param cmd Nom de la commande
     * @param arg Les arguments passés avec la commande
     */
    void handle(String cmd, String arg);
}
