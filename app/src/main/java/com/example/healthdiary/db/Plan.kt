package com.example.healthdiary.db

import android.database.Cursor
import kotlin.collections.ArrayList

class Plan(var date: String, var description: String) {

    companion object {
        private const val MODEL_NAME = "Plan"
        private const val ID_COLUMN_NAME = "_id"
        private const val DATE_COLUMN_NAME = "date"
        private const val DESCRIPTION_COLUMN_NAME = "description"

        val createQuery
            get() = "CREATE TABLE $MODEL_NAME (" +
                    "$ID_COLUMN_NAME INTEGER PRIMARY KEY," +
                    "$DATE_COLUMN_NAME TEXT," +
                    "$DESCRIPTION_COLUMN_NAME TEXT," +
                    "UNIQUE($DATE_COLUMN_NAME, $DESCRIPTION_COLUMN_NAME)" +
                    ")"

        val dropQuery get() = "DROP TABLE IF EXISTS $MODEL_NAME"

        val selectQuery get() = "SELECT * FROM $MODEL_NAME ORDER BY $ID_COLUMN_NAME DESC"

        fun getSelectByDateQuery(date: String) =
            "SELECT * FROM $MODEL_NAME WHERE $DATE_COLUMN_NAME = '$date'"

        fun getInsertQuery(plan: Plan): String =
            "INSERT OR IGNORE INTO $MODEL_NAME ($DATE_COLUMN_NAME, $DESCRIPTION_COLUMN_NAME) VALUES ('${plan.date}', '${plan.description}');"

        fun getPlanList(cursor: Cursor?): ArrayList<Plan> {
            val result: ArrayList<Plan> = ArrayList()
            if (cursor!!.moveToFirst()) {
                result.add(getPlan(cursor))
                while (cursor.moveToNext()) {
                    result.add(getPlan(cursor))
                }
            }
            return result
        }

        private fun getPlan(cursor: Cursor): Plan {
            return Plan(
                cursor.getString(cursor.getColumnIndex(DATE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN_NAME))
            )
        }
    }
}