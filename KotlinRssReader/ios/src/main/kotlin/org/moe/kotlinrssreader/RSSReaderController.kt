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

import org.moe.natj.general.Pointer
import org.moe.natj.objc.SEL
import org.moe.natj.objc.ann.Selector
import org.moe.kotlinrssreader.common.Bookmarks
import org.moe.kotlinrssreader.common.RSSFeed
import org.moe.kotlinrssreader.common.RSSFeedItem
import ios.c.Globals
import ios.coregraphics.c.CoreGraphics
import ios.coregraphics.struct.CGSize
import ios.foundation.*
import ios.foundation.struct.NSRange
import ios.uikit.*
import ios.uikit.c.UIKit
import ios.uikit.enums.*
import ios.uikit.struct.UIEdgeInsets

class RSSReaderController protected constructor(peer: Pointer) : CustomCellTableController(peer) {

    private var loaderThread: Thread? = null

    override fun viewDidLoad() {
        super.viewDidLoad()

        setTitle("RSS Reader")

        val bmbutton = UIBarButtonItem.alloc().initWithTitleStyleTargetAction("Bookmarks",
                UIBarButtonItemStyle.Plain, this,
                SEL("showBookmarks"))
        navigationItem().setRightBarButtonItem(bmbutton)

        loadURL(Bookmarks.bookmarks[0])
    }

    protected override fun prepareController() {
        val nib = UINib.nibWithNibNameBundle("RSSCell", NSBundle.mainBundle())
        tableView().registerNibForCellReuseIdentifier(nib, CELL_IDENTIFIER)
    }

    private fun loadURL(url: String) {
        if (loaderThread != null) {
            return
        }

        loaderThread = Thread(object : Runnable {

            override fun run() {

                val feed = RSSFeed(url)
                if (!feed.lastErrorMessage.isEmpty()) {
                    handleError(feed.lastErrorMessage)
                    return
                }

                Globals.dispatch_sync(Globals.dispatch_get_main_queue(), object : Globals.Block_dispatch_sync {

                    override fun call_dispatch_sync() {
                        options.clear()
                        for (item in feed.items) {
                            add(item)
                        }
                        println("Finished loading url " + url)
                        tableView().reloadData()
                        tableView().scrollRectToVisibleAnimated(CoreGraphics.CGRectMake(0.0, 0.0, 1.0, 1.0), true)
                        loaderThread = null
                    }
                })

            }
        })
        loaderThread!!.start()
    }


    private fun handleError(errorMessage: String) {
        val alertView = UIAlertView.alloc().init()
        alertView.setMessage("Cannot Show Rss news: " + errorMessage)
        alertView.show()
    }

    protected override fun setupCellAtIndex(cell: UITableViewCell, rowData: Any) {
        val c = cell as RSSCell
        val i = rowData as RSSFeedItem

        c.rssTitle().setText(i.title)
        c.rssDescription().setText(i.description)
        c.rssDate().setText(i.pubDate)
    }

    @Selector("showBookmarks")
    fun showBookmarks() {
        val bms = SimpleTableController.alloc().init()
        bms.setTitle("Bookmarks")
        for (bm in Bookmarks.bookmarks) {
            bms.add(bm)
        }
        if (UIDevice.currentDevice().userInterfaceIdiom() == UIUserInterfaceIdiom.Phone) {
            bms.listener = (object : SimpleTableController.EventListener {
                override fun tableViewDidSelectRow(tableView: UITableView,
                                          row: String) {
                    loadURL(row)
                    navigationController().popViewControllerAnimated(true)
                }
            })
            navigationController().pushViewControllerAnimated(bms, true)
        } else {
            val ctrl = UIPopoverController.alloc().initWithContentViewController(bms)
            bms.listener = (object : SimpleTableController.EventListener {
                override fun tableViewDidSelectRow(tableView: UITableView,
                                          row: String) {
                    loadURL(row)
                    ctrl.dismissPopoverAnimated(true)
                }
            })
            ctrl.presentPopoverFromBarButtonItemPermittedArrowDirectionsAnimated(
                    navigationItem().rightBarButtonItem(),
                    UIPopoverArrowDirection.Any, true)
        }
    }

    override fun tableViewHeightForRowAtIndexPath(tableView: UITableView,
                                         indexPath: NSIndexPath): Double {
        val item = options.get(indexPath.row().toInt()) as RSSFeedItem
        var ttm = item.description
        if (ttm!!.endsWith("\n")) {
            ttm += "-"
        }
        val measure = NSString.stringWithString(ttm)

        // Width: 2 * 10 pixel margin + 5 pixel padding
        val size = CGSize(tableView.bounds().size().width() - 10.0 * 2.0 - textViewHPadding, 1000.0)
        val bounding = measure.boundingRectWithSizeOptionsAttributesContext(
                size, textViewDrawingOptions, textViewAttributes as NSDictionary<String, Any>, null)

        return Math.ceil(bounding.size().height() + textViewVPadding + 1.0) + 39.0 + 34.0
    }

    override fun tableViewDidSelectRowAtIndexPath(tableView: UITableView?,
                                         indexPath: NSIndexPath?) {
        val item = options.get(indexPath!!.row().toInt()) as RSSFeedItem
        val url = item.url
        if (url != null) {
            UIApplication.sharedApplication().openURL(NSURL.URLWithString(url))
        }
    }

    companion object {

        private val textViewHPadding: Double
        private val textViewVPadding: Double
        private val textViewDrawingOptions: Long
        private val textViewAttributes: NSDictionary<Any, Any>

        init {
            // Some consts
            val textContainerInsets = UIEdgeInsets(8.0, 0.0,
                    8.0, 0.0)
            val contentInsets = UIEdgeInsets(0.0, 0.0, 0.0,
                    0.0)
            val lineFragmentPadding = 5.0

            // Calculate paddings
            textViewHPadding = textContainerInsets.left() + textContainerInsets.right() + 2.0 * lineFragmentPadding + contentInsets.left() + contentInsets.right()
            textViewVPadding = textContainerInsets.top() + textContainerInsets.bottom() + contentInsets.top() + contentInsets.bottom()

            // Set options
            textViewDrawingOptions = NSStringDrawingOptions.UsesLineFragmentOrigin or NSStringDrawingOptions.UsesFontLeading

            val paragraphStyle = NSMutableParagraphStyle.alloc().init()
            paragraphStyle.setLineBreakMode(NSLineBreakMode.WordWrapping)

            val textStorage = NSTextStorage.alloc().initWithString("-")
            val textStorageAttrs = textStorage.attributesAtIndexEffectiveRange(0, NSRange(0, 1))
            val font = textStorageAttrs[UIKit.NSFontAttributeName()] as UIFont

            val attributes : NSMutableDictionary<Any,Any>? = NSMutableDictionary.dictionaryWithCapacity<Any, Any>(2) as NSMutableDictionary<Any,Any>?
            attributes!!.put(UIKit.NSFontAttributeName(), font)
            attributes!!.put(UIKit.NSParagraphStyleAttributeName(), paragraphStyle)
            textViewAttributes = attributes
        }

        @JvmStatic external fun alloc(): RSSReaderController
    }
}