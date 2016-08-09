package org.moe.kotlinmuseummap.android.db

import android.content.ContentValues
import org.moe.kotlinmuseummap.common.database.ISQLiteContentValues

class AndroidContentValues : ISQLiteContentValues {

    val contentValues: ContentValues

    init {
        contentValues = ContentValues()
    }

    override operator fun get(arg0: String): Any? {
        return contentValues.get(arg0)
    }

    override fun keySet(): Set<String> {
        return contentValues.keySet()
    }

    override fun put(arg0: String, arg1: String) {
        contentValues.put(arg0, arg1)
    }

    override fun put(arg0: String, arg1: Byte?) {
        contentValues.put(arg0, arg1)
    }

    override fun put(arg0: String, arg1: Short?) {
        contentValues.put(arg0, arg1)
    }

    override fun put(arg0: String, arg1: Int?) {
        contentValues.put(arg0, arg1)
    }

    override fun put(arg0: String, arg1: Long?) {
        contentValues.put(arg0, arg1)
    }

    override fun put(arg0: String, arg1: Float?) {
        contentValues.put(arg0, arg1)
    }

    override fun put(arg0: String, arg1: Double?) {
        contentValues.put(arg0, arg1)
    }

    override fun put(arg0: String, arg1: Boolean?) {
        contentValues.put(arg0, arg1)
    }

    override fun size(): Int {
        return contentValues.size()
    }

}