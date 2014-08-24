package com.metafisicainformatica.core.controller.nodegeneratos;

import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.Happenings;
import com.metafisicainformatica.core.model.nodes.Node;

public class BirthdayGenerator implements NodeGenerator {

	private Happenings happenings;

	public BirthdayGenerator(Happenings happenings) {
		this.happenings = happenings;
	}

	@Override
	public Node generate(CharacterData characterData) {
		return happenings.buildNode(Math.random() > 0.75f ? "birthday-branch" : "birthday");
	}
}
