package org.moe.kotlinmuseummap.common.model.db

import org.moe.kotlinmuseummap.common.core.Utils
import org.moe.kotlinmuseummap.common.database.ISQLiteCursor
import org.moe.kotlinmuseummap.common.database.ISQLiteDatabase
import org.moe.kotlinmuseummap.common.database.ISQLiteDatabaseHelper
import org.moe.kotlinmuseummap.common.model.Museum
import org.moe.kotlinmuseummap.common.model.entities.MuseumEntity
import java.util.*

class DataSource(private val dbHelper: ISQLiteDatabaseHelper) {


    private var db: ISQLiteDatabase? = null

    fun open() {
        db = dbHelper.writableDatabase
        Utils.executeSQLStatement(db, Utils.createTableSQL(MuseumEntity.TABLE_NAME, MuseumEntity.fields))
    }

    fun close() {
        dbHelper.close()
    }

    fun createMuseum(museum: Museum) {
        MuseumEntity.saveToDB(db, museum)
    }

    fun updateMuseum(museum: Museum) {
        MuseumEntity.updateToDB(db, museum)
    }

    fun deleteMuseum(id: Int) {
        MuseumEntity.deleteFromDB(db, id)
    }

    fun getMuseumById(id: Int): Museum? {
        val cursor = MuseumEntity.selectFromDB(db, MuseumEntity.ID.fieldName + " = " + id) ?: return null

        cursor.moveToFirst()
        var museum: Museum? = null
        if (!cursor.isAfterLast) {
            museum = MuseumEntity.cursorToObject(cursor)
        }
        cursor.close()
        return museum
    }

    val allMuseum: ArrayList<Museum>?
        get() {
            val cursor = MuseumEntity.selectFromDB(db, null) ?: return null

            val museums = ArrayList<Museum>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val museum = MuseumEntity.cursorToObject(cursor)
                museums.add(museum)
                cursor.moveToNext()
            }
            cursor.close()
            return museums
        }

    fun getMuseumsByAllParameters(name: String, lat: Double, lng: Double): ArrayList<Museum> {
        val museums = ArrayList<Museum>()
        val cursor: ISQLiteCursor?
        cursor = MuseumEntity.selectFromDB(db, "UPPER(" + MuseumEntity.NAME.fieldName + ") LIKE UPPER(\"%" + name.replace("\"", "") + "%\") and " + MuseumEntity.LATITUDE.fieldName + "=" + lat.toString() + " and " + MuseumEntity.LONGITUDE.fieldName + "=" + lng.toString())

        cursor!!.moveToFirst()
        while (!cursor.isAfterLast) {
            val museum = MuseumEntity.cursorToObject(cursor)
            museums.add(museum)
            cursor.moveToNext()
        }
        cursor.close()
        return museums
    }
}