package client;

public class ClientControleur {
    private ClientVue vue;
    private ClientModele modele;

    public ClientControleur(ClientVue vue, ClientModele modele) {
        this.vue = vue;
        this.modele = modele;
    }
}
