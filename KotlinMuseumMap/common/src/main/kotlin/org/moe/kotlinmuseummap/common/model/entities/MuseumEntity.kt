package org.moe.kotlinmuseummap.common.model.entities

import org.moe.kotlinmuseummap.common.core.Utils
import org.moe.kotlinmuseummap.common.database.ISQLiteCursor
import org.moe.kotlinmuseummap.common.database.ISQLiteDatabase
import org.moe.kotlinmuseummap.common.model.Museum
import org.moe.kotlinmuseummap.common.model.db.DataBaseField

object MuseumEntity {

    val TABLE_NAME = "MUSEUMS"

    private val SQLITE_TYPE_TEXT = "TEXT"
    private val SQLITE_TYPE_DOUBLE = "DOUBLE"
    private val SQLITE_TYPE_INTEGER = "INTEGER"

    val ID = DataBaseField("ID", SQLITE_TYPE_INTEGER, true, true)
    val NAME = DataBaseField("NAME", SQLITE_TYPE_TEXT)
    val LATITUDE = DataBaseField("LATITUDE", SQLITE_TYPE_DOUBLE)
    val LONGITUDE = DataBaseField("LONGITUDE", SQLITE_TYPE_DOUBLE)

    val fields = arrayOf<DataBaseField?>(ID, NAME, LATITUDE, LONGITUDE)
    val fieldNames = arrayOf<String?>(ID.fieldName as String, NAME.fieldName as String,
            LATITUDE.fieldName as String, LONGITUDE.fieldName as String)

    fun saveToDB(db: ISQLiteDatabase?, museum: Museum) {
        val values = db!!.newContentValues()
        values.put(MuseumEntity.NAME.fieldName as String, museum.name)
        values.put(MuseumEntity.LATITUDE.fieldName as String, museum.latitude)
        values.put(MuseumEntity.LONGITUDE.fieldName as String, museum.longitude)
        if (museum.id != -1) {
            db!!.update(TABLE_NAME, values, Utils.createClauseWhereFieldEqualsValue(ID, museum.id), null)
        } else {
            //values.put(MuseumEntity.ID.fieldName as String, museum.id)
            museum.id = db.insert(TABLE_NAME, null, values)
        }
    }

    fun updateToDB(db: ISQLiteDatabase?, museum: Museum) {
        val values = db!!.newContentValues()
        values.put(MuseumEntity.NAME.fieldName as String, museum.name)
        values.put(MuseumEntity.LATITUDE.fieldName as String, museum.latitude)
        values.put(MuseumEntity.LONGITUDE.fieldName as String, museum.longitude)
        db!!.update(TABLE_NAME, values, Utils.createClauseWhereFieldEqualsValue(ID, museum.id), null)
    }

    fun deleteFromDB(db: ISQLiteDatabase?, id: Int) {
        db!!.delete(TABLE_NAME, Utils.createClauseWhereFieldEqualsValue(ID, id), null)
    }

    fun selectFromDB(db: ISQLiteDatabase?, selection: String?): ISQLiteCursor? {
        val cursor = db!!.query(TABLE_NAME, fieldNames, selection, null, null, null, null)
        return cursor
    }

    fun selectMaxIDFromDB(db: ISQLiteDatabase): ISQLiteCursor? {
        val column = ID.fieldName
        val selection = "SELECT max($column) FROM $TABLE_NAME"
        val cursor = db.rawQuery(selection, null)
        return cursor
    }

    fun cursorToObject(cursor: ISQLiteCursor): Museum {
        val museum = Museum()
        museum.id = cursor.getInt(0)
        museum.name = cursor.getString(1)
        museum.latitude = cursor.getDouble(2)
        museum.longitude = cursor.getDouble(3)
        return museum
    }

}