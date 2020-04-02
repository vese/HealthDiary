package com.example.healthdiary.model.medicaments

import android.database.Cursor
import java.io.Serializable

data class Medicament(
    val name: String,
    val price: String,
    val dosage: String,
    val manufacturer: String
) : Serializable {

    fun toParams() : String = "'$name', '$price', '$dosage', '$manufacturer'"

    companion object {
        const val MODEL_NAME = "Medicament"
        const val ID_COLUMN_NAME = "_id"
        const val NAME_COLUMN_NAME = "name"
        const val PRICE_COLUMN_NAME = "price"
        const val DOSAGE_COLUMN_NAME = "dosage"
        const val MANUFACTURER_COLUMN_NAME = "manufacturer"

        val createQuery
            get() = "CREATE TABLE $MODEL_NAME (" +
                    "$ID_COLUMN_NAME INTEGER PRIMARY KEY," +
                    "$NAME_COLUMN_NAME TEXT UNIQUE," +
                    "$PRICE_COLUMN_NAME TEXT," +
                    "$DOSAGE_COLUMN_NAME TEXT," +
                    "$MANUFACTURER_COLUMN_NAME TEXT" +
                    ")"

        val dropQuery get() = "DROP TABLE IF EXISTS $MODEL_NAME"

        val selectQuery get() = "SELECT * FROM $MODEL_NAME ORDER BY $ID_COLUMN_NAME"

        fun getUpdateQuery(medicament: Medicament) =
            "INSERT OR REPLACE INTO $MODEL_NAME ($NAME_COLUMN_NAME, $PRICE_COLUMN_NAME, $DOSAGE_COLUMN_NAME, $MANUFACTURER_COLUMN_NAME) VALUES (${medicament.toParams()});"


        fun getList(cursor: Cursor): List<Medicament> {
            val result: MutableList<Medicament> = ArrayList()
            if (cursor.moveToFirst()) {
                result.add(mapMedicamentFromCursor(cursor))
                while (cursor.moveToNext()) {
                    result.add(mapMedicamentFromCursor(cursor))
                }
                cursor.close()
            }
            return result
        }

        private fun mapMedicamentFromCursor(cursor: Cursor): Medicament = Medicament(
            cursor.getString(cursor.getColumnIndex(NAME_COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(PRICE_COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(DOSAGE_COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(MANUFACTURER_COLUMN_NAME))
        )
    }
}
