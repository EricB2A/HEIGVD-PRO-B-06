package com.gdx.uch2;

import com.badlogic.gdx.Screen;

public class ScreenManager {

    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to game
    private UltimateChickenHorse2 game;

    private Screen placementScreen;

    // Singleton: private constructor
    private ScreenManager() {
        super();
    }

    // Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Initialization with the game class
    public void initialize(UltimateChickenHorse2 game) {
        this.game = game;
    }

    // Show in the game the screen which enum type is received
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

    public void setPlacementScreen(Screen placementScreen) {
        this.placementScreen = placementScreen;
    }

    public Screen getPlacementScreen() {
        return placementScreen;
    }
}