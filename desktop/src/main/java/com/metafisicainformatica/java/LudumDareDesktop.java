package com.metafisicainformatica.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.metafisicainformatica.core.LudumDare;

public class LudumDareDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1066;
		config.height = 600;
		new LwjglApplication(new LudumDare(), config);
	}
}
