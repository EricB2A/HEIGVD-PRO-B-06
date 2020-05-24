package com.gdx.uch2;

import com.badlogic.gdx.Game;
import com.gdx.uch2.networking.client.GameClient;
import com.gdx.uch2.ui.MainMenu;

import java.io.IOException;

public class UltimateChickenHorse2 extends Game {
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
