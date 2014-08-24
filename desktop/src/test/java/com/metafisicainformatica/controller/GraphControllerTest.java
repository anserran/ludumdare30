package com.metafisicainformatica.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.metafisicainformatica.core.controller.GraphController;
import com.metafisicainformatica.core.controller.GraphController.NodeListener;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.Happenings;
import com.metafisicainformatica.core.model.nodes.Node;

import java.util.Random;

public class GraphControllerTest {

	private int counter = 0;

	private boolean done = false;

	public void testGraph() {
		Gdx.files = new LwjglFiles();
		CharacterData characterData = new CharacterData();
		Happenings happenings = new Happenings(characterData);
		final GraphController graphController = new GraphController(characterData, happenings);
		graphController.addListener(new NodeListener() {
			@Override
			public void rootSet(Node root) {

			}

			@Override
			public void created(Node parent, Node node, int index, int total) {

			}

			@Override
			public void nodeSelected(Node parent, Node node, boolean demon, boolean gotoNode) {

			}

			@Override
			public void message(String message) {

			}

			@Override
			public void ended() {
				counter++;
				if (counter < 100) {
					graphController.init();
				} else {
					done = true;
				}
			}
		});
		graphController.init();
		Random random = new Random(System.currentTimeMillis());
		while (!done) {
			try {
				graphController.nextNode(random.nextInt(graphController.getCurrentNode().childrenCount()));
			} catch (Exception e) {

			}
		}
	}
}
