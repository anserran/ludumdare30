package com.metafisicainformatica.core.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.metafisicainformatica.core.controller.GraphController.NodeListener;
import com.metafisicainformatica.core.model.nodes.ChoiceNode;
import com.metafisicainformatica.core.model.nodes.IncidentNode;
import com.metafisicainformatica.core.model.nodes.Node;
import com.metafisicainformatica.core.model.nodes.WalkNode;
import com.metafisicainformatica.core.view.scene.NodeActor;

public class NodeRenderer extends Group implements NodeListener {

	private BitmapFont font = new BitmapFont();

	private NodeActor player;

	private Skin skin;

	public NodeRenderer(Skin skin) {
		this.skin = skin;
		player = new NodeActor();
		player.setOrigin(40, 10);
		player.setSize(80, 80);
		player.setDrawable(skin.getDrawable("player"));
	}

	@Override
	public void rootSet(Node root) {
		clearChildren();
		player.setPosition(0, 0);
		addActor(player);
	}

	@Override
	public void created(Node parent, Node node, int index, int total) {
		Actor nodeActor = findActor(node.getId());
		Actor parentActor = parent != null ? findActor(parent.getId()) : null;
		if (nodeActor == null) {
			nodeActor = createNodeActor(node);
			if (parent == null) {
				nodeActor.setPosition(0, 0);
			} else {
				nodeActor.setPosition(parentActor.getX() + 150, parentActor.getY() - index * 200);
			}

			addActor(nodeActor);
			nodeActor.toBack();
		}

		if (parent != null) {
			NodeActor path = findActor(parent.getId() + "->" + node.getId());
			if (path == null) {
				path = new NodeActor();
				path.setColor(Color.BLACK);
				path.setDrawable(skin.getDrawable("square"));
				Vector2 p1 = new Vector2(parentActor.getX(), parentActor.getY()).sub(nodeActor.getX(), nodeActor.getY());

				path.setSize(p1.len(), 10);
				path.setRotation(p1.angle());

				path.setPosition(nodeActor.getX() + 5, nodeActor.getY());

				addActor(path);
				path.toBack();
				path.setName(parent.getId() + "->" + node.getId());
			}
		}
	}

	@Override
	public void nodeSelected(Node parent, Node node, boolean demon, boolean gotoNode) {
		centerAt(node);
		select(node);
		if (parent != null) {
			deselect(parent);
		}
	}

	@Override
	public void message(String message) {

	}

	@Override
	public void sound(String sound) {

	}

	@Override
	public void ended() {

	}

	private void deselect(Node node) {
		Actor nodeActor = findActor(node.getId());
	}

	private void select(Node node) {
		Actor nodeActor = findActor(node.getId());
		player.addAction(Actions.moveTo(nodeActor.getX(), nodeActor.getY(), 0.5f));
	}

	private void centerAt(Node node) {
		Actor nodeActor = findActor(node.getId());
		addAction(Actions.moveTo(getWidth() / 6.0f - nodeActor.getX(), getHeight() / 2.0f - nodeActor.getY(), 0.5f));
	}

	private Actor createNodeActor(Node node) {
		/*Label label = new Label(node.toString() + (node.hasDemon() ? "*" : ""),
				new LabelStyle(font, Color.BLACK));
		label.setAlignment(Align.center, Align.center);
		label.setSize(label.getPrefWidth(), label.getPrefHeight());
		label.setName(node.getId());
		return label;*/
		NodeActor nodeActor = new NodeActor();
		nodeActor.setSize(40, 40);
		nodeActor.setOrigin(nodeActor.getWidth() / 2.f, nodeActor.getHeight() / 2.0f);
		if (node instanceof WalkNode) {
			nodeActor.setDrawable(skin.getDrawable("triangle"));
			nodeActor.setColor(Color.GREEN);
		} else if (node instanceof IncidentNode) {
			nodeActor.setDrawable(skin.getDrawable("square"));
			nodeActor.setColor(Color.BLUE);
		} else if (node instanceof ChoiceNode) {
			nodeActor.setDrawable(skin.getDrawable("circle"));
			nodeActor.setColor(Color.RED);
		}
		nodeActor.setName(node.getId());
		return nodeActor;
	}
}
