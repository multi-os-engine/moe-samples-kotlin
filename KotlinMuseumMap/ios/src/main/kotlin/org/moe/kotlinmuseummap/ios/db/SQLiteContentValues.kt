package org.moe.kotlinmuseummap.ios.db

import org.moe.kotlinmuseummap.common.database.ISQLiteContentValues
import java.util.*

class SQLiteContentValues : ISQLiteContentValues {

    private val mValues = HashMap<String, Any>()

    override fun put(key: String, value: String) {
        mValues.put(key, value)
    }

    override fun put(key: String, value: Byte?) {
        mValues.put(key, value as Any)
    }

    override fun put(key: String, value: Short?) {
        mValues.put(key, value as Any)
    }

    override fun put(key: String, value: Int?) {
        mValues.put(key, value as Any)
    }

    override fun put(key: String, value: Long?) {
        mValues.put(key, value as Any)
    }

    override fun put(key: String, value: Float?) {
        mValues.put(key, value as Any)
    }

    override fun put(key: String, value: Double?) {
        mValues.put(key, value as Any)
    }

    override fun put(key: String, value: Boolean?) {
        mValues.put(key, value as Any)
    }

    override fun size(): Int {
        return mValues.size
    }

    override fun keySet(): Set<String> {
        return mValues.keys
    }

    override operator fun get(colName: String): Any? {
        return mValues[colName]
    }

}