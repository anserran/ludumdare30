package com.metafisicainformatica.core.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.metafisicainformatica.core.model.happenings.Branch;
import com.metafisicainformatica.core.model.happenings.Choice;
import com.metafisicainformatica.core.model.happenings.Happening;
import com.metafisicainformatica.core.model.happenings.Incident;
import com.metafisicainformatica.core.model.happenings.Walk;
import com.metafisicainformatica.core.model.nodes.ChoiceNode;
import com.metafisicainformatica.core.model.nodes.IncidentNode;
import com.metafisicainformatica.core.model.nodes.Node;
import com.metafisicainformatica.core.model.nodes.WalkNode;

import java.util.HashMap;
import java.util.Map;

public class Happenings {

	private Map<String, Happening> happenings = new HashMap<String, Happening>();

	private CharacterData characterData;

	@SuppressWarnings("unchecked")
	public Happenings(CharacterData characterData) {
		this.characterData = characterData;
		Json json = new Json();
		add(json.fromJson(Array.class, Walk.class, Gdx.files.internal("walks.json")));
		add(json.fromJson(Array.class, Incident.class, Gdx.files.internal("incidents.json")));
		add(json.fromJson(Array.class, Choice.class, Gdx.files.internal("choices.json")));
		add(json.fromJson(Array.class, Branch.class, Gdx.files.internal("branches.json")));
	}

	private void add(Array<Happening> list) {
		for (Happening h : list) {
			happenings.put(h.getId(), h);
		}
	}

	public Happening get(String id) {
		return happenings.get(id);
	}

	public Node buildNode(String id) {
		Node newNode = null;
		Happening happening = happenings.get(id);
		if (happening instanceof Walk) {
			newNode = new WalkNode((Walk) happening);
		} else if (happening instanceof Incident) {
			newNode = new IncidentNode((Incident) happening);
		} else if (happening instanceof Choice) {
			Choice choice = (Choice) happening;
			Node[] nodes = new Node[choice.getChoices().size];
			for (int i = 0; i < choice.getChoices().size; i++) {
				nodes[i] = buildNode(choice.getChoices().get(i));
				if (nodes[i] == null) {
					System.out.println(choice.getChoices().get(i) + " not found in " + id);
				}
			}

			if (choice.getNextIds().size > 0) {
				Node[] nextNodes = new Node[choice.getNextIds().size];
				for (int i = 0; i < choice.getNextIds().size; i++) {
					nextNodes[i] = buildNode(choice.getNextIds().get(i));
					if (nextNodes[i] == null) {
						System.out.println(choice.getNextIds().get(i) + " not built in " + id);
					}
				}

				for (int i = 0; i < choice.getNextIndexes().size; i++) {
					nodes[i].setNextNode(nextNodes[choice.getNextIndexes().get(i)]);
				}
			}
			newNode = new ChoiceNode(choice, nodes);
		} else if (happening instanceof Branch) {
			Branch branch = (Branch) happening;
			Node[] nodes = new Node[branch.getNodes().size];
			int i = 0;
			for (String nodeId : branch.getNodes()) {
				Node node = buildNode(nodeId);
				if (i > 0) {
					nodes[i - 1].append(node);
				}
				nodes[i++] = node;
			}
			newNode = nodes[0];
		}

		if (newNode != null) {
			newNode.setHasDemon(characterData);
		}
		return newNode;
	}
}
