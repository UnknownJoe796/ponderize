package com.ivieleague.ponderize

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.ivieleague.ponderize.model.Volume
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Created by josep on 10/6/2015.
 */
class Database(context: Context) {
    companion object {
        const val NAME: String = "lds_scriptures.db"
    }

    var database: SQLiteDatabase = {
        val file = context.getDatabasePath(NAME)
        if (!file.exists()) {
            copyDb(context, file)
        }
        SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY)
    }()

    val volumes: List<Volume> get() {
        val cursor = database.query("lds_scriptures_volumes", null, null, null, null, null, null)
        val list: ArrayList<Volume> = ArrayList()
        cursor.moveToFirst()
        if (!cursor.isAfterLast) do {
            list.add(Volume(
                    database,
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6)
            ))
        } while (cursor.moveToNext())
        return list
    }

    private fun copyDb(context: Context, file: File) {
        //Open a stream for reading from our ready-made database
        //The stream source is located in the assets
        val externalDbStream = context.assets.open(NAME)

        //Path to the created empty database on your Android device
        file.parentFile.mkdirs()
        val outFileName = file.path

        //Now create a stream for writing the database byte by byte
        val localDbStream = FileOutputStream(outFileName)

        //Copying the database
        val buffer = ByteArray(1024);
        var bytesRead: Int;
        while (true) {
            bytesRead = externalDbStream.read(buffer)
            if (bytesRead <= 0) break
            localDbStream.write(buffer, 0, bytesRead)
        }
        //Don’t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
    }
}