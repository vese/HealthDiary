package com.example.healthdiary.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.healthdiary.model.medicaments.Medicament

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    constructor(context: Context) : this(context, null)
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserParameters.createQuery)
        db.execSQL(UserParameter.createQuery)
        db.execSQL(Plan.createQuery)
        db.execSQL(Medicament.createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameters.dropQuery)
        db.execSQL(UserParameter.dropQuery)
        db.execSQL(Plan.dropQuery)
        db.execSQL(Medicament.dropQuery)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameters.dropQuery)
        db.execSQL(UserParameter.dropQuery)
        db.execSQL(Plan.dropQuery)
        db.execSQL(Medicament.dropQuery)
        onCreate(db)
    }

    //user parameters
    fun getLastUserParameters(): UserParameters? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameters.selectLastQuery, null)
        return if (cursor!!.moveToFirst()) {
            UserParameters.getUserParameters(cursor)
        } else {
            null
        }
    }

    fun getUserParametersList(): ArrayList<UserParameters> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameters.selectQuery, null)
        return UserParameters.getUserParametersList(cursor)
    }

    fun insertUserParameters(userParameters: UserParameters) {
        val lastUserParameters = getLastUserParameters()
        if (lastUserParameters != null && lastUserParameters.date == userParameters.date) {
            if (lastUserParameters.weight != userParameters.weight || lastUserParameters.height != userParameters.height) {
                val db = this.writableDatabase
                db.execSQL(UserParameters.getUpdateQuery(userParameters))
                db.close()
            }
        } else {
            val db = this.writableDatabase
            db.insert(UserParameters.MODEL_NAME, null, userParameters.contentValues)
            db.close()
        }
    }

    //user parameter
    fun getUserParameterList(): ArrayList<UserParameter> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameter.selectQuery, null)
        val result = UserParameter.getUserParametersList(cursor)
        cursor.close()
        return result
    }

    fun getUserParameter(name: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameter.getParameterQuery(name), null)
        val result = if (cursor!!.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(UserParameter.VALUE_COLUMN_NAME))
        } else {
            null
        }
        cursor.close()
        return result
    }

    fun updateUserParameter(userParameter: UserParameter) {
        val db = this.readableDatabase
        db.execSQL(UserParameter.getUpdateQuery(userParameter))
    }

    //plan
    fun getPlansByDate(date: String): ArrayList<Plan> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(Plan.getSelectByDateQuery(date), null)
        val result = Plan.getPlanList(cursor)
        cursor.close()
        return result
    }

    fun getPlansList(): ArrayList<Plan> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(Plan.selectQuery, null)
        val result = Plan.getPlanList(cursor)
        cursor.close()
        return result
    }

    fun insertPlan(plan: Plan) {
        val db = this.readableDatabase
        db.execSQL(Plan.getInsertQuery(plan))
    }

    //medicaments
    fun getMedicaments(): List<Medicament> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(Medicament.selectQuery, null)
        return Medicament.getList(cursor)
    }

    fun saveMedicament(medicament: Medicament) {
        val db = this.writableDatabase
        db.execSQL(Medicament.getUpdateQuery(medicament))
        db.close()
    }


    companion object {
        private const val DATABASE_VERSION = 5
        private const val DATABASE_NAME = "healthDiary.db"
    }
}