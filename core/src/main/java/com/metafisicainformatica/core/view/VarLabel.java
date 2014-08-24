package com.metafisicainformatica.core.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.CharacterData.VarListener;

public class VarLabel extends Label implements VarListener {

	private Skin skin;

	private String style;

	private Var var;

	private String prefix = "";

	private boolean animateDiff = false;

	private int currentValue;

	public VarLabel(Var var, CharacterData characterData, Skin skin, String style) {
		super(characterData.get(var) + "", skin, style);
		this.skin = skin;
		this.style = style;
		this.var = var;
		setSize(getPrefWidth(), getPrefHeight());
		setAlignment(Align.center, Align.center);
		characterData.addListener(this);
	}

	public VarLabel prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	@Override
	public void updated(Var var, int value) {
		if (this.var == var) {
			if (animateDiff) {
				addAnimation(value - currentValue);
			}
			currentValue = value;
			setText(prefix + value);
		}
	}

	private void addAnimation(int diff) {
		if (diff != 0) {
			Label label = new Label((diff >= 0 ? "+" : "") + diff + "", skin, style);
			label.setColor(diff >= 0 ? Color.GREEN : Color.RED);
			label.addAction(Actions.sequence(Actions.moveTo(getX(), getY()), Actions.moveBy(0, 50.0f, 2.0f), Actions.removeActor()));
			label.setPosition(getX(), getY());
			getParent().getParent().addActor(label);
		}
	}

	public VarLabel align(int align) {
		setAlignment(align, align);
		return this;
	}

	public VarLabel animateDiff() {
		animateDiff = true;
		return this;
	}
}
