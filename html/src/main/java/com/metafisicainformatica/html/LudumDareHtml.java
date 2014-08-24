package com.metafisicainformatica.html;

import com.metafisicainformatica.core.LudumDare;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class LudumDareHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new LudumDare();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(1066, 600);
	}
}
