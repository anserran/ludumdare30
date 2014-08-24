package com.metafisicainformatica.core.model.happenings;

import com.badlogic.gdx.utils.ObjectMap;

public class Happening {

	private String message;

	private String drawable;

	private String name;

	private String id;

	private boolean hasDemon;

	private int repetitions = 0;

	private float probability = 0.0f;

	private ObjectMap<String, Float> influence = new ObjectMap<String, Float>();

	public String getId() {
		return id;
	}

	public String toString() {
		return id;
	}

	public boolean isHasDemon() {
		return hasDemon;
	}

	public void setHasDemon(boolean hasDemon) {
		this.hasDemon = hasDemon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDrawable() {
		return drawable;
	}

	public void setDrawable(String drawable) {
		this.drawable = drawable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public float getProbability() {
		return probability;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}

	public ObjectMap<String, Float> getInfluence() {
		return influence;
	}

	public void setInfluence(ObjectMap<String, Float> influence) {
		this.influence = influence;
	}
}
