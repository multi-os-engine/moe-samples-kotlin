package org.moe.kotlinmuseummap.ios.db

import org.moe.natj.general.ptr.VoidPtr
import org.moe.kotlinmuseummap.common.database.ISQLiteContentValues
import org.moe.kotlinmuseummap.common.database.ISQLiteCursor
import org.moe.kotlinmuseummap.common.database.ISQLiteDatabase

class SQLiteDatabase(private val dbHandle: VoidPtr?) : ISQLiteDatabase {

    init {
        if (dbHandle == null) {
            throw NullPointerException("dbHandle can't be null")
        }
    }

    override fun newContentValues(): ISQLiteContentValues {
        return SQLiteContentValues()
    }

    override fun query(table: String?, columns: Array<String?>, selection: String?,
              selectionArgs: Array<String>?, groupBy: String?, having: String?,
              orderBy: String?): ISQLiteCursor? {
        if (selectionArgs != null || groupBy != null || having != null || orderBy != null) {
            throw RuntimeException("Unimplemented")
        }
        val sql = StringBuilder()
        sql.append("SELECT ")
        if (columns == null || columns.size == 0) {
            sql.append("*")
        } else {
            for (`val` in columns) {
                sql.append(`val`)
                sql.append(",")
            }
            sql.deleteCharAt(sql.length - 1)
        }
        sql.append(" FROM ")
        sql.append(table)
        if (!TextUtils.isEmpty(selection)) {
            sql.append(" WHERE (")
            sql.append(selection)
            sql.append(")")
        }

        val stmt = SQLiteStatement(sql.toString(), null)
        if (stmt.prepare(dbHandle)) {
            return stmt.query()
        } else {
            System.err.println("Error querying - " + stmt.lastError)
            System.err.println("\tin: " + stmt.statement)
        }
        return null
    }

    override fun rawQuery(sql: String, selectionArgs: Array<String>?): ISQLiteCursor? {
        val stmt = SQLiteStatement(sql, null)
        if (stmt.prepare(dbHandle)) {
            return stmt.query()
        } else {
            System.err.println("Error querying - " + stmt.lastError)
            System.err.println("\tin: " + stmt.statement)
        }
        return null
    }

    override fun delete(table: String, whereClause: String?, whereArgs: Array<String>?): Int {
        if (whereArgs != null) {
            throw RuntimeException("Unimplemented")
        }

        val stmt = SQLiteStatement("DELETE FROM " + table + (if (!TextUtils.isEmpty(whereClause))
            " WHERE " + whereClause
        else
            ""), null)
        var affected = 0
        if (stmt.prepare(dbHandle)) {
            if (!stmt.exec()) {
                System.err.println("Error deleting - " + stmt.lastError)
            } else {
                affected = stmt.affectedCount
            }
        } else {
            System.err.println("Error deleting - " + stmt.lastError)
            System.err.println("\tin: " + stmt.statement)
        }
        return affected
    }

    override fun insert(table: String, nullColumnHack: String?, values: ISQLiteContentValues): Int {
        val sql = StringBuilder()
        sql.append("INSERT")
        sql.append(" INTO ")
        sql.append(table)
        sql.append('(')

        var bindArgs: Array<Any?>? = null
        val size = if ((values.size() > 0))
            values.size()
        else
            0
        if (size > 0) {
            bindArgs = arrayOfNulls<Any>(size)
            var i = 0
            for (colName in values.keySet()) {
                sql.append(if ((i > 0)) "," else "")
                sql.append(colName)
                bindArgs[i++] = values.get(colName)
            }
            sql.append(')')
            sql.append(" VALUES (")
            i = 0
            while (i < size) {
                sql.append(if ((i > 0)) ",?" else "?")
                i++
            }
        } else {
            sql.append(nullColumnHack + ") VALUES (NULL")
        }
        sql.append(')')

        val stmt = SQLiteStatement(sql.toString(), bindArgs)
        var lastInsertedID = -1
        if (stmt.prepare(dbHandle)) {
            if (!stmt.exec()) {
                System.err.println("Error inserting - " + stmt.lastError)
            } else {
                lastInsertedID = stmt.lastInsertedID.toInt()
            }
        } else {
            System.err.println("Error inserting - " + stmt.lastError)
            System.err.println("\tin: " + stmt.statement)
        }
        return lastInsertedID
    }

    override fun update(tableName: String, values: ISQLiteContentValues,
               whereClause: String, whereArgs: Array<String>?) {
        if (values == null || values!!.size() === 0) {
            throw IllegalArgumentException("Empty values")
        }

        val sql = StringBuilder(120)
        sql.append("UPDATE ")
        sql.append(tableName)
        sql.append(" SET ")

        // move all bind args to one array
        val setValuesSize = values!!.size()
        val bindArgsSize = if ((whereArgs == null)) setValuesSize else (setValuesSize + whereArgs.size)
        val bindArgs = arrayOfNulls<Any>(bindArgsSize)
        var i = 0
        for (colName in values!!.keySet()) {
            sql.append(if ((i > 0)) "," else "")
            sql.append(colName)
            bindArgs[i++] = values!!.get(colName)
            sql.append("=?")
        }
        if (whereArgs != null) {
            i = setValuesSize
            while (i < bindArgsSize) {
                bindArgs[i] = whereArgs[i - setValuesSize]
                i++
            }
        }
        if (!TextUtils.isEmpty(whereClause)) {
            sql.append(" WHERE ")
            sql.append(whereClause)
        }

        val stmt = SQLiteStatement(sql.toString(), bindArgs)
        if (stmt.prepare(dbHandle)) {
            if (!stmt.exec()) {
                System.err.println("Error updating - " + stmt.lastError)
            }
        } else {
            System.err.println("Error updating - " + stmt.lastError)
            System.err.println("\tin: " + stmt.statement)
        }
    }

    override fun execSQL(statement: String) {
        val stmt = SQLiteStatement(statement, null)
        if (stmt.prepare(dbHandle)) {
            if (!stmt.exec()) {
                System.err.println("Error executing - " + stmt.lastError)
            }
        } else {
            System.err.println("Error executing - " + stmt.lastError)
            System.err.println("\tin: " + stmt.statement)
        }
    }
}