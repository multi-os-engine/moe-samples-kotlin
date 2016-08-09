package org.moe.kotlinmuseummap.android.db

import android.database.Cursor
import org.moe.kotlinmuseummap.common.database.ISQLiteCursor

class AndroidCursor(private val cursor: Cursor?) : ISQLiteCursor {

    init {
        if (cursor == null) throw IllegalArgumentException("cursor must not be null")
    }

    override fun close() {
        cursor!!.close()
    }

    override fun getInt(arg0: Int): Int {
        return cursor!!.getInt(arg0)
    }

    override fun getLong(arg0: Int): Long {
        return cursor!!.getLong(arg0)
    }

    override fun getString(arg0: Int): String {
        return cursor!!.getString(arg0)
    }

    override val isAfterLast: Boolean
        get() = cursor!!.isAfterLast

    override fun moveToFirst() {
        cursor!!.moveToFirst()
    }

    override fun moveToNext() {
        cursor!!.moveToNext()
    }

    override fun getDouble(i: Int): Double {
        return cursor!!.getDouble(i)
    }

}