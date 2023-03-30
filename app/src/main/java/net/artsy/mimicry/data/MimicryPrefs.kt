package net.artsy.mimicry.data;

import android.content.Context
import android.content.SharedPreferences
import net.artsy.mimicry.R

object MimicryPrefs {
	fun get(context: Context): SharedPreferences {
		return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
	}

}
