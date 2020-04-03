package com.example.healthdiary.db

import android.database.Cursor
import java.text.SimpleDateFormat
import java.util.*

class WaterTrackParameters {

    var date: String
    var currentIntake: Int

    constructor(currentIntake: Int) {
        this.date = SimpleDateFormat(DATE_FORMAT).format(Date());
        this.currentIntake = currentIntake;
    }

    constructor(date: String, currentIntake: Int) {
        this.date = date;
        this.currentIntake = currentIntake;
    }

    companion object {
        const val MODEL_NAME = "WaterTracking"
        const val ID_COLUMN_NAME = "id"
        const val DATE_COLUMN_NAME = "date"
        const val CURRENTINTAKE_COLUMN_NAME = "currentIntake"
        const val DATE_FORMAT = "dd.MM.yyyy"

        val createQuery
            get() = "CREATE TABLE $MODEL_NAME (" +
                    "$ID_COLUMN_NAME INTEGER PRIMARY KEY," +
                    "$DATE_COLUMN_NAME TEXT UNIQUE," +
                    "$CURRENTINTAKE_COLUMN_NAME INTEGER" +
                    ")"

        val dropQuery get() = "DROP TABLE IF EXISTS $MODEL_NAME"

        val selectQuery get() = "SELECT * FROM $MODEL_NAME ORDER BY $ID_COLUMN_NAME"

        val selectLastQuery
            get() = "SELECT * FROM $MODEL_NAME " +
                    "WHERE $DATE_COLUMN_NAME = '${SimpleDateFormat(DATE_FORMAT).format(Date())}'"

        fun getUpdateQuery(waterTrackParameters: WaterTrackParameters) =
            "INSERT OR REPLACE INTO $MODEL_NAME ($CURRENTINTAKE_COLUMN_NAME, $DATE_COLUMN_NAME) " +
                    "VALUES (${waterTrackParameters.currentIntake},'${waterTrackParameters.date}') "

        fun getWaterTrackParametersList(cursor: Cursor?): ArrayList<WaterTrackParameters> {
            var result: ArrayList<WaterTrackParameters> = ArrayList()
            if (cursor!!.moveToFirst()) {
                result.add(getWaterTrackParameters(cursor))
                while (cursor.moveToNext()) {
                    result.add(getWaterTrackParameters(cursor))
                }
                cursor.close()
            }
            return result
        }

        fun getWaterTrackParameters(cursor: Cursor): WaterTrackParameters {
            return WaterTrackParameters(
                cursor.getString(cursor.getColumnIndex(DATE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(CURRENTINTAKE_COLUMN_NAME)).toInt()
            )
        }
    }
}