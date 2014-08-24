package com.metafisicainformatica.core.controller;

import com.badlogic.gdx.utils.Array;
import com.metafisicainformatica.core.controller.nodegeneratos.BirthdayGenerator;
import com.metafisicainformatica.core.controller.nodegeneratos.ChoicesGenerator;
import com.metafisicainformatica.core.controller.nodegeneratos.InicidentGenerator;
import com.metafisicainformatica.core.controller.nodegeneratos.NodeGenerator;
import com.metafisicainformatica.core.controller.nodegeneratos.WalkGenerator;
import com.metafisicainformatica.core.controller.util.RandomChoice;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.CharacterData.VarListener;
import com.metafisicainformatica.core.model.Happenings;
import com.metafisicainformatica.core.model.happenings.Incident;
import com.metafisicainformatica.core.model.nodes.DeathNode;
import com.metafisicainformatica.core.model.nodes.IncidentNode;
import com.metafisicainformatica.core.model.nodes.Node;
import com.metafisicainformatica.core.model.nodes.SimpleNode;

import java.util.Random;

public class GraphController implements VarListener {

	public static int NODES_FOR_BIRTHDAY = 10;

	public static int MAX_AGE = 90;

	private Random random = new Random(System.currentTimeMillis());

	private CharacterData characterData;

	private Node currentNode;

	private Array<NodeListener> nodeListeners = new Array<NodeListener>();

	private RandomChoice randomGenerator = new RandomChoice();

	private BirthdayGenerator birthdayGenerator;

	private Happenings happenings;

	public GraphController(CharacterData characterData, Happenings happenings) {
		this.characterData = characterData;
		this.happenings = happenings;

		this.birthdayGenerator = new BirthdayGenerator(happenings);
		randomGenerator.add(0.4f, new ChoicesGenerator(happenings));
		randomGenerator.add(0.1f, new WalkGenerator(happenings));
		randomGenerator.add(0.6f, new InicidentGenerator(happenings));
	}

	public void init() {
		Node.idGenerator = 0;
		characterData.reset();
		setRoot(happenings.buildNode("root-branch"));
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void addListener(NodeListener nodeListener) {
		nodeListeners.add(nodeListener);
	}

	public void setRoot(Node root) {
		this.currentNode = root;
		notifyRoot(root);
		notifyCreated(null, root, 0, 1);
		notifySelected(null, root, false, true);
	}

	public void nextNode(int index) {
		if (currentNode.childrenCount() == 0) {
			notifyGameEnded();
		} else {
			characterData.walk();
			index = Math.min(index, currentNode.childrenCount() - 1);
			Node playerNode = currentNode.getNode(index);
			int newIndex = calculateIndex(index);
			Node node = currentNode.getNode(newIndex);
			generateNextNodes(node, characterData.get(Var.visibility));
			if (playerNode != node) {
				notifySelected(currentNode, playerNode, false, false);
				notifySelected(currentNode, node, true, true);
			} else {
				notifySelected(currentNode, node, false, true);
			}
			selected(node);
			this.currentNode = node;
		}
	}

	private int calculateIndex(int index) {
		if (currentNode.childrenCount() > 1 && currentNode.hasDemon()) {
			int oldIndex = index;
			while (index == oldIndex) {
				index = random.nextInt(currentNode.childrenCount());
			}
			notifySound("demon.mp3");
		}
		return index;
	}

	private void generateNextNodes(Node node, int visibility) {
		if (visibility == 0) {
			return;
		}
		if (node instanceof SimpleNode) {
			addNextNode((SimpleNode) node);
		}
		for (int i = 0; i < node.childrenCount(); i++) {
			generateNextNodes(node.getNode(i), visibility - 1);
		}
	}

	private void addNextNode(SimpleNode node) {
		if (node.getNextNode() != null) {
			return;
		}


		Node nextNode;
		if (characterData.get(Var.age) >= MAX_AGE || characterData.get(Var.health) <= 0) {
			nextNode = new DeathNode();
		} else if (characterData.get(Var.nodes) >= characterData.get(Var.age) * NODES_FOR_BIRTHDAY) {
			nextNode = birthdayGenerator.generate(characterData);
		} else {
			NodeGenerator nodeGenerator = (NodeGenerator) randomGenerator.random();
			nextNode = nodeGenerator.generate(characterData);
		}

		node.setNextNode(nextNode);
	}

	private void notifyVisible(Node parent, Node child, int index, int total, int visibility) {
		child.setDepth(parent == null ? 0 : parent.getDepth() + 1);
		if (visibility == 0) {
			return;
		}
		notifyCreated(parent, child, index, total);
		for (int i = 0; i < child.childrenCount(); i++) {
			notifyVisible(child, child.getNode(i), i, child.childrenCount(), visibility - 1);
		}
	}

	private void notifyCreated(Node parent, Node child, int index, int total) {
		for (NodeListener listener : nodeListeners) {
			listener.created(parent, child, index, total);
		}
	}

	private void selected(Node selected) {
		processNode(selected);
	}

	private void notifySelected(Node parent, Node selected, boolean demon, boolean gotoNode) {
		if (gotoNode) {
			updateVisibleNodes(selected);
		}
		for (NodeListener listener : nodeListeners) {
			listener.nodeSelected(parent, selected, demon, gotoNode);
		}
	}

	private void updateVisibleNodes(Node selected) {
		for (int i = 0; i < selected.childrenCount(); i++) {
			notifyVisible(selected, selected.getNode(i), i, selected.childrenCount(), characterData.get(Var.visibility));
		}
	}

	private void notifyGameEnded() {
		for (NodeListener listener : nodeListeners) {
			listener.ended();
		}
	}

	private void notifyRoot(Node root) {
		for (NodeListener listener : nodeListeners) {
			listener.rootSet(root);
		}
	}

	private void processNode(Node selected) {
		if (selected instanceof IncidentNode) {
			Incident incident = ((IncidentNode) selected).getHappening();
			characterData.processIncident(incident);
		}
		if (selected != null && selected.getHappening() != null) {
			notifyMessage(selected.getHappening().getMessage());
			String sound = selected.getHappening().getSound();
			if ( sound != null ){
				notifySound(sound);
			}
		}
	}


	private void notifyMessage(String message) {
		for (NodeListener listener : nodeListeners) {
			listener.message(message);
		}
	}

	private void notifySound(String sound) {
		for (NodeListener listener : nodeListeners) {
			listener.sound(sound);
		}
	}

	@Override
	public void updated(Var var, int value) {
		if (var == Var.visibility && currentNode != null) {
			generateNextNodes(currentNode, value);
			updateVisibleNodes(currentNode);
		}
	}

	public interface NodeListener {

		void rootSet(Node root);

		void created(Node parent, Node node, int index, int total);

		void nodeSelected(Node parent, Node node, boolean demon, boolean gotoNode);

		void message(String message);

		void sound(String sound);

		void ended();
	}


}
