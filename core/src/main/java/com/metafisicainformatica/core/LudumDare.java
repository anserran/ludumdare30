package com.metafisicainformatica.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.metafisicainformatica.core.controller.GraphController;
import com.metafisicainformatica.core.controller.GraphController.NodeListener;
import com.metafisicainformatica.core.model.CharacterData;
import com.metafisicainformatica.core.model.CharacterData.Var;
import com.metafisicainformatica.core.model.Happenings;
import com.metafisicainformatica.core.model.nodes.Node;
import com.metafisicainformatica.core.view.CharacterDebug;
import com.metafisicainformatica.core.view.NodeRenderer;
import com.metafisicainformatica.core.view.hud.Hud;
import com.metafisicainformatica.core.view.scene.Scene;

public class LudumDare implements ApplicationListener {

	public static int WIDTH = 1066, HEIGHT = 600;

	private Stage stage;

	private CharacterDebug characterDebug;

	private GraphController graphController;
	private CharacterData characterData;
	private NodeRenderer nodeRenderer;
	private Happenings happenings;
	private AssetManager assetManager;
	private Scene scene;
	private boolean debug = true;


	@Override
	public void create() {
		assetManager = new AssetManager();
		loadAssets();
		Skin skin = assetManager.get("skin.json", Skin.class);

		stage = new Stage(new FitViewport(WIDTH, HEIGHT));
		characterData = new CharacterData();
		happenings = new Happenings(characterData);
		graphController = new GraphController(characterData, happenings);
		nodeRenderer = new NodeRenderer(skin);
		graphController.addListener(nodeRenderer);
		nodeRenderer.setSize(WIDTH, HEIGHT);

		scene = new Scene(skin, characterData);
		scene.setSize(WIDTH, HEIGHT);
		graphController.addListener(scene);
		stage.addActor(scene);

		//stage.addActor(nodeRenderer);
		characterData.addListener(graphController);

		characterDebug = new CharacterDebug(skin);
		characterData.addListener(characterDebug);

		characterDebug.setY(HEIGHT / 3.0f);

		//stage.addActor(characterDebug);

		Hud hud = new Hud(skin, characterData);
		stage.addActor(hud);
		characterData.addListener(hud);
		graphController.addListener(hud);

		Gdx.input.setInputProcessor(stage);

		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
					case Keys.UP:
						graphController.nextNode(0);
						break;
					case Keys.RIGHT:
						graphController.nextNode(1);
						break;
					case Keys.DOWN:
						graphController.nextNode(2);
						break;
					case Keys.R:
						graphController.init();
						break;
					case Keys.A:
						if (debug || characterData.get(Var.highVisibility) <= 0 && characterData.get(Var.highVisibilityItems) > 0) {
							characterData.inc(Var.highVisibilityItems, -1);
							characterData.set(Var.visibility, 3);
							characterData.set(Var.highVisibility, 10);
						}
						break;
					case Keys.S:
						if (characterData.get(Var.seeDemons) <= 0 && characterData.get(Var.seeDemonsItems) > 0) {
							characterData.inc(Var.seeDemonsItems, -1);
							characterData.set(Var.seeDemons, 10);
						}
						break;
					case Keys.V:
						characterData.inc(Var.visibility, 1);
						break;
					case Keys.K:
						if (debug) {
							for (int i = 0; i < 20; i++) {
								graphController.nextNode(0);
							}
						}
						break;
				}
				return true;
			}
		});

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
			public void sound(String sound) {
				assetManager.get(sound, Sound.class).play();
			}

			@Override
			public void ended() {
				graphController.init();
			}
		});

		graphController.init();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glClearColor(0.88f, 0.88f, 0.88f, 1.0f);
		stage.getViewport().update(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		stage.dispose();
	}

	private void loadAssets() {
		String[] sounds = new String[]{
				"birthday", "health", "money", "love", "demon"
		};

		for (String sound : sounds) {
			assetManager.load(sound + ".mp3", Sound.class);
		}

		assetManager.load("skin.json", Skin.class);
		assetManager.finishLoading();
	}
}
