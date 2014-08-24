package com.metafisicainformatica.core.view.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class NodeActor extends Group {

	private Drawable drawable;

	public NodeActor() {
		setScale(0.0f);
		addAction(Actions.scaleTo(1.0f, 1.0f, 0.2f));
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
		setSize(drawable.getMinWidth(), drawable.getMinHeight());
	}

	@Override
	protected void drawChildren(Batch batch, float parentAlpha) {
		batch.setColor(getColor());
		drawable.draw(batch, 0, 0, getWidth(), getHeight());
		super.drawChildren(batch, parentAlpha);
	}
}
