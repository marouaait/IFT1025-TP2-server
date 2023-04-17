package server.models;

import java.io.Serializable;

/**
 * La classe RegistrationForm represente un formulaire d'inscription a un cours.
 */
public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * Constructeur de la classe RegistrationForm.
     * @param prenom Prenom de la personne.
     * @param nom Nom de famille de la personne.
     * @param email Email de la personne.
     * @param matricule Matricule de la personne.
     * @param course Cours auquel la personne s'inscrit.
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * Retourner le prenom de la personne.
     * @return Prenom de la personne.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Changer le prenom de la personne.
     * @param prenom Nouveau prenom.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourner le nom de famille de la personne.
     * @return Nom de famille de la personne.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Changer le nom de famille de la personne.
     * @param nom Nouveau nom de famille.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourner l'email de la personne.
      * @return Email de la personne.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changer l'email de la personne.
     * @param email Nouvel email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourner le matricule de la personne.
     * @return Matricule de la personne.
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Changer le matricule de la personne
     * @param matricule Matricule de la personne.
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Retourner le cours sur le formulaire.
     * @return Cours sur le formulaire.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Changer le cours sur le formulaire.
     * @param course Nouveau cours.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Retourne un String avec le prenom, nom de famille, email, matricule et cours sur le formulaire.
     * @return String representant le formulaire.
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
