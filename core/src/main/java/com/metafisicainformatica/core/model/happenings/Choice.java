package com.metafisicainformatica.core.model.happenings;

import com.badlogic.gdx.utils.Array;

public class Choice extends Happening {

	private Array<String> choices = new Array<String>();

	private Array<String> nextIds = new Array<String>();

	private Array<Integer> nextIndexes =new Array<Integer>();

	public Array<String> getChoices() {
		return choices;
	}

	public void setChoices(Array<String> choices) {
		this.choices = choices;
	}

	public Array<String> getNextIds() {
		return nextIds;
	}

	public void setNextIds(Array<String> nextIds) {
		this.nextIds = nextIds;
	}

	public Array<Integer> getNextIndexes() {
		return nextIndexes;
	}

	public void setNextIndexes(Array<Integer> nextIndexes) {
		this.nextIndexes = nextIndexes;
	}
}
