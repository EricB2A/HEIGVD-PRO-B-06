package com.gdx.uch2;

import com.badlogic.gdx.Game;
import com.gdx.uch2.ui.MainMenu;

public class UltimateChickenHorse2 extends Game {
	public UltimateChickenHorse2(){

	}

	@Override
	public void create() {
		setScreen(new MainMenu());
	}
}
