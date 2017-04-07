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

import org.moe.libgdxmissilecommand.common.MissileCommand
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector3

class MainMenuScreen(game: MissileCommand) : ScreenAdapter() {
	internal var game: MissileCommand
	internal var guiCam: OrthographicCamera
	internal var startButton: Sprite
	internal var creditsButton: Sprite
	internal var touchPoint: Vector3

	init {
		this.game = game
		guiCam = OrthographicCamera(org.moe.libgdxmissilecommand.common.Assets.screenWidth, org.moe.libgdxmissilecommand.common.Assets.screenHeight)
		guiCam.position.set(org.moe.libgdxmissilecommand.common.Assets.screenWidth / 2, org.moe.libgdxmissilecommand.common.Assets.screenHeight / 2, 0f)
		startButton = Sprite(org.moe.libgdxmissilecommand.common.Assets.startButtonTextureRegion)
		startButton.setPosition(org.moe.libgdxmissilecommand.common.Assets.screenWidth / 2 - startButton.getWidth() / 2, startButton.getHeight() * 2.5f)
		creditsButton = Sprite(org.moe.libgdxmissilecommand.common.Assets.creditsButtonTextureRegion)
		creditsButton.setPosition(org.moe.libgdxmissilecommand.common.Assets.screenWidth / 2 - creditsButton.getWidth() / 2, creditsButton.getHeight())
		touchPoint = Vector3()
	}

	fun update() {
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			Gdx.app.exit()
		}
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat(), 0f))
			if (startButton.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(StartGameScreen(game))
				return
			}
			if (creditsButton.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(CreditsScreen(game))
				return
			}
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
		game.batcher.draw(org.moe.libgdxmissilecommand.common.Assets.backgroundRegionMenu, 0f, 0f, org.moe.libgdxmissilecommand.common.Assets.screenWidth, org.moe.libgdxmissilecommand.common.Assets.screenHeight)
		game.batcher.end()
		game.batcher.enableBlending()
		game.batcher.begin()
		startButton.draw(game.batcher)
		creditsButton.draw(game.batcher)
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