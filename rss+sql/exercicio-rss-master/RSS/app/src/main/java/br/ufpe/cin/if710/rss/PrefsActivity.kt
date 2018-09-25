package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.SharedPreferences
import android.nfc.Tag
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment

class PrefsActivity: Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment)
    }


    class UserPreferenceFragment : PreferenceFragment(){
        private var mListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
        private var mLink: Preference? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.preferencias)
        }

        companion object {
            protected val TAG = "UserPresFragment"
        }
    }
}