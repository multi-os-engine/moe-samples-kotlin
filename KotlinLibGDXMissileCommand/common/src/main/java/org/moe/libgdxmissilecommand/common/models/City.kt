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
package org.moe.libgdxmissilecommand.common.models

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.moe.libgdxmissilecommand.common.Assets
import java.security.SecureRandom

class City : GameObject(Assets.citiesTextureRegions[SecureRandom().nextInt(6)]) {
	init {
		if (cityCounter < 3) {
			setPosition(50 + ((Assets.screenWidth - 200) / 6) * cityCounter, Assets.screenHeight / 24)
		} else {
			setPosition(50 + ((Assets.screenWidth - 200) / 6) * cityCounter + 120, Assets.screenHeight / 24)
		}
		cityCounter++
	}

	override fun update(deltaTime: Float) {
		if (explosionTime >= 0)
			explosionTime += deltaTime
	}

	override fun draw(batch: Batch) {
		if (explosionTime.compareTo(-1) != 0) {
			if (explosionTime < 0) {
				explosionTime = 0f
				Assets.playSound(Assets.explosionSound)
			}
			val keyFrame = Assets.citiesDestroyAnimation.getKeyFrame(explosionTime, false)
			if (Assets.citiesDestroyAnimation.getKeyFrameIndex(explosionTime) == 1) {
				batch.draw(keyFrame, getX(), getY(), getWidth(), 15f)
				if (isAlive)
					cityCounter--
				isAlive = false
			} else {
				batch.draw(keyFrame, getX(), getY(), getWidth(), getHeight())
			}
		} else {
			super.draw(batch)
		}
	}

	companion object {
		var cityCounter: Long = 0
			internal set

		fun resetCounter() {
			cityCounter = 0
		}
	}
}