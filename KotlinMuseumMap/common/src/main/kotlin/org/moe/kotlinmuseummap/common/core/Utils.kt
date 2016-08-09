package org.moe.kotlinmuseummap.common.core

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import org.moe.kotlinmuseummap.common.database.ISQLiteContentValues
import org.moe.kotlinmuseummap.common.database.ISQLiteCursor
import org.moe.kotlinmuseummap.common.database.ISQLiteDatabase
import org.moe.kotlinmuseummap.common.model.Museum
import org.moe.kotlinmuseummap.common.model.db.DataBaseField
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getDateStringInDateFormat(date: Date): String {
        val format = SimpleDateFormat("EEEE, MM/dd/yy kk:mm")
        return format.format(date)
    }

    fun getDateStringInTimeFormat(date: Date): String {
        val format = SimpleDateFormat("kk:mm")
        return format.format(date)
    }

    fun executeSQLStatement(db: ISQLiteDatabase?, statement: String) {
        db!!.execSQL(statement)
    }

    fun createTableSQL(tableName: String, fields: Array<DataBaseField?>): String {
        var statement = "CREATE TABLE IF NOT EXISTS $tableName("
        for (i in fields.indices) {
            statement += fields[i]!!.fieldName + " " + fields[i]!!.fieldType
            if (fields[i]!!.isPrimaryKey) {
                statement += " PRIMARY KEY"
            }
            if (fields[i]!!.isAutoIncrement) {
                statement += " AUTOINCREMENT"
            }
            if (fields[i]!!.isNotNull) {
                statement += " NOT NULL"
            }
            if (i != fields.size - 1) {
                statement += ", "
            }
        }
        statement += ");"
        return statement
    }

    fun createClauseWhereFieldEqualsValue(field: DataBaseField, value: Any): String {
        val clause = field.fieldName + "=" + value
        return clause
    }

    fun dropTableIfExistsSQL(tableName: String): String {
        val statement = "DROP TABLE IF EXISTS $tableName;"
        return statement
    }

    fun insertObject(db: ISQLiteDatabase, tableName: String, values: ISQLiteContentValues) {
        db.insert(tableName, null, values)
    }

    fun deleteObject(db: ISQLiteDatabase, tableName: String, whereClause: String) {
        db.delete(tableName, whereClause, null)
    }

    fun clearObjects(db: ISQLiteDatabase, tableName: String) {
        db.delete(tableName, null, null)
    }

    fun getAllObjects(db: ISQLiteDatabase, tableName: String, fields: Array<DataBaseField>): ISQLiteCursor? {
        return getAllObjectsOrderedByParam(db, tableName, fields, null)
    }

    fun getAllObjectsOrderedByParam(db: ISQLiteDatabase, tableName: String, fields: Array<DataBaseField>, orderedBy: String?): ISQLiteCursor? {
        val fieldNames = arrayOfNulls<String>(fields.size)
        for (i in fields.indices) {
            fieldNames[i] = fields[i].fieldName
        }
        return db.query(tableName, fieldNames, null, null, null, null, orderedBy)
    }

    operator fun get(text: String?): List<Museum>? {
        val gson = Gson()
        val museums = ArrayList<Museum>()

        var results = gson.fromJson(text, Map::class.java)
        var listMuseum = results.get("results") as ArrayList<*>

        for (i in 0..listMuseum.size - 1) {
            var node: LinkedTreeMap<*, *>? = listMuseum[i] as LinkedTreeMap<*, *>? ?: break

            var name :String = node!!.get("name") as String

            if (name == null)
                name = "Unknown"

            node = node.get("geometry") as LinkedTreeMap<*, *>?
            if (node == null) break
            node = node.get("location") as LinkedTreeMap<*, *>?
            if (node == null) break
            val lat = node.get("lat") as Double
            val lng = node.get("lng") as Double
            museums.add(Museum(name, lat, lng))
        }

        return museums
    }
}