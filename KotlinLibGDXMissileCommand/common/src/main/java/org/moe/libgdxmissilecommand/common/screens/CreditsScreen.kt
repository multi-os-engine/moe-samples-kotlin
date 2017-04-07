// Copyright (c) 2015, Intel Corporation
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
// 3. Neither the name of the copyright holder nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package org.moe.libgdxmissilecommand.common.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector3
import org.moe.libgdxmissilecommand.common.MissileCommand
import org.moe.libgdxmissilecommand.common.Assets

class CreditsScreen(game: MissileCommand) : ScreenAdapter() {
	internal var game: MissileCommand
	internal var guiCam: OrthographicCamera
	internal var backButton: Sprite
	internal var touchPoint: Vector3
	internal var font: BitmapFont

	init {
		this.game = game
		font = BitmapFont(Assets.fontFile)
		font.getData().setScale(0.7f)
		guiCam = OrthographicCamera(Assets.screenWidth, Assets.screenHeight)
		guiCam.position.set(Assets.screenWidth / 2, Assets.screenHeight / 2, 0f)
		backButton = Sprite(Assets.backButtonTextureRegion)
		backButton.setPosition(Assets.screenWidth / 2 - backButton.getWidth() / 2, backButton.getHeight())
		touchPoint = Vector3()
	}

	fun update() {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat(), 0f))
			if (backButton.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(MainMenuScreen(game))
				return
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			return
		}
	}

	fun draw() {
		val gl = Gdx.gl
		gl.glClearColor(1f, 0f, 0f, 1f)
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		guiCam.update()
		game.batcher.setProjectionMatrix(guiCam.combined)
		game.batcher.disableBlending()
		game.batcher.begin()
		game.batcher.draw(Assets.backgroundRegionMenu, 0f, 0f, Assets.screenWidth, Assets.screenHeight)
		game.batcher.end()
		game.batcher.enableBlending()
		game.batcher.begin()
		backButton.draw(game.batcher)
		font.draw(game.batcher, "Programmer: Matheus Palheta\n\nGame Design: Jucimar Jr", 200f, 320f)
		game.batcher.end()
	}

	override fun render(delta: Float) {
		update()
		draw()
	}

	override fun pause() {
//		Settings.save();
	}
}