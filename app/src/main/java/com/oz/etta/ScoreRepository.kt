package com.oz.etta

import android.content.Context
import java.io.*

class ScoreRepository {
    val scoreFileName = "scoreMap.txt"

    fun writeToFile(
        userScoreMap: Map<String, Integer>,
        applicationContext: Context
    ) {
        val file = getFile(scoreFileName, applicationContext)
        val fileOutputStream = FileOutputStream(file)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)

        objectOutputStream.writeObject(userScoreMap)
        objectOutputStream.close()
    }

    fun readFromFile(applicationContext: Context): Map<String, Integer> {

        val file = getFile(scoreFileName, applicationContext)
        if(!file.exists()){
            return emptyMap()
        }

        val fileInputStream = FileInputStream(file)
        val objectInputStream = ObjectInputStream(fileInputStream)

        val scoreMap = objectInputStream.readObject() as Map<String, Integer>
        objectInputStream.close()
        return scoreMap
    }

    private fun getFile(fileName: String, applicationContext: Context) =
        File(applicationContext.filesDir, fileName)

}
