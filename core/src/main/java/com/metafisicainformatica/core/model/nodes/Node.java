package com.metafisicainformatica.core.model.nodes;

import com.metafisicainformatica.core.model.*;
import com.metafisicainformatica.core.model.happenings.Happening;

public abstract class Node<T extends Happening> {

	public static long idGenerator = 0;

	private String id;

	private int depth;

	protected boolean hasDemon;

	protected T happening;

	public Node(T happening) {
		this.id = idGenerator++ + "";
		this.happening = happening;
	}

	public T getHappening() {
		return happening;
	}

	public String getId() {
		return id;
	}

	public abstract Node getNode(int index);

	public abstract int childrenCount();

	public abstract void setNextNode(Node nextNode);

	public abstract void append(Node node);

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setHasDemon(CharacterData characterData) {
		hasDemon = happening.isHasDemon();
	}

	public boolean hasDemon() {
		return hasDemon;
	}
}
