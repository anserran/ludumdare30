package com.metafisicainformatica.core.view.scene;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.metafisicainformatica.core.model.*;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.CharacterData.VarListener;

public class Demon extends NodeActor implements VarListener {

	public Demon(Skin skin, CharacterData characterData) {
		setName("demon");
		setDrawable(skin.getDrawable("demon"));
		setOrigin(getWidth() / 2.0f, getHeight() / 2.0f);
		setX(-20);
		getColor().a = 0.75f;
		Action moveRight = Actions.moveBy(40, 0, 0.7f);
		Action moveLeft = Actions.moveBy(-40, 0, 0.7f);
		addAction(Actions.forever(Actions.sequence(moveRight, moveLeft)));
		characterData.addListener(this);
		setVisible(characterData.get(Var.seeDemons) > 0);
	}

	@Override
	public void updated(Var var, int value) {
		if (var == Var.seeDemons) {
			setVisible(value > 0);
		}
	}

	public void goAway() {
		setVisible(true);
		addAction(Actions.sequence(Actions.parallel(Actions.scaleTo(20, 20, 1.0f), Actions.alpha(0.0f, 1.0f)), Actions.removeActor()));
	}
}
