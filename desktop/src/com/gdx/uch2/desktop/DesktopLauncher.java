package com.gdx.uch2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.uch2.UltimateChickenHorse2;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 675;
		config.fullscreen = true;
		new LwjglApplication(new UltimateChickenHorse2(), config);
	}
}
