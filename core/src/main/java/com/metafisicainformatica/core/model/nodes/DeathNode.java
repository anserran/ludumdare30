package com.metafisicainformatica.core.model.nodes;

import com.metafisicainformatica.core.model.happenings.Incident;

public class DeathNode extends Node<Incident> {
	public DeathNode() {
		super(null);
	}

	@Override
	public Node getNode(int index) {
		return null;
	}

	@Override
	public int childrenCount() {
		return 0;
	}

	@Override
	public void setNextNode(Node nextNode) {

	}

	@Override
	public void append(Node node) {

	}
}
