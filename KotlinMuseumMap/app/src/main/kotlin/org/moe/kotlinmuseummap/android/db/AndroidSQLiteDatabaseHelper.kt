package org.moe.kotlinmuseummap.android.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.moe.kotlinmuseummap.common.core.Utils
import org.moe.kotlinmuseummap.common.database.ISQLiteDatabase
import org.moe.kotlinmuseummap.common.database.ISQLiteDatabaseHelper
import org.moe.kotlinmuseummap.common.model.entities.MuseumEntity
import java.io.InputStream

class AndroidSQLiteDatabaseHelper(ctx: Context, dbName: String) : ISQLiteDatabaseHelper {

    internal var db: AndroidSQLiteDatabase? = null

    private inner class DBOpenHelper(context: Context, name: String,
                                     factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(sqlDb: SQLiteDatabase) {
            if (db == null) {
                db = AndroidSQLiteDatabase(sqlDb)
            }
            Utils.executeSQLStatement(db, Utils.createTableSQL(MuseumEntity.TABLE_NAME,
                    MuseumEntity.fields))
        }

        override fun onUpgrade(sqlDb: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if (db == null) {
                db = AndroidSQLiteDatabase(sqlDb)
            }
            Utils.executeSQLStatement(db, Utils.dropTableIfExistsSQL(MuseumEntity.TABLE_NAME))
            onCreate(sqlDb)
        }

    }

    private var dbH: DBOpenHelper

    init {
        dbH = DBOpenHelper(ctx, dbName, null, 2)
    }

    override fun close() {
        dbH.close()
        db = null
    }

    override val writableDatabase: ISQLiteDatabase?
        get() {
            if (db == null) {
                db = AndroidSQLiteDatabase(dbH.writableDatabase)
            }
            return db
        }

    // TODO Auto-generated method stub
    override val defaultDatabaseContents: InputStream?
        get() = null
}