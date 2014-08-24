package com.metafisicainformatica.java;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class GenerateSkin {


	public static void main(String[] args) {
		Settings settings = new Settings();
		TexturePacker.process(settings, "/home/bender/repositories/ludumdare/ludumdare30/png", "/home/bender/repositories/ludumdare/ludumdare30/assets", "skin");
	}
}
