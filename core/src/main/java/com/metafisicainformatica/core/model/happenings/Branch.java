package com.metafisicainformatica.core.model.happenings;

import com.badlogic.gdx.utils.Array;

public class Branch extends Happening {

	private Array<String> nodes = new Array<String>();

	public Array<String> getNodes() {
		return nodes;
	}

	public void setNodes(Array<String> nodes) {
		this.nodes = nodes;
	}
}
