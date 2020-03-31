package com.example.healthdiary.db

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserParameters.createQuery)
        db.execSQL(UserParameter.createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameters.dropQuery)
        db.execSQL(UserParameter.dropQuery)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameters.dropQuery)
        db.execSQL(UserParameter.dropQuery)
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
        if (lastUserParameters != null && lastUserParameters.date == userParameters.date){
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
        return UserParameter.getUserParametersList(cursor)
    }

    fun getUserParameter(name: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameter.getParameterQuery(name), null)
        return if (cursor!!.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(UserParameter.VALUE_COLUMN_NAME))
        } else {
            null
        }
    }

    fun updateUserParameter(userParameter: UserParameter) {
        val db = this.readableDatabase
        db.execSQL(UserParameter.getUpdateQuery(userParameter))
    }

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "healthDiary.db"
    }
}