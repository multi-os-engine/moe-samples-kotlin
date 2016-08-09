package org.moe.kotlinmuseummap.android.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.moe.kotlinmuseummap.common.database.ISQLiteContentValues
import org.moe.kotlinmuseummap.common.database.ISQLiteCursor
import org.moe.kotlinmuseummap.common.database.ISQLiteDatabase

class AndroidSQLiteDatabase(private val db: SQLiteDatabase?) : ISQLiteDatabase {

    init {
        if (db == null) {
            throw IllegalArgumentException("dbHandle can't be null")
        }
    }

    override fun newContentValues(): ISQLiteContentValues {
        return AndroidContentValues()
    }

    override fun query(table: String?, columns: Array<String?>, selection: String?,
                       selectionArgs: Array<String>?, groupBy: String?, having: String?,
                       orderBy: String?): ISQLiteCursor? {
        val cursor = db!!.query(table, columns, selection, selectionArgs, groupBy, having, orderBy) ?: return null
        return AndroidCursor(cursor)
    }

    override fun delete(table: String, whereClause: String?, whereArgs: Array<String>?): Int {
        return db!!.delete(table, whereClause, whereArgs)
    }

    override fun insert(table: String, nullColumnHack: String?, values: ISQLiteContentValues): Int {
        val values = convertFromValues(values)
        return db!!.insert(table, nullColumnHack, values).toInt()
    }

    private fun convertFromValues(initialValues: ISQLiteContentValues): ContentValues {
        return (initialValues as AndroidContentValues).contentValues
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

        val stmt = db!!.compileStatement(sql.toString())
        var idx = 0
        for (bind in bindArgs) {
            idx++
            if (bind is String) {
                stmt.bindString(idx, bind)
            } else if (bind is Int) {
                stmt.bindLong(idx, bind.toLong())
            } else if (bind is Long) {
                stmt.bindLong(idx, bind)
            } else if (bind is Double) {
                stmt.bindDouble(idx, bind)
            }
        }
        stmt.executeUpdateDelete()
    }

    override fun execSQL(statement: String) {
        db!!.execSQL(statement)
    }

    override fun rawQuery(sql: String, selectionArgs: Array<String>?): ISQLiteCursor? {
        val cursor = db!!.rawQuery(sql, selectionArgs)
        return if (cursor == null) null else AndroidCursor(cursor)
    }
}