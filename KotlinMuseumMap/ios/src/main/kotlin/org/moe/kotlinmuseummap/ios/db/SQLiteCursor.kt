package org.moe.kotlinmuseummap.ios.db

import org.moe.kotlinmuseummap.common.database.ISQLiteCursor
import org.sqlite.c.Globals

class SQLiteCursor(private var stmt: SQLiteStatement?) : ISQLiteCursor {

    override var isAfterLast = false

    init {
        moveToNext()
    }

    override fun moveToFirst() {
        if (stmt == null) {
            throw RuntimeException("statement is closed")
        }
        stmt!!.reset()
        moveToNext()
    }

    override fun close() {
        if (stmt == null) {
            throw RuntimeException("statement is closed")
        }
        stmt = null
    }

    override fun getString(i: Int): String {
        if (stmt == null) {
            throw RuntimeException("statement is closed")
        }
        return Globals.sqlite3_column_text(stmt!!.stmtHandle, i)
    }

    override fun getInt(i: Int): Int {
        if (stmt == null) {
            throw RuntimeException("statement is closed")
        }
        return Globals.sqlite3_column_int(stmt!!.stmtHandle, i)
    }

    override fun getLong(i: Int): Long {
        if (stmt == null) {
            throw RuntimeException("statement is closed")
        }
        return Globals.sqlite3_column_int64(stmt!!.stmtHandle, i)
    }

    override fun getDouble(i: Int): Double {
        if (stmt == null) {
            throw RuntimeException("statement is closed")
        }
        return Globals.sqlite3_column_double(stmt!!.stmtHandle, i)
    }

    override fun moveToNext() {
        if (stmt == null) {
            throw RuntimeException("statement is closed")
        }
        isAfterLast = !stmt!!.step()
    }

}