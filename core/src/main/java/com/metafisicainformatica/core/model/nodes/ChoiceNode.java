package com.metafisicainformatica.core.model.nodes;

import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.happenings.Choice;

public class ChoiceNode extends Node<Choice> {

	private Node[] choices;

	public ChoiceNode(Choice node, Node... nodes) {
		super(node);
		choices = nodes;
	}

	@Override
	public Node getNode(int index) {
		return choices[index];
	}

	@Override
	public int childrenCount() {
		return choices.length;
	}

	@Override
	public void setNextNode(Node nextNode) {
		if (choices != null) {
			for (Node node : choices) {
				node.setNextNode(nextNode);
			}
		}
	}

	@Override
	public void append(Node node) {
		if (choices.length > 0) {
			for (Node n : choices) {
				n.setNextNode(node);
			}
		}
	}

	public String toString() {
		return "O";
	}

	@Override
	public void setHasDemon(CharacterData characterData) {
		hasDemon = happening.isHasDemon() || (characterData.get(Var.createDemons) > 0 && Math.random() > 0.9f);
	}
}
