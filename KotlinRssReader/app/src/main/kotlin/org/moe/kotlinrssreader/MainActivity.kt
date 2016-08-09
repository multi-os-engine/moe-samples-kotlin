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

package org.moe.kotlinrssreader

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import org.moe.kotlinrssreader.common.Bookmarks
import org.moe.kotlinrssreader.common.RSSFeed
import org.moe.kotlinrssreader.common.RSSFeedItem

class MainActivity : AppCompatActivity() {

    private var listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.mainListView) as ListView
        listView!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val rssItem = parent.getItemAtPosition(position) as RSSFeedItem
                var url = rssItem.url

                if (!url!!.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        }

        if (adapter == null)
            loadUrl(Bookmarks.bookmarks[0])
        else
            listView!!.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bookmarks) {
            val intent = Intent(this, BookmarksActivity::class.java)
            startActivityForResult(intent, 1)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        val address = data.getStringExtra("address")
        loadUrl(address)
    }

    private fun loadUrl(url: String) {
        val loadTaskParams = arrayOf(url)
        val loader = LoadTask()
        loader.execute(*loadTaskParams)
    }

    private fun updateListView(items: List<RSSFeedItem>) {
        adapter = null
        adapter = RssListAdapter(this, items)

        listView!!.adapter = adapter
    }

    private inner class LoadTask : AsyncTask<String, Void, Void>() {
        private var feed: RSSFeed? = null


        override fun doInBackground(vararg params: String): Void? {
            val url = params[0]

            feed = RSSFeed(url)

            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            if (feed!!.lastErrorMessage.isEmpty()) {
                updateListView(feed!!.items)
            } else
                handleError(feed!!.lastErrorMessage)
        }
    }

    private fun handleError(errorMessage: String) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Error!").setMessage("Cannot Show Rss news: " + errorMessage).setCancelable(false).setNegativeButton("OK",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        dialog.cancel()
                    }
                })
        val alert = builder.create()
        alert.show()
    }

    companion object {

        private var adapter: RssListAdapter? = null
    }
}