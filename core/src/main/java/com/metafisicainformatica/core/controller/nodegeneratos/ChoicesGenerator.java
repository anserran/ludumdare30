package com.metafisicainformatica.core.controller.nodegeneratos;

import com.metafisicainformatica.core.model.Happenings;
import com.metafisicainformatica.core.model.happenings.Choice;

public class ChoicesGenerator extends HappeningNodeGenerator<Choice> {

	public ChoicesGenerator(Happenings happenings) {
		super(happenings);
	}

	@Override
	public Class happeningsClass() {
		return Choice.class;
	}

	@Override
	public String happeningsFile() {
		return "choices.json";
	}
}
