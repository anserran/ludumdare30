package com.metafisicainformatica.core.controller.util;

import com.badlogic.gdx.utils.Array;

public class RandomChoice {

	private Array<Choice> choices = new Array<Choice>();

	private float totalProbability;

	public void clear() {
		choices.clear();
		totalProbability = 0.0f;
	}

	public void add(float probability, Object choice) {
		choices.add(new Choice(probability, choice));
		totalProbability += probability;
	}

	public Object random() {
		float prob = (float) (Math.random() * totalProbability);
		float accProb = 0.0f;
		for (Choice choice : choices) {
			accProb += choice.probability;
			if (accProb > prob) {
				return choice.choice;
			}

		}
		return null;
	}


	private static class Choice {

		private float probability;

		private Object choice;

		private Choice(float probability, Object choice) {
			this.probability = probability;
			this.choice = choice;
		}
	}

	public String toString() {
		String result = "";
		for (Choice choice : choices) {
			result += choice.choice + ": " + (Math.round((choice.probability / totalProbability) * 1000000.0f) / 10000.0f) + "%\n";
		}
		return result;
	}

}
