package com.metafisicainformatica.core.model.happenings;

import com.badlogic.gdx.utils.ObjectMap;

public class Incident extends Happening {

	private ObjectMap<String, Integer> changes = new ObjectMap<String, Integer>();


	public ObjectMap<String, Integer> getChanges() {
		return changes;
	}

	public void setChanges(ObjectMap<String, Integer> changes) {
		this.changes = changes;
	}
}
