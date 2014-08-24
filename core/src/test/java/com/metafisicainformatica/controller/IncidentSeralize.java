package com.metafisicainformatica.controller;

import com.badlogic.gdx.utils.Json;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.happenings.Incident;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IncidentSeralize {

	@Test
	public void testSerialize() {
		Incident i = new Incident();
		i.setId("incident_label");
		i.getChanges().put(Var.age.toString(), 1);
		i.getInfluence().put(Var.age.toString(), 0.01f);
		i.setProbability(0.1234f);
		i.setProbability(-1);

		Json json = new Json();

		Incident a = json.fromJson(Incident.class, json.toJson(i));
		System.out.println(json.toJson(a));

		assertEquals(a.getChanges().get(Var.age.toString()), i.getChanges().get(Var.age.toString()));
	}
}
