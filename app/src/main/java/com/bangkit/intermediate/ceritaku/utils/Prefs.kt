package com.bangkit.intermediate.ceritaku.utils

import android.content.Context
import android.content.SharedPreferences
import com.bangkit.intermediate.ceritaku.source.response.LoginResult

object Prefs {
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private const val PREFERENCE_NAME = "ceritaku.pref"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    //Login Result
    private const val KEY_ID = "key_id"
    private const val KEY_NAME = "key_name"
    private const val KEY_TOKEN = "key_token"

    val getName: String
        get() = prefs.getString(KEY_NAME, "") ?: ""
    val getToken: String
        get() = prefs.getString(KEY_TOKEN, "") ?: ""
    val getUserId: String
        get() = prefs.getString(KEY_ID, "") ?: ""


    fun setLoginPrefs(loginResult: LoginResult) {
        editor.putString(KEY_NAME, loginResult.name)
        editor.putString(KEY_TOKEN, loginResult.token)
        editor.putString(KEY_ID, loginResult.userId)
        editor.apply()
    }

    fun setUserPref(userItem: LoginResult) {
        userItem.apply {
            editor.putString(KEY_NAME, name)
            editor.putString(KEY_ID, userId)
            editor.putString(KEY_TOKEN, token)
            editor.apply()
        }
    }
    fun clearAuthPrefs() {
        editor.remove(KEY_NAME)
        editor.remove(KEY_TOKEN)
        editor.remove(KEY_ID)
        editor.apply()
    }

}
