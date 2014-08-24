package com.metafisicainformatica.core.view.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.metafisicainformatica.core.LudumDare;
import com.metafisicainformatica.core.controller.GraphController.NodeListener;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.CharacterData.VarListener;
import com.metafisicainformatica.core.model.nodes.Node;
import com.metafisicainformatica.core.view.VarLabel;
import com.metafisicainformatica.core.view.scene.Scene;

public class Hud extends Table implements VarListener, NodeListener {

	private InstructionsHud instructions;

	public Hud(Skin skin, CharacterData characterData) {
		bottom();
		left();
		setSize(LudumDare.WIDTH, LudumDare.HEIGHT);

		Table bottom = new Table();
		bottom.setBackground(skin.getDrawable("hud-bg"));
		bottom.pad(10);
		VarsHud varsHud = new VarsHud(skin, characterData);
		bottom.add(varsHud).left();
		bottom.add(new VarLabel(Var.score, characterData, skin, "score").prefix("= ").align(Align.left)).padLeft(10);
		bottom.add(new ItemsHud(skin, characterData)).expand().right().padRight(10).padBottom(10);

		VerticalGroup scores = new VerticalGroup();
		scores.align(Align.left);
		scores.addActor(new VarLabel(Var.topScore, characterData, skin, "top-score").prefix("Top Score: ").align(Align.left));
		add(scores).left().top().pad(10);

		instructions = new InstructionsHud(skin);
		add(instructions).padRight(1).expandY().fillY().right().fillX().width(Value.prefWidth);
		row();
		add(bottom).fillX().expandX().colspan(2);
	}

	@Override
	public void updated(Var var, int value) {
		if (var == Var.visibility) {
			instructions.setPrefWidth(LudumDare.WIDTH - LudumDare.WIDTH / Scene.DIVISION_FACTOR - (value + 1) * Scene.X_OFFSET);
		}
	}

	@Override
	public void rootSet(Node root) {

	}

	@Override
	public void created(Node parent, Node node, int index, int total) {

	}

	@Override
	public void nodeSelected(Node parent, Node node, boolean demon, boolean gotoNode) {

	}

	@Override
	public void message(String message) {
		instructions.setText(message);
	}

	@Override
	public void sound(String sound) {

	}

	@Override
	public void ended() {

	}
}
