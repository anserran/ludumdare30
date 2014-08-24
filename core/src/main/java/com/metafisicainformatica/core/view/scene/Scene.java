package com.metafisicainformatica.core.view.scene;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.metafisicainformatica.core.LudumDare;
import com.metafisicainformatica.core.controller.GraphController.NodeListener;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.happenings.Incident;
import com.metafisicainformatica.core.model.nodes.ChoiceNode;
import com.metafisicainformatica.core.model.nodes.DeathNode;
import com.metafisicainformatica.core.model.nodes.IncidentNode;
import com.metafisicainformatica.core.model.nodes.Node;
import com.metafisicainformatica.core.model.nodes.WalkNode;
import com.metafisicainformatica.core.view.CenteredDrawable;

import java.util.HashMap;
import java.util.Map;

public class Scene extends Group implements NodeListener {

	public static float DIVISION_FACTOR = 3.0f;
	public static float X_OFFSET = 100;
	public static float PLAYER_TIME = 0.25f;

	private Skin skin;

	private Map<Integer, Vector2> spacesOccupied = new HashMap<Integer, Vector2>();

	private Actor player;

	private Group columns = new Group();

	private Group paths = new Group();

	private Group floors = new Group();

	private Group arrows = new Group();

	private Map<Class, Color> colors = new HashMap<Class, Color>();

	private CharacterData characterData;

	private Node selected;

	public Scene(Skin skin, CharacterData characterData) {
		this.skin = skin;
		this.characterData = characterData;
		addActor(columns);
		addActor(paths);
		addActor(floors);
		addActor(arrows);
		addActor(player = createPlayer());

		colors.put(IncidentNode.class, new Color(65 / 255.f, 105 / 255f, 225 / 255f, 1.0f));
		colors.put(WalkNode.class, new Color(50 / 255f, 205 / 255f, 50 / 255f, 1.0f));
		colors.put(ChoiceNode.class, new Color(255 / 255f, 0, 0, 1.0f));
		colors.put(DeathNode.class, Color.BLACK);

	}

	private Actor createPlayer() {
		NodeActor player = new NodeActor();
		Drawable drawable = skin.getDrawable("player");
		player.setSize(drawable.getMinWidth(), drawable.getMinHeight());
		player.setDrawable(skin.getDrawable("player"));
		return player;
	}

	@Override
	public void rootSet(Node root) {
		spacesOccupied.clear();
		columns.clearChildren();
		paths.clearChildren();
		floors.clearChildren();
		arrows.clearChildren();
		player.setPosition(0, 0);
	}

	@Override
	public void created(Node parent, Node node, int index, int total) {
		if (parent != null && paths.findActor(parent.getId() + "->" + node.getId()) != null) {
			return;
		}

		Actor[] column = createNodeActor(parent, node, index, total);
		columns.addActor(column[0]);
		floors.addActor(column[1]);
		if (parent != null) {
			Actor parentActor = columns.findActor(parent.getId());
			Vector2 vector = new Vector2(column[0].getX() - parentActor.getX(), column[0].getY() - parentActor.getY());
			Actor path = createPath(parentActor, vector, column[0].getWidth(), column[0].getHeight());
			path.setName(parent.getId() + "->" + node.getId());
			if (column.length == 3) {
				path.setY(path.getY() + 40.0f);
			}

			paths.addActor(path);
			// Add arrows keys
			Actor arrow = createArrow(vector, parentActor, index, total);
			arrow.setVisible(false);
			arrow.setName(parent.getId());
			arrows.addActor(arrow);


			createAnimation(column[0], column[1], path, column.length != 3);
		} else {
			createAnimation(column[0], column[1], null, column.length != 3);
		}
	}

	private Actor createArrow(Vector2 vector, Actor parentActor, int index, int total) {
		String drawable = "key-right";
		switch (total) {
			case 2:
				switch (index) {
					case 0:
						drawable = "key-up";
						break;
					case 1:
						drawable = "key-down";
						break;
				}
				break;
			case 3:
				switch (index) {
					case 0:
						drawable = "key-up";
						break;
					case 1:
						drawable = "key-right";
						break;
					case 2:
						drawable = "key-down";
						break;
				}
				break;
		}

		CenteredDrawable arrow = new CenteredDrawable(skin.getDrawable(drawable));
		vector.scl(0.5f).add(parentActor.getX(), parentActor.getY());
		arrow.setPosition(vector.x + arrow.getWidth() / 2.0f, vector.y + arrow.getHeight());
		return arrow;
	}

	private void createAnimation(Actor column, Actor floor, Actor path, boolean animateColumn) {
		float columnUpTime = 1f;
		float pathTime = 2f;
		float floorOffset = 10.0f;

		float columnHeight = column.getHeight();
		float floorY = floor.getY();

		if (animateColumn) {
			column.setHeight(0.0f);
			floor.getColor().a = 0.0f;
			floor.setY(floorY + floorOffset);
		}

		Action columnUp = Actions.addAction(Actions.sizeTo(column.getWidth(), columnHeight, columnUpTime, Interpolation.bounceOut), column);
		Action floorDown = Actions.addAction(Actions.moveTo(floor.getX(), floorY, columnUpTime, Interpolation.bounceOut), floor);
		Action floorAppears = Actions.addAction(Actions.alpha(1.0f, columnUpTime, Interpolation.bounceOut), floor);
		Action columnBuilt = Actions.parallel(columnUp, floorDown, floorAppears);

		if (path != null) {
			float pathWidth = path.getWidth();
			path.setWidth(0.0f);
			Action extendPath = Actions.addAction(Actions.sizeTo(pathWidth, path.getHeight(), pathTime, Interpolation.exp10Out), path);
			if (animateColumn) {
				addAction(Actions.sequence(columnBuilt, extendPath));
			} else {
				addAction(Actions.sequence(extendPath));
			}

		} else {
			addAction(columnBuilt);
		}
	}

	@Override
	public void nodeSelected(Node parent, Node node, boolean demon, boolean gotoNode) {
		if (gotoNode) {
			if (parent != null) {
				remove(parent.getId());
			}
			centerAt(node);
			select(node);
			for (Actor actor : arrows.getChildren()) {
				if (node.getId().equals(actor.getName())) {
					actor.setVisible(true);
				}
			}
		}

		if (parent != null) {
			Actor actor = paths.findActor(parent.getId() + "->" + node.getId());
			if (actor != null) {
				actor.setColor(demon ? Color.RED : Color.GREEN);
			}
		}

		if (gotoNode && demon && parent != null) {
			Demon demonActor = ((Group) floors.findActor(parent.getId())).findActor("demon");
			if (demonActor != null) {
				demonActor.goAway();
			}
		}
	}

	private void remove(String id) {
		goAway(floors.findActor(id));
		goAway(columns.findActor(id));
		goAway(arrows.findActor(id));
		for (Actor actor : paths.getChildren()) {
			if (actor.getName() != null && actor.getName().contains(id)) {
				goAway(actor);
			}
		}
	}

	private void goAway(Actor actor) {
		float randomTime = (float) (Math.random() * 5.0f + 10.0f);
		float signum = Math.random() > 0.5f ? -1.0f : 1.0f;
		actor.addAction(Actions.sequence(Actions.delay(0.3f), Actions.parallel(Actions.moveBy(-LudumDare.WIDTH, 0, randomTime), Actions.rotateBy(360 * signum, randomTime)), Actions.removeActor()));
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

	private Actor[] createNodeActor(Node parent, Node node, int index, int total) {
		if (columns.findActor(node.getId()) != null) {
			return new Actor[]{
					columns.findActor(node.getId()), floors.findActor(node.getId()), null
			};
		}

		float ySpace = total == 1 ? 1 : LudumDare.HEIGHT * 0.4f;
		float yDepthOffset = 100;


		float x = 0;
		float y = 0;

		Vector2 minMax = spacesOccupied.get(node.getDepth());
		if (minMax == null) {
			minMax = new Vector2(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
			spacesOccupied.put(node.getDepth(), minMax);
		}

		if (parent != null) {
			Actor parentColumn = columns.findActor(parent.getId());
			x = parentColumn.getX() + X_OFFSET;
			y = parentColumn.getY();

			Vector2 space = new Vector2(y, y + ySpace).sub(ySpace / 2.0f, ySpace / 2.0f);

			if ((minMax.x <= space.x && space.x <= minMax.y) || (minMax.x <= space.y && space.y <= minMax.y)) {
				// Space is occupied
				boolean toDown = minMax.x <= space.y && space.y <= minMax.y;
				if (toDown) {
					y -= space.y - minMax.x - yDepthOffset;
				} else {
					y += minMax.y - space.x + yDepthOffset;
				}
			}

			if (total > 1) {
				float yIndex = (1.0f / (total - 1.0f)) * index;
				y += (ySpace / 2.0f) - yIndex * ySpace;
			}
		}

		if (index == total - 1) {
			minMax.set(Math.min(minMax.x, y), Math.max(minMax.y, y));
		}

		NodeActor column = new NodeActor();
		column.setDrawable(skin.getDrawable("column"));
		column.setPosition(x, y);

		column.setName(node.getId());

		NodeActor floor = new NodeActor();
		floor.setDrawable(skin.getDrawable("column-top"));
		floor.setPosition(x, y);
		floor.setName(node.getId());
		floor.setColor(colors.get(node.getClass()));

		if (node.hasDemon()) {
			floor.addActor(new Demon(skin, characterData));
		}

		if (node instanceof IncidentNode) {
			Actor item = createIncident((IncidentNode) node);
			item.setPosition(floor.getWidth() / 2.0f, floor.getHeight());
			floor.addActor(item);
			item.toFront();
			item.addAction(Actions.forever(Actions.sequence(Actions.moveBy(0, 10.0f, 1.7f), Actions.moveBy(0, -10.0f, 0.92f))));
			item.setName("item");
		}

		return new Actor[]{column, floor};
	}

	private Actor createPath(Actor parentActor, Vector2 path, float width, float height) {
		NodeActor pathActor = new NodeActor();
		Drawable drawable = skin.getDrawable("path");
		pathActor.setDrawable(drawable);
		pathActor.setSize(path.len(), drawable.getMinHeight());
		pathActor.setRotation(path.angle());
		pathActor.setPosition(parentActor.getX() + width / 2.0f, parentActor.getY() + height - pathActor.getHeight());

		return pathActor;
	}

	private void select(Node node) {
		this.selected = node;
		Actor nodeActor = columns.findActor(node.getId());
		player.addAction(Actions.moveTo(nodeActor.getX(), nodeActor.getY() + player.getHeight() / 3.0f, PLAYER_TIME));

		// Remove item
		Actor item = ((Group) floors.findActor(node.getId())).findActor("item");
		if (item != null) {
			item.addAction(Actions.sequence(Actions.delay(PLAYER_TIME / 2.0f), Actions.scaleTo(0.0f, 0.0f, PLAYER_TIME / 2.0f), Actions.removeActor()));
		}
	}

	private void centerAt(Node node) {
		Actor nodeActor = columns.findActor(node.getId());
		addAction(Actions.moveTo(getWidth() / DIVISION_FACTOR - nodeActor.getX(), getHeight() / 2.0f - nodeActor.getY() - 40.0f, 0.5f));
	}

	private Actor createIncident(IncidentNode node) {
		Incident incident = node.getHappening();
		Group group = new Group();
		float y = 0;
		if (incident.getDrawable() != null) {
			Drawable drawable = skin.getDrawable(incident.getDrawable());
			group.addActor(new CenteredDrawable(drawable));
			y += drawable.getMinHeight();
		}

		if (incident.getName() != null) {
			Label label = new Label(incident.getName(), skin, "incident");
			label.setPosition(-label.getPrefWidth() / 2.0f, y - 5);
			group.addActor(label);
		}
		return group;
	}
}
