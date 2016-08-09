package org.moe.kotlinmuseummap.android.db

object TextUtils {

    fun isEmpty(whereClause: String?): Boolean {
        if (whereClause == null) {
            return true
        }
        return whereClause.trim { it <= ' ' }.length == 0
    }

}