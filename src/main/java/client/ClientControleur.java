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
        modele.consulterCours(session);
        ArrayList<Course> liste = modele.getListeCours();
        vue.enleverListe();
        for(Course cours : liste) {
            vue.chargerCours(cours);
        }
    }

    public void inscription(String prenom, String nom, String email, String matricule, String code) {
        boolean succes = modele.inscrireCours(prenom, nom, email, matricule, code);
        if (succes) {
            vue.confirmerInscription(prenom, nom, code);
        } else {
            vue.echecInscription();
        }
    }
}
