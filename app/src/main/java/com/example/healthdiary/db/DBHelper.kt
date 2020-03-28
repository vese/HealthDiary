package com.example.healthdiary.db

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserParameters.createQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameters.dropQuery)
        onCreate(db)
    }

    fun getLastUserParameters(): UserParameters {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameters.selectLastQuery, null)
        cursor!!.moveToFirst()
        return UserParameters.getUserParameters(cursor)
    }

    fun getUserParametersList(): ArrayList<UserParameters> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameters.selectQuery, null)
        return UserParameters.getUserParametersList(cursor)
    }

    fun insertUserParameter(userParameters: UserParameters) {
        val lastUserParameters = getLastUserParameters()
        if (lastUserParameters.date == userParameters.date){
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

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "healthDiary.db"
    }
}