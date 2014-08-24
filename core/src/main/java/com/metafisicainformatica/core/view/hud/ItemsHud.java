package com.metafisicainformatica.core.view.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.CharacterData.VarListener;
import com.metafisicainformatica.core.view.CenteredDrawable;
import com.metafisicainformatica.core.view.VarLabel;


public class ItemsHud extends HorizontalGroup {

	public ItemsHud(Skin skin, CharacterData characterData) {
		space(30);
		addItemButton(skin, characterData, Var.highVisibilityItems, Var.highVisibility, "eye", "key-a");
		addItemButton(skin, characterData, Var.seeDemonsItems, Var.seeDemons, "demon", "key-s");
	}

	private void addItemButton(Skin skin, CharacterData characterData, Var itemCount, Var stepsRemaining, String style, String key) {
		ItemGroup group = new ItemGroup(characterData, itemCount, stepsRemaining, skin, style, key);
		addActor(group);
	}

	private Actor createLabel(Skin skin, CharacterData characterData, Var var, String style) {
		return new VarLabel(var, characterData, skin, style);
	}

	private class ItemGroup extends Group implements VarListener {

		private Var countVar;

		private Var stepsVar;

		private Actor count;
		private Actor steps;
		private CenteredDrawable keyActor;
		private ImageButton button;
		private CharacterData characterData;

		private ItemGroup(CharacterData characterData, Var countVar, Var stepsVar, Skin skin, String style, String key) {
			this.characterData = characterData;
			this.countVar = countVar;
			this.stepsVar = stepsVar;
			button = new ImageButton(skin.get(style, ImageButtonStyle.class));
			addActor(button);
			addActor(keyActor = new CenteredDrawable(skin.getDrawable(key)));
			setSize(button.getPrefWidth(), button.getPrefHeight());


			count = createLabel(skin, characterData, countVar, "itemCount");
			count.setPosition(getWidth() - count.getWidth() / 2.0f, -count.getHeight() / 2.0f);
			addActor(count);


			steps = createLabel(skin, characterData, stepsVar, "itemSteps");
			steps.setPosition(getWidth() - steps.getWidth() / 2.0f, getHeight() - steps.getHeight() / 2.0f);
			addActor(steps);
			characterData.addListener(this);
			disable();
		}

		@Override
		public void updated(Var var, int value) {
			if (var == countVar) {
				if (value == 0) {
					disable();
				} else {
					enable();
				}
			} else if (var == stepsVar) {
				if (value == 0) {
					disableSteps();
					if (characterData.get(countVar) == 0) {
						disable();
					}
				} else {
					enableSteps();
				}
			}
		}


		private void disable() {
			disableSteps();
			keyActor.setVisible(false);
			button.setColor(Color.DARK_GRAY);
			button.getColor().a = 0.1f;
			count.setVisible(false);
			steps.setVisible(false);
		}

		private void enable() {
			keyActor.setVisible(true);
			button.setColor(Color.WHITE);
			count.setVisible(true);
		}

		private void disableSteps() {
			steps.setVisible(false);
			count.setVisible(true);
			keyActor.setVisible(true);
			button.clearActions();
			button.setColor(Color.WHITE);
		}

		private void enableSteps() {
			if (!steps.isVisible()) {
				steps.setVisible(true);
				count.setVisible(false);
				keyActor.setVisible(false);
				button.addAction(Actions.forever(Actions.sequence(Actions.alpha(0.25f, 0.2f), Actions.alpha(1.0f, 0.2f))));
			}
		}
	}
}
