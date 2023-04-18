package client;

import server.models.Course;

import java.util.ArrayList;

/**
 * La classe ClientControleur correspond a la composante Controleur du pattern MVC et sert a relayer l'information entre
 * ClientModele (composante Modele) et ClientVue (composante Vue)
 */
public class ClientControleur {
    private ClientVue vue;
    private ClientModele modele;

    /**
     * Constructeur de la classe ClientControleur.
     * @param vue Composante Vue
     * @param modele Composante Modele
     */
    public ClientControleur(ClientVue vue, ClientModele modele) {
        this.vue = vue;
        this.modele = modele;
    }

    /**
     * Charger les cours de la session donnee dans le Modele et la Vue.
     * @param session Session pour laquelle on charge les cours
     */
    public void chargerCoursSession(String session) {
        // Demander les cours au Modele
        boolean succes = modele.consulterCours(session);
        if (!succes) {
            vue.erreurFenetre("Erreur lors du chargement des cours.");
        }
        // Enlever la liste des cours et mettre les nouveaux cours dans la Vue
        ArrayList<Course> liste = modele.getListeCours();
        vue.enleverListe();
        for(Course cours : liste) {
            vue.chargerCours(cours);
        }
    }

    /**
     * Effectuer une inscription a un cours avec les parametres donnes.
     * @param prenom Prenom de la personne
     * @param nom Nom de famille de la personne
     * @param email Email de la personne
     * @param matricule Matricule de la personne
     * @param cours Cours auquel la personne s'inscrit
     */
    public void inscription(String prenom, String nom, String email, String matricule, Course cours) {
        // Enlever tous les contours d'erreur et seulement remettre ceux qui s'appliquent
        vue.enleverErreurContour();
        String messageErreur = "Le formulaire est invalide.\n";
        boolean erreur = false;
        // Verifier si un cours a ete selectionne
        if(cours == null) {
            erreur = true;
            vue.erreurCours();
            messageErreur += "Vous devez sélectionner un cours!\n";
        }
        // Verifier si l'email est de la forme x@y.z ou x,y,z sont des chaines characteres non vides
        if (!email.matches("\\S+@\\S+\\.\\S+")) {
            erreur = true;
            vue.erreurEmail();
            messageErreur += "Le champ 'Email' est invalide!\n";
        }
        // Verifier que le matricule est compose de 8 chiffres
        if (!matricule.matches("\\d{8}")) {
             erreur = true;
            vue.erreurMatricule();
            messageErreur += "Le champ 'Matricule' est invalide!\n";
        }
        // Afficher l'erreur s'il y en a une
        if (erreur) {
            vue.erreurFenetre(messageErreur);
            return;
        }

        // Inscrire la personne a l'aide du Modele et renvoyer le message du resultat (succes ou echec)
        boolean succes = modele.inscrireCours(prenom, nom, email, matricule, cours);
        if (succes) {
            String messageSucces = "Félicitations! " + prenom + " " + nom + " est inscrit(e) avec succès " +
                    "pour le cours " + cours.getCode() + "!";
            vue.confirmerInscription(messageSucces);
        } else {
            vue.erreurFenetre("Erreur lors de l'inscription.");
        }
    }
}
