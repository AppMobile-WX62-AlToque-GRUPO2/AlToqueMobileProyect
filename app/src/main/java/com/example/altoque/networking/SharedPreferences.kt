package com.example.altoque.networking
import com.google.gson.Gson
import android.content.Context
import com.example.altoque.models.VTokenData

class SharedPreferences (val context: Context) {
    private val PREFS_NAME = "sharedPreferences"

    private val sharedPreference = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    private val gson = Gson()

    // Método para guardar un objeto de tipo VTokenData
    fun saveData(keyName: String, value: VTokenData) {
        val json = gson.toJson(value)
        save(keyName, json)
    }

    // Método para recuperar un objeto de tipo VTokenData
    fun getData(keyName: String): VTokenData? {
        val json = getValue(keyName)
        return if (json != null) {
            gson.fromJson(json, VTokenData::class.java)
        } else {
            null
        }
    }

    fun save(keyName: String, value: String){
        val editor = sharedPreference.edit()
        editor.putString(keyName, value)
        editor.commit()
    }

    fun getValue(keyName: String): String?{
        return sharedPreference.getString(keyName, null)
    }

    fun clearSharedPreferences(){
        val editor = sharedPreference.edit()
        editor.clear()
        editor.commit()

    }

    fun removeValue(keyName: String){
        val editor = sharedPreference.edit()
        editor.remove(keyName)
        editor.commit()
    }
}