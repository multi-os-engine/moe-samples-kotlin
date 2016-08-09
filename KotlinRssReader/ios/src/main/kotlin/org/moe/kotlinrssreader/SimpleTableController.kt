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
import ios.foundation.NSArray
import ios.foundation.NSIndexPath
import ios.uikit.UITableView
import ios.uikit.UITableViewCell
import ios.uikit.UITableViewController
import ios.uikit.enums.UITableViewRowAnimation
import java.util.*

class SimpleTableController protected constructor(peer: Pointer) : UITableViewController(peer) {

    val options = ArrayList<String>()

    interface EventListener {
        fun tableViewDidSelectRow(tableView: UITableView, row: String)
    }

    var listener: EventListener? = null

    external override fun init(): SimpleTableController

    override fun viewDidLoad() {
        super.viewDidLoad()

        tableView().registerClassForCellReuseIdentifier(
                org.moe.natj.objc.Class("UITableViewCell"),
                CELL_IDENTIFIER)
    }

    fun add(elem: String) {
        options.add(elem)
        if (tableView() != null) {
            val path = NSIndexPath.indexPathForRowInSection(
                    (options.size - 1).toLong(), 0)
            val paths = NSArray.arrayWithObject(path)
            tableView().insertRowsAtIndexPathsWithRowAnimation(paths as NSArray<out NSIndexPath>?,
                    UITableViewRowAnimation.Automatic)
        }
    }

    override fun numberOfSectionsInTableView(tableView: UITableView?): Long {
        return 1
    }

    override fun tableViewNumberOfRowsInSection(tableView: UITableView, section: Long): Long {
        return options.size.toLong()
    }

    override fun tableViewCellForRowAtIndexPath(
            tableView: UITableView, indexPath: NSIndexPath): UITableViewCell {

        tableView.dequeueReusableCellWithIdentifierForIndexPath(CELL_IDENTIFIER,
                indexPath).textLabel().setText(options[indexPath.row().toInt()])

        return tableView.dequeueReusableCellWithIdentifierForIndexPath(CELL_IDENTIFIER,
                indexPath)
    }

    override fun tableViewDidSelectRowAtIndexPath(tableView: UITableView?,
                                                  indexPath: NSIndexPath?) {
        if (listener != null) {
            listener!!.tableViewDidSelectRow(tableView as UITableView,
                    options[indexPath!!.row().toInt()])
        }
    }

    companion object {

        private val CELL_IDENTIFIER = "Cell"

        @JvmStatic external fun alloc(): SimpleTableController
    }

}