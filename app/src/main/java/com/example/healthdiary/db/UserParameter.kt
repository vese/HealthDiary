package com.example.healthdiary.db

import android.database.Cursor
import kotlin.collections.ArrayList

class UserParameter(var name: String, var value: String) {

    companion object {
        private const val MODEL_NAME = "UserParameter"
        private const val ID_COLUMN_NAME = "_id"
        private const val NAME_COLUMN_NAME = "name"
        const val VALUE_COLUMN_NAME = "value"

        val createQuery
            get() = "CREATE TABLE $MODEL_NAME (" +
                    "$ID_COLUMN_NAME INTEGER PRIMARY KEY," +
                    "$NAME_COLUMN_NAME TEXT UNIQUE," +
                    "$VALUE_COLUMN_NAME TEXT" +
                    ")"

        val dropQuery get() = "DROP TABLE IF EXISTS $MODEL_NAME"

        val selectQuery get() = "SELECT * FROM $MODEL_NAME ORDER BY $ID_COLUMN_NAME"

        fun getParameterQuery(name: String) =
            "SELECT $VALUE_COLUMN_NAME FROM $MODEL_NAME WHERE $NAME_COLUMN_NAME = '$name'"

        fun getUpdateQuery(userParameter: UserParameter) =
            "INSERT OR REPLACE INTO $MODEL_NAME ($NAME_COLUMN_NAME, $VALUE_COLUMN_NAME) VALUES ('${userParameter.name}', '${userParameter.value}');"


        fun getUserParametersList(cursor: Cursor?): ArrayList<UserParameter> {
            val result: ArrayList<UserParameter> = ArrayList()
            if (cursor!!.moveToFirst()) {
                result.add(getUserParameter(cursor))
                while (cursor.moveToNext()) {
                    result.add(getUserParameter(cursor))
                }
            }
            return result
        }

        private fun getUserParameter(cursor: Cursor) = UserParameter(
            cursor.getString(cursor.getColumnIndex(NAME_COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(VALUE_COLUMN_NAME))
        )
    }
}