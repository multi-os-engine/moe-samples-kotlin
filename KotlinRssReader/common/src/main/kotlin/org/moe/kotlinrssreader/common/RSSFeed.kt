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

package org.moe.kotlinrssreader.common

import org.w3c.dom.Node
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class RSSFeed(string: String) {

    val items = ArrayList<RSSFeedItem>()
    var lastErrorMessage = ""
        private set

    init {
        var inputStream: InputStream? = null
        var `in`: BufferedInputStream? = null
        try {
            val url = URL(string)
            inputStream = url.openStream()
            `in` = BufferedInputStream(inputStream)
            val builderf = DocumentBuilderFactory.newInstance()
            val builder = builderf.newDocumentBuilder()
            val doc = builder.parse(`in`)


            val list = doc.childNodes
            for (i in 0..list.length - 1) {
                if (list.item(i).nodeName == "rss") {
                    handleRSSNode(list.item(i))
                }
            }
        } catch (e: Exception) {
            lastErrorMessage += e.message
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    lastErrorMessage += e.message
                }

            }
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    lastErrorMessage += e.message
                }

            }
        }
    }

    private fun handleRSSNode(rss: Node) {
        val list = rss.childNodes
        for (i in 0..list.length - 1) {
            if (list.item(i).nodeName == "channel") {
                handleChannelNode(list.item(i))
            }
        }
    }

    private fun handleChannelNode(channel: Node) {
        val list = channel.childNodes
        for (i in 0..list.length - 1) {
            if (list.item(i).nodeName == "item") {
                handleItemNode(list.item(i))
            }
        }
    }

    private fun handleItemNode(item: Node) {
        val fi = RSSFeedItem()
        val list = item.childNodes
        for (i in 0..list.length - 1) {
            val node = list.item(i)
            if (node.nodeName == "title") {
                fi.title = node.textContent
            } else if (node.nodeName == "link") {
                fi.url = node.textContent
            } else if (node.nodeName == "description") {
                fi.description = node.textContent
            } else if (node.nodeName == "pubDate") {
                fi.pubDate = node.textContent
            }
        }
        items.add(fi)
    }

    val count: Int
        get() = items.size

    operator fun get(row: Int): RSSFeedItem {
        return items[row]
    }

}