package com.metafisicainformatica.core.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.metafisicainformatica.core.model.happenings.Incident;

import java.util.HashMap;
import java.util.Map;

public class CharacterData {

	private Array<VarListener> listeners = new Array<VarListener>();


	public enum Var {
		topScore(0),
		score(0),
		age(200),
		money(100),
		health(100),
		love(100),
		nodes(0),
		visibility(6),
		seeDemons(15),
		seeDemonsItems(5),
		highVisibility(6),
		highVisibilityItems(6),
		createDemons(1);

		private int maxValue;

		private Var(int maxValue) {
			this.maxValue = maxValue;
		}

		public int maxValue() {
			return maxValue;
		}
	}

	private Map<Var, Integer> variables = new HashMap<Var, Integer>();

	public CharacterData() {
		reset();
	}

	public void addListener(VarListener varListener) {
		listeners.add(varListener);
	}

	public void removeListener(VarListener varListener) {
		listeners.removeValue(varListener, true);
	}

	public void inc(Var var, int amount) {
		variables.put(var, get(var) + amount);
		updateScore();
		notify(var, get(var));
	}

	public void set(Var var, int value) {
		variables.put(var, value);
		updateScore();
		notify(var, value);
	}

	private void notify(Var var, int value) {
		for (VarListener listener : listeners) {
			listener.updated(var, value);
		}
	}

	private void updateScore() {
		variables.put(Var.score, get(Var.age) + get(Var.money) + get(Var.love) + get(Var.health));
		notify(Var.score, get(Var.score));
	}

	public int get(Var var) {
		return variables.containsKey(var) ? variables.get(var) : 0;
	}

	public void walk() {
		inc(Var.nodes, 1);
		if (get(Var.seeDemons) > 0) {
			inc(Var.seeDemons, -1);
		}
		if (get(Var.highVisibility) > 0) {
			inc(Var.highVisibility, -1);
			if (get(Var.highVisibility) == 0) {
				set(Var.visibility, 1);
			}
		}
	}

	public void processIncident(Incident incident) {
		for (Entry<String, Integer> entry : incident.getChanges().entries()) {
			inc(Var.valueOf(entry.key), entry.value);
		}
	}

	public void reset() {
		if (!variables.containsKey(Var.topScore)) {
			set(Var.topScore, 0);
		} else {
			set(Var.topScore, get(Var.score));
		}

		set(Var.score, 0);
		set(Var.visibility, 1);
		set(Var.age, 0);
		set(Var.health, 100);
		set(Var.love, 10);
		set(Var.nodes, 0);
		set(Var.money, 0);
		set(Var.seeDemonsItems, 0);
		set(Var.seeDemons, 0);
		set(Var.highVisibilityItems, 0);
		set(Var.highVisibility, 0);
		set(Var.createDemons, 0);
	}

	public interface VarListener {

		void updated(Var var, int value);

	}


}
