package server;

/**
 * EventHandler est une interface fonctionnelle avec la methode handle. L'interface est implementee par les traiteurs
 * de commande. Le traiteur traite la commande avec la methode handle.
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Traiter la commande recue.
     * @param cmd Nom de la commande
     * @param arg Les arguments passes avec la commande
     */
    void handle(String cmd, String arg);
}
