package com.gdx.uch2;

import com.badlogic.gdx.Screen;

/**
 * Classe Singleton gèreant les différents écrans de jeu
 */
public class ScreenManager {

    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to game
    private UltimateChickenHorse2 game;

    private Screen placementScreen;

    // Singleton: private constructor
    private ScreenManager() {
    }

    /**
     * Obtient l'instance unique du singleton
     * @return l'instance unique du singleton
     */
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Initialization with the game class

    /**
     * Initialisation prenant la classe du jeu en paramètre
     * @param game le jeu à afficher
     */
    public void initialize(UltimateChickenHorse2 game) {
        this.game = game;
    }

    /**
     * Affiche l'écran passé en paramètre
     * @param screen écran à afficher
     */
    public void showScreen(Screen screen) {

        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Show new screen
        game.setScreen(screen);

        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }

    /**
     * Donne une valeur à l'écran de placement sauvegardé dans la classe
     * @param placementScreen la nouvelle valeur de l'écran de placement
     */
    public void setPlacementScreen(Screen placementScreen) {
        this.placementScreen = placementScreen;
    }

    /**
     * Obtient l'écran de placement sauvegardé dans la classe
     * @return l'écran de placement sauvegardé dans la classe
     */
    public Screen getPlacementScreen() {
        return placementScreen;
    }
}