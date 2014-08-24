package com.metafisicainformatica.core.controller.nodegeneratos;

import com.metafisicainformatica.core.model.Happenings;
import com.metafisicainformatica.core.model.happenings.Walk;

public class WalkGenerator extends HappeningNodeGenerator<Walk> {

	public WalkGenerator(Happenings happenings) {
		super(happenings);
	}

	@Override
	public Class happeningsClass() {
		return Walk.class;
	}

	@Override
	public String happeningsFile() {
		return "walks.json";
	}
}
