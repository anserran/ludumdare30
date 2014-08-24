package com.metafisicainformatica.core.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.CharacterData.VarListener;

import java.util.HashMap;
import java.util.Map;

public class CharacterDebug extends Group implements VarListener {

	private Map<Var, Label> labels = new HashMap<Var, Label>();

	public CharacterDebug(Skin skin) {
		LabelStyle style = skin.get("score", LabelStyle.class);
		float y = 0;
		for (Var var : Var.values()) {
			Label label = new Label(var.toString() + ": 0", style);
			label.setPosition(10, y);
			y += 40;
			addActor(label);
			labels.put(var, label);
		}
	}


	@Override
	public void updated(Var var, int value) {
		Label label = labels.get(var);
		label.setText(var.toString() + ": " + value);
	}
}
