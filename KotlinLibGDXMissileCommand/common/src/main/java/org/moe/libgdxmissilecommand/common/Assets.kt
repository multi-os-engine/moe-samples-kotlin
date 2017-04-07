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
package org.moe.libgdxmissilecommand.common

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

object Assets {
	val screenWidth = 800f
	val screenHeight = 480f
	// Backgrounds
	lateinit var backgroundRegionMenu: TextureRegion
	lateinit var backgroundLoadGame1TextureRegion: TextureRegion
	lateinit var backgroundLoadGame2TextureRegion: TextureRegion
	lateinit var levelsBackgroundsTextureRegions: Array<TextureRegion>
	// Buttons
	lateinit var backButtonTextureRegion: TextureRegion
	lateinit var pauseButtonTextureRegion: TextureRegion
	lateinit var startButtonTextureRegion: TextureRegion
	lateinit var creditsButtonTextureRegion: TextureRegion
	// Player
	lateinit var playerBaseTextureRegion: TextureRegion
	lateinit var playerGunTextureRegion: TextureRegion
	// Missiles
	lateinit var missileTextureRegion: TextureRegion
	lateinit var missileSound: Sound
	// Explosions
	lateinit var explosionAnimation: Animation<TextureRegion>
	lateinit var explosionSound: Sound
	// Cities
	lateinit var citiesDestroyAnimation: Animation<TextureRegion>
	lateinit var citiesTextureRegions: Array<TextureRegion>
	// Naves
	lateinit var navesTextureRegion: TextureRegion
	// Font file
	lateinit var fontFile: FileHandle
	lateinit var alarmSound: Sound
	private fun loadTexture(file: String?): Texture? {
		return Texture(Gdx.files.internal(file))
	}

	fun load() {
		val backgroundMenuTexture = loadTexture("Splash2.png")
		backgroundRegionMenu = TextureRegion(backgroundMenuTexture, 0, 0, 800, 480)
		val backgroundLoadGame1Texture = loadTexture("aviso1.png")
		backgroundLoadGame1TextureRegion = TextureRegion(backgroundLoadGame1Texture, 0, 0, 800, 480)
		val backgroundLoadGame2Texture = loadTexture("aviso2.png")
		backgroundLoadGame2TextureRegion = TextureRegion(backgroundLoadGame2Texture, 0, 0, 800, 480)

		levelsBackgroundsTextureRegions = Array<TextureRegion>(4, { i ->
			TextureRegion(loadTexture("bg" + (i + 1).toString() + ".png"), 0, 0, 800, 480)
		});

		val startButtonTexture = loadTexture("btStart.png")
		startButtonTextureRegion = TextureRegion(startButtonTexture, 0, 0, 120, 43)
		val creditsButtonTexture = loadTexture("btCredits.png")
		creditsButtonTextureRegion = TextureRegion(creditsButtonTexture, 0, 0, 144, 43)
		val backButtonTexture = loadTexture("btBack.png")
		backButtonTextureRegion = TextureRegion(backButtonTexture, 0, 0, 106, 43)
		val pauseButtonTexture = loadTexture("btPause.png")
		pauseButtonTextureRegion = TextureRegion(pauseButtonTexture, 0, 0, 81, 22)
		val playerBaseTexture = loadTexture("canhaopart1.png")
		playerBaseTextureRegion = TextureRegion(playerBaseTexture, 0, 0, 75, 45)
		val playerGunTexture = loadTexture("canhaopart2.png")
		playerGunTextureRegion = TextureRegion(playerGunTexture, 0, 0, 8, 45)
		val missileTexture = loadTexture("missil.png")
		missileTextureRegion = TextureRegion(missileTexture, 0, 0, 5, 9)

		citiesTextureRegions = Array<TextureRegion>(6, { i ->
			TextureRegion(loadTexture("city" + (i + 1).toString() + ".png"), 0, 0, 80, 80)
		})

		val citiesDestroy = loadTexture("cidade.png")
		citiesDestroyAnimation = Animation(0.2f, TextureRegion(citiesDestroy, 2, 53, 51, 59),
				TextureRegion(citiesDestroy, 55, 2, 51, 15))
		fontFile = Gdx.files.internal("arial.fnt")
		alarmSound = Gdx.audio.newSound(Gdx.files.internal("alarm.mp3"))
		missileSound = Gdx.audio.newSound(Gdx.files.internal("missil.mp3"))
		val explosion = loadTexture("explosao1.png")
		explosionAnimation = Animation(0.2f, TextureRegion(explosion, 8, 8, 26, 22),
				TextureRegion(explosion, 35, 8, 40, 38), TextureRegion(explosion, 76, 8, 60, 56),
				TextureRegion(explosion, 137, 8, 74, 76), TextureRegion(explosion, 8, 85, 74, 68),
				TextureRegion(explosion, 83, 85, 72, 68), TextureRegion(explosion, 156, 85, 68, 62),
				TextureRegion(explosion, 8, 154, 62, 60), TextureRegion(explosion, 71, 154, 58, 56))
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("bomb.mp3"))
		val nave = loadTexture("naves.png")
		navesTextureRegion = TextureRegion(nave, 12, 20)
	}

	fun playSound(sound: Sound) {
		sound.play(1f)
	}
}