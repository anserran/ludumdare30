package com.metafisicainformatica.core.controller.nodegeneratos;

import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.Happenings;
import com.metafisicainformatica.core.model.happenings.Incident;

public class InicidentGenerator extends HappeningNodeGenerator<Incident> {

	public InicidentGenerator(Happenings happenings) {
		super(happenings);
	}

	@Override
	public Class happeningsClass() {
		return Incident.class;
	}

	@Override
	public String happeningsFile() {
		return "incidents.json";
	}


}
