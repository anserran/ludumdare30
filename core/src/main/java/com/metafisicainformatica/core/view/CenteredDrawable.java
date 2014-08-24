package com.metafisicainformatica.core.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CenteredDrawable extends Group {

	private Drawable drawable;

	public CenteredDrawable(Drawable drawable) {
		this.drawable = drawable;
		setSize(drawable.getMinWidth(), drawable.getMinHeight());
	}

	@Override
	protected void drawChildren(Batch batch, float parentAlpha) {
		super.drawChildren(batch, parentAlpha);
		batch.setColor(Color.WHITE);
		drawable.draw(batch, -drawable.getMinWidth() / 2.0f, -drawable.getMinHeight() / 2.0f, drawable.getMinWidth(), drawable.getMinHeight());
	}
}
