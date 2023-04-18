package server.models;

import java.io.Serializable;

/**
 * La classe Course représente un cours offert avec un nom, un code et la session d'enseignement.
 */
public class Course implements Serializable {

    private String name;
    private String code;
    private String session;

    /**
     * Constructeur de la classe Course.
     * @param name Nom du cours
     * @param code Code du cours
     * @param session Session du cours
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * Retourner le nom du cours.
     * @return Nom du cours.
     */
    public String getName() {
        return name;
    }

    /**
     * Changer le nom du cours.
     * @param name Nouveau nom du cours.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retourner le code du cours.
     * @return Code du cours.
     */
    public String getCode() {
        return code;
    }

    /**
     * Changer le code du cours.
     * @param code Nouveau code du cours.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Retourner la session du cours.
     * @return La session du cours.
     */
    public String getSession() {
        return session;
    }

    /**
     * Changer la session du cours
     * @param session Nouvelle session du cours
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Retourner un String avec le nom, le code et la session du cours.
     * @return String representant le cours.
     */
    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
