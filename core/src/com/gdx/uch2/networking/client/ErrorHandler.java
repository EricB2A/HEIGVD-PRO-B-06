package com.gdx.uch2.networking.client;

/**
 * Singleton gérant les erreurs qui peuvent survenir durant l'exécution du programme
 */
public class ErrorHandler {
    private String error;
    private boolean set;

    private static class Instance {
        private static final ErrorHandler instance = new ErrorHandler();
    }

    private ErrorHandler() {
        set = false;
    }

    /**
     *
     * @return L'instance du singleton
     */
    public static ErrorHandler getInstance() {
        return Instance.instance;
    }

    /**
     *
     * @return True si une erreur a été recensée, false sinon
     */
    public boolean isSet() {
        return set;
    }

    /**
     * Recense une erreur
     * @param error Un message expliquant l'erreur qui a eu lieu
     */
    public void setError(String error) {
        System.out.println(error);
        this.error = error;
        set = true;
    }

    /**
     * @return L'erreur recensée
     */
    public String getError() {
        return error;
    }

    /**
     * Indique qu'aucune erreur n'est actuellement indiquée dans le singleton
     */
    public void reset() {
        set = false;
    }
}
