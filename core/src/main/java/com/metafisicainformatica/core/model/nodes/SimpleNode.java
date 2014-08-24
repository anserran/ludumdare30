package com.metafisicainformatica.core.model.nodes;

import com.metafisicainformatica.core.model.happenings.Happening;

public class SimpleNode<T extends Happening> extends Node<T> {

	private Node nextNode;

	public SimpleNode(T happening) {
		super(happening);
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}

	@Override
	public void append(Node node) {
		if ( nextNode != null ){
			nextNode.append(node);
		} else {
			nextNode = node;
		}
	}

	public Node getNextNode() {
		return nextNode;
	}

	@Override
	public Node getNode(int index) {
		return nextNode;
	}

	@Override
	public int childrenCount() {
		return nextNode == null ? 0 : 1;
	}


}
