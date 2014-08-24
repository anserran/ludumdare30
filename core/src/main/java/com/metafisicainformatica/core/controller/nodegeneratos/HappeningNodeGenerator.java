package com.metafisicainformatica.core.controller.nodegeneratos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.metafisicainformatica.core.controller.util.RandomChoice;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.Happenings;
import com.metafisicainformatica.core.model.happenings.Happening;
import com.metafisicainformatica.core.model.nodes.Node;

public abstract class HappeningNodeGenerator<T extends Happening> implements NodeGenerator {

	private RandomChoice randomChoice = new RandomChoice();

	private Happenings happenings;

	private Array<T> happeningsList;

	public abstract Class happeningsClass();

	public abstract String happeningsFile();

	public Node build(T happening) {
		return happenings.buildNode(happening.getId());
	}

	public HappeningNodeGenerator(Happenings happenings) {
		this.happenings = happenings;
		Json json = new Json();
		happeningsList = json.fromJson(Array.class, happeningsClass(), Gdx.files.internal(happeningsFile()));
		// Remove impossible incidents
		for (T happening : happeningsList) {
			if (happening.getRepetitions() == 0 || MathUtils.isEqual(happening.getProbability(), 0.0f, 0.000001f)) {
				happeningsList.removeValue(happening, true);
			}
		}
	}

	public Node generate(CharacterData characterData) {
		randomChoice.clear();
		for (T happening : happeningsList) {
			float probability = getProbability(happening, characterData);
			randomChoice.add(probability, happening);
		}

		System.out.println(randomChoice);
		T incident = (T) randomChoice.random();
		System.out.println("Selected: " + incident);
		if (incident.getRepetitions() > 0) {
			incident.setRepetitions(incident.getRepetitions() - 1);
			if (incident.getRepetitions() <= 0) {
				happeningsList.removeValue(incident, true);
			}
		}

		return build(incident);
	}

	public float getProbability(Happening happening, CharacterData characterData) {
		float probability = happening.getProbability();
		for (Entry<String, Float> entry : happening.getInfluence().entries()) {
			Var var = Var.valueOf(entry.key);
			float proportion = Math.min(1, Math.max(0, characterData.get(var) / var.maxValue()));
			probability += Math.max(0.0f, proportion * entry.value);
		}
		return probability;
	}

}
