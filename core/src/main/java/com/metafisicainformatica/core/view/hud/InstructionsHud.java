package com.metafisicainformatica.core.view.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class InstructionsHud extends Table {

	private Label label;

	private float prefWidth;

	public InstructionsHud(Skin skin) {
		label = new Label("", skin, "instructions");
		label.setWrap(true);
		add(label).expandX().fillX().pad(10);
		label.setAlignment(Align.center, Align.center);
		background(skin.getDrawable("instructions-bg"));
		prefWidth = 400;
	}

	public void setPrefWidth(float prefWidth) {
		this.prefWidth = prefWidth;
		invalidateHierarchy();
	}

	@Override
	public float getPrefWidth() {
		return prefWidth;
	}

	public void setText(String text) {
		text = text == null ? "" : text;
		label.setText(text);
		label.invalidateHierarchy();
		invalidateHierarchy();
	}
}
