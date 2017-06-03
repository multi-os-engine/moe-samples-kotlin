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
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector3
import org.moe.libgdxmissilecommand.common.Assets
import org.moe.libgdxmissilecommand.common.MissileCommand
import org.moe.libgdxmissilecommand.common.models.*

class MissileCommandGameScreen(game: MissileCommand) : ScreenAdapter() {
    private val game: MissileCommand
    private val guiCam: OrthographicCamera
    private val fontTime: BitmapFont
    private val fontTitle: BitmapFont
    private var levelBackgroung: Int = 0
    private var lastAttackTime: Long = 0
    private var time: Float = 0f
    private var playing: Boolean = true
    private val pauseButton: Sprite
    private val backButton: Sprite
    private val touchPoint: Vector3
    private var wave: Int = 1
    private var verNaves: Long = 0
    private var score: Long = 0
    private val missiles = arrayListOf<Missile>()
    private val naves = arrayListOf<Nave>()
    internal var cities: Array<City>
    internal var player: Player

    init {
        this.game = game
        fontTime = BitmapFont(Assets.fontFile)
        fontTime.getData().setScale(0.5f)
        fontTitle = BitmapFont(Assets.fontFile)
        fontTitle.getData().setScale(0.7f)
        guiCam = OrthographicCamera(Assets.screenWidth, Assets.screenHeight)
        guiCam.position.set(Assets.screenWidth / 2, Assets.screenHeight / 2, 0f)
        pauseButton = Sprite(Assets.pauseButtonTextureRegion)
        pauseButton.setPosition(Assets.screenWidth - (pauseButton.getWidth() + 2), 5f)
        backButton = Sprite(Assets.backButtonTextureRegion)
        backButton.setPosition(Assets.screenWidth - (backButton.getWidth() + 2), 2f)
        touchPoint = Vector3()
        player = Player()
        player.setPosition(Assets.screenWidth / 2, Assets.screenHeight / 24)
        City.resetCounter()
        cities = Array<City>(6, { City() })
    }

    private fun draw() {
        val gl = Gdx.gl
        gl.glClearColor(1f, 0f, 0f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        guiCam.update()
        game.batcher.setProjectionMatrix(guiCam.combined)
        game.batcher.begin()
        game.batcher.draw(Assets.levelsBackgroundsTextureRegions[levelBackgroung], 0f, 0f, Assets.screenWidth, Assets.screenHeight)
        game.batcher.end()
        game.batcher.enableBlending()
        if (playing)
            checkCollisions()
        game.batcher.begin()
        val dt = Gdx.graphics.getDeltaTime()
        for (city in cities) {
            if (playing)
                city.update(dt)
            city.draw(game.batcher)
        }
        if (playing) {
            time -= dt
            if (time > 20 / wave && (System.currentTimeMillis() - verNaves > 5000) && City.cityCounter > 0) {
                addNaves()
            }
        }
        var printTime = time.toInt()
        if (printTime < 0)
            printTime = 0
        fontTime.draw(game.batcher, "Time: " + printTime.toString() + "s",
                Assets.screenWidth - 150, Assets.screenHeight - 25)
        fontTime.draw(game.batcher, "Score: " + score.toString(),
                2f, Assets.screenHeight - 25f)
        var i = 0;
        while (i < missiles.size) {
            val missile = missiles.get(i)
            if (missile.isAlive) {
                if (playing)
                    missile.update(dt)
                missile.draw(game.batcher)
                i++
            } else {
                missiles.removeAt(i)
            }
        }
        player.draw(game.batcher)
        i = 0
        while (i < naves.size) {
            val nave = naves.get(i)
            if (nave.isAlive) {
                if (playing)
                    nave.update(dt)
                nave.draw(game.batcher)
                i++
            } else {
                naves.removeAt(i)
            }
        }
        if (playing) {
            if (time <= 0) {
                fontTitle.draw(game.batcher, "Wave " + wave.toString() +
                        " Starts in " + (3 + time).toInt().toString() + "s",
                        230f, 240f)
                if (time < -3) {
                    time = 60f
                    wave++
                    levelBackgroung = (wave - 2) % 4
                }
            }
            pauseButton.draw(game.batcher)
        } else {
            if (City.cityCounter > 0) {
                fontTitle.draw(game.batcher, "Touch the screen to resuming the game",
                        100f, 320f + fontTitle.getLineHeight())
                fontTitle.draw(game.batcher, "Or press back button to exit in the main menu",
                        70f, 320f - fontTitle.getLineHeight())
            } else {
                fontTitle.draw(game.batcher, "You Lose!",
                        320f, 320f + fontTitle.getLineHeight())
                fontTitle.draw(game.batcher, "Score: " + score.toString(),
                        320f, 320f - fontTitle.getLineHeight())
            }
            backButton.draw(game.batcher)
        }
        fontTime.draw(game.batcher, "fps: " + Gdx.graphics.getFramesPerSecond(), 0f, 0f)
        game.batcher.end()
    }

    private fun update() {
        if (City.cityCounter <= 0) {
            playing = false
        }
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat(), 0f))
            if (pauseButton.getBoundingRectangle().contains(touchPoint.x, touchPoint.y) && playing) {
                playing = false
                return
            }
            if (backButton.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(MainMenuScreen(game))
                return
            }
            if (!playing) {
                if (City.cityCounter > 0)
                    playing = true
                return
            }
            if (System.currentTimeMillis() - lastAttackTime > 450 && touchPoint.y >= player.getY()) {
                lastAttackTime = System.currentTimeMillis()
                missiles.add(Missile())
                missiles.get(missiles.size - 1).detectTarget(touchPoint.x, touchPoint.y)
                player.setRotation(missiles.get(missiles.size - 1).getRotation())
            }
        }
    }

    private fun addNaves() {
        naves.add(Nave(wave))
        naves.add(Nave(wave))
        naves.add(Nave(wave))
        verNaves = System.currentTimeMillis()
    }

    override
    fun render(delta: Float) {
        update()
        draw()
    }

    private fun checkCollisions() {
        for (i in 0..naves.size - 1) {
            for (city in cities) {
                if (isCollision(naves.get(i), city)) {
                    naves.get(i).kill()
                    city.kill()
                }
            }
            for (j in 0..missiles.size - 1) {
                if (isCollision(naves.get(i), missiles.get(j)) && naves.get(i).kill()) {
                    score++
                    missiles.get(j).kill()
                }
            }
        }
    }

    private fun <T : GameObject> isCollision(obj1: T, obj2: T): Boolean {
        val r1 = obj1.collisionRectangle()
        val r2 = obj2.collisionRectangle()
        return !(r1 == null || r2 == null) && r2.overlaps(r1)
    }

    override fun pause() {
//		Settings.save();
    }
}