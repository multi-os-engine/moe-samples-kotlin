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

package org.moe.kotlinmuseummap.common

import org.moe.kotlinmuseummap.common.core.Utils
import org.moe.kotlinmuseummap.common.model.Museum
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*

object MuseumSearchEngine {
    private val GOOGLE_MAPS_WEB_API_KEY = "WEB_GOOGLE_MAPS_API_KEY"

    private val requestTemplate = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=__latitude__,__longitude__&radius=50000&types=museum&key=API_KEY_HERE"

    var lastError :String? = ""
        private set

    fun find(latitude: Double, longitude: Double): ArrayList<Museum>? {
        val request = requestTemplate.replace("__latitude__", latitude.toString()).replace("__longitude__", longitude.toString()).replace("API_KEY_HERE", GOOGLE_MAPS_WEB_API_KEY)

        val url: URL
        val museums: ArrayList<Museum>?
        try {
            url = URL(request)
            println(request)

            val inputStream = url.openStream()
            var inputStreamReader = InputStreamReader(inputStream)
            museums = Utils.get(inputStreamReader.readText()) as ArrayList<Museum>?
            inputStreamReader.close()
            inputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
            lastError = e.message
            museums = null
        }

        return museums
    }
}