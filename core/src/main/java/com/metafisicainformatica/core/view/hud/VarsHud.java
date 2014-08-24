package com.metafisicainformatica.core.view.hud;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.view.VarLabel;

public class VarsHud extends HorizontalGroup {


	public VarsHud(Skin skin, CharacterData characterData) {
		this.space(15);
		Var[] vars = new Var[]{Var.age, Var.health, Var.money, Var.love};
		for (Var var : vars) {
			addActor(new Image(skin, var.toString()));
			Label label = new VarLabel(var, characterData, skin, "score").animateDiff();
			addActor(label);
		}
	}
}
