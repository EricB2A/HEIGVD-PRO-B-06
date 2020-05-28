package com.gdx.uch2;

import com.badlogic.gdx.Game;
import com.gdx.uch2.ui.MainMenu;

/**
 * Classe principale du jeu, génère l'écran de menu principal
 */
public class UltimateChickenHorse2 extends Game {

	/**
	 * Constructeur sans arguments
	 */
	public UltimateChickenHorse2(){

	}

	@Override
	public void create() {
		ScreenManager.getInstance().initialize(this);
		MainMenu main = new MainMenu();
		setScreen(main);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
