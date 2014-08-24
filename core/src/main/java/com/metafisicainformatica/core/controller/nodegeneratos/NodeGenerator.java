package com.metafisicainformatica.core.controller.nodegeneratos;

import com.metafisicainformatica.core.model.*;
import com.metafisicainformatica.core.model.nodes.Node;

public interface NodeGenerator {

	public Node generate(CharacterData characterData);
}
