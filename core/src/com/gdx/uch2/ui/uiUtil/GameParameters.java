package com.gdx.uch2.ui.uiUtil;

/**
 * Regroupe les paramètres d'une nouvelle partie
 */
public class GameParameters {
    public final String hostname;
    public final String nickname;
    public final int port;

    /**
     * Constructeur
     * @param hostname le hostname du créateur de la partie
     * @param nickname le pseudonyme utilisé par le joueur
     * @param port le port sur lequel communique le serveur
     */
    public GameParameters(String hostname, String nickname, int port) {
        this.hostname = hostname;
        this.nickname = nickname;
        this.port = port;
    }
}
