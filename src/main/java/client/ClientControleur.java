package client;

import server.models.Course;

import java.util.ArrayList;

public class ClientControleur {
    private ClientVue vue;
    private ClientModele modele;

    public ClientControleur(ClientVue vue, ClientModele modele) {
        this.vue = vue;
        this.modele = modele;
    }

    public void chargerCoursSession(String session) {
        boolean succes = modele.consulterCours(session);
        if (!succes) {
            vue.erreurFenetre("Erreur lors du chargement des cours.");
        }
        ArrayList<Course> liste = modele.getListeCours();
        vue.enleverListe();
        for(Course cours : liste) {
            vue.chargerCours(cours);
        }
    }

    public void inscription(String prenom, String nom, String email, String matricule, Course cours) {
        vue.enleverErreurContour();
        String messageErreur = "Le formulaire est invalide.\n";
        boolean erreur = false;
        if(cours == null) {
            erreur = true;
            vue.erreurCours();
            messageErreur += "Vous devez sélectionner un cours!\n";
        }
        if (!email.matches("\\S+@\\S+\\.\\S+")) {
            erreur = true;
            vue.erreurEmail();
            messageErreur += "Le champ 'Email' est invalide!\n";
        }
        if (!matricule.matches("\\d{8}")) {
             erreur = true;
            vue.erreurMatricule();
            messageErreur += "Le champ 'Matricule' est invalide!\n";
        }

        if (erreur) {
            vue.erreurFenetre(messageErreur);
            return;
        }

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
