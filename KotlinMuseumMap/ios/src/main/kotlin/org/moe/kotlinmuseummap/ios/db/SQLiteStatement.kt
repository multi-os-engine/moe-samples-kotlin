package org.moe.kotlinmuseummap.ios.db

import org.moe.natj.general.ptr.Ptr
import org.moe.natj.general.ptr.VoidPtr
import org.moe.natj.general.ptr.impl.PtrFactory
import org.sqlite.c.Globals

class SQLiteStatement(val statement: String?, bindArgs: Array<Any?>?) {

    private val bindArgs: Array<Any?>

    internal var stmtHandle:

            VoidPtr? = null
        private set

    internal var dbHandle:

            VoidPtr? = null
        private set

    internal var lastError:

            String? = null
        private set

    var affectedCount = 0
        private set

    var lastInsertedID: Long = -1
        private set

    init {
        if (statement == null) {
            throw NullPointerException()
        }
        this.bindArgs = bindArgs ?: arrayOfNulls<Any>(0)
    }

    fun prepare(dbHandle: VoidPtr?): Boolean {
        if (dbHandle == null) {
            throw NullPointerException()
        }
        this.dbHandle = dbHandle

        @SuppressWarnings("unchecked")
        val stmtRef = PtrFactory.newPointerPtr(Void::class.java, 2, 1, true, false) as Ptr<VoidPtr>
        var err = Globals.sqlite3_prepare_v2(dbHandle, statement, -1, stmtRef,
                null)
        if (err != 0) {
            lastError = Globals.sqlite3_errmsg(dbHandle)
            return false
        }
        stmtHandle = stmtRef.get()
        var idx = 0
        for (bind in bindArgs) {
            idx++
            if (bind is String) {
                err = Globals.sqlite3_bind_text(stmtHandle, idx, bind as String?, -1, object : Globals.Function_sqlite3_bind_text {
                    override fun call_sqlite3_bind_text(arg0: VoidPtr) {

                    }
                })
            } else if (bind is Int) {
                err = Globals.sqlite3_bind_int(stmtHandle, idx, bind)
            } else if (bind is Long) {
                err = Globals.sqlite3_bind_int64(stmtHandle, idx, bind)
            } else if (bind is Double) {
                err = Globals.sqlite3_bind_double(stmtHandle, idx, bind)
            } else if (bind == null) {
                err = Globals.sqlite3_bind_null(stmtHandle, idx)
            } else {
                lastError = "No implemented SQLite3 bind function found for " + bind.javaClass.name
                return false
            }
            if (err != 0) {
                lastError = Globals.sqlite3_errmsg(dbHandle)
                return false
            }
        }
        return true
    }

    fun exec(): Boolean {
        if (stmtHandle == null) {
            throw RuntimeException("statement handle is closed")
        }
        val err = Globals.sqlite3_step(stmtHandle)
        if (err == 101 /* SQLITE_DONE */) {
            affectedCount = Globals.sqlite3_changes(dbHandle)
            lastInsertedID = Globals.sqlite3_last_insert_rowid(dbHandle)
        }
        close()
        if (err != 101 /* SQLITE_DONE */) {
            lastError = Globals.sqlite3_errmsg(dbHandle)
            return false
        }
        return true
    }

    fun query(): SQLiteCursor {
        return SQLiteCursor(this)
    }

    private fun close() {
        if (stmtHandle != null) {
            Globals.sqlite3_finalize(stmtHandle)
            stmtHandle = null
        }
    }

    internal fun step(): Boolean {
        if (stmtHandle == null) {
            throw RuntimeException("statement handle is closed")
        }
        val err = Globals.sqlite3_step(stmtHandle)
        if (err != 100 /* SQLITE_ROW */) {
            lastError = Globals.sqlite3_errmsg(dbHandle)
            return false
        }
        return true
    }

    internal fun reset() {
        if (stmtHandle == null) {
            throw RuntimeException("statement handle is closed")
        }
        Globals.sqlite3_reset(stmtHandle)
    }
}