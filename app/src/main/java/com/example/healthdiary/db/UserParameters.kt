package com.example.healthdiary.db

import android.content.ContentValues
import android.database.Cursor
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserParameters {
    var date: String
    var weight: Float
    var height: Float

    constructor(weight: Float, height: Float) {
        this.date = SimpleDateFormat(DATE_FORMAT).format(Date())
        this.weight = weight
        this.height = height
    }

    constructor(date: String, weight: Float, height: Float) {
        this.date = date
        this.weight = weight
        this.height = height
    }

    val contentValues: ContentValues get() {
        val values = ContentValues()
        values.put(DATE_COLUMN_NAME, date)
        values.put(WEIGHT_COLUMN_NAME, weight)
        values.put(HEIGHT_COLUMN_NAME, height)
        return values
    }

    companion object {
        const val MODEL_NAME = "UserParameters"
        const val ID_COLUMN_NAME = "_id"
        const val DATE_COLUMN_NAME = "date"
        const val WEIGHT_COLUMN_NAME = "weight"
        const val HEIGHT_COLUMN_NAME = "height"
        const val DATE_FORMAT = "dd.MM.yyyy"

        val createQuery get() = "CREATE TABLE $MODEL_NAME (" +
                "$ID_COLUMN_NAME INTEGER PRIMARY KEY," +
                "$DATE_COLUMN_NAME TEXT," +
                "$WEIGHT_COLUMN_NAME REAL," +
                "$HEIGHT_COLUMN_NAME REAL" +
                ")"

        val dropQuery get() = "DROP TABLE IF EXISTS $MODEL_NAME"

        val selectQuery get() = "SELECT * FROM $MODEL_NAME ORDER BY $ID_COLUMN_NAME DESC LIMIT 5"

        val selectLastQuery get() = "SELECT * FROM $MODEL_NAME ORDER BY $ID_COLUMN_NAME DESC LIMIT 1"

        fun getUpdateQuery(userParameters: UserParameters): String {
            return "UPDATE $MODEL_NAME SET $WEIGHT_COLUMN_NAME = '${userParameters.weight}', $HEIGHT_COLUMN_NAME = '${userParameters.height}' WHERE $DATE_COLUMN_NAME = '${userParameters.date}'"
        }

        fun getUserParametersList(cursor: Cursor?): ArrayList<UserParameters> {
            cursor!!.moveToFirst()
            var result: ArrayList<UserParameters> = ArrayList()
            result.add(getUserParameters(cursor))
            while (cursor.moveToNext()) {
                result.add(getUserParameters(cursor))
            }
            cursor.close()
            return result
        }

        fun getUserParameters(cursor: Cursor): UserParameters {
            return UserParameters(cursor.getString(cursor.getColumnIndex(DATE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(WEIGHT_COLUMN_NAME)).toFloat(),
                cursor.getString(cursor.getColumnIndex(HEIGHT_COLUMN_NAME)).toFloat())
        }
    }
}