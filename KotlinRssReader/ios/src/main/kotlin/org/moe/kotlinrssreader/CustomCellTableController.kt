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
import apple.foundation.NSIndexPath
import apple.uikit.UITableView
import apple.uikit.UITableViewCell
import apple.uikit.UITableViewController
import java.util.*

abstract class CustomCellTableController protected constructor(peer: Pointer) : UITableViewController(peer) {

    val options = ArrayList<Any>()

    interface EventListener {
        fun tableViewDidSelectRow(tableView: UITableView, row: Any)
    }

    var listener: EventListener? = null

    external override fun init(): CustomCellTableController

    override fun viewDidLoad() {
        super.viewDidLoad()

        prepareController()
    }

    protected abstract fun prepareController()

    protected fun add(elem: Any) {
        options.add(elem)
    }

    override fun numberOfSectionsInTableView(tableView: UITableView?): Long {
        return 1
    }

    override fun tableViewNumberOfRowsInSection(tableView: UITableView, section: Long): Long {
        return options.size.toLong()
    }

    override fun tableViewCellForRowAtIndexPath(
            tableView: UITableView, indexPath: NSIndexPath): UITableViewCell {

        setupCellAtIndex(tableView.dequeueReusableCellWithIdentifierForIndexPath(CELL_IDENTIFIER,
                indexPath), options[indexPath.row().toInt()])

        return tableView.dequeueReusableCellWithIdentifierForIndexPath(CELL_IDENTIFIER,
                indexPath)
    }

    protected abstract fun setupCellAtIndex(cell: UITableViewCell,
                                            rowData: Any)

    override fun tableViewDidSelectRowAtIndexPath(tableView: UITableView?,
                                                  indexPath: NSIndexPath?) {
        if (listener != null) {
            listener!!.tableViewDidSelectRow(tableView as UITableView, options[indexPath!!.row().toInt()])
        }
    }

    companion object {

        val CELL_IDENTIFIER = "Cell"

        @JvmStatic external fun alloc(): CustomCellTableController
    }

}