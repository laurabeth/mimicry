package net.artsy.mimicry

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.artsy.mimicry.data.MetaphysicsController
import net.artsy.mimicry.data.MimicryClient
import net.artsy.mimicry.data.MimicryPrefs
import net.artsy.mimicry.data.models.MetaphysicsData

class MainActivityViewModel : ViewModel() {
	private val mimicryClient = MimicryClient
	private var data: MetaphysicsData? = null
	private var environmentIndex: Int = 0
	private var persist: Boolean = false
	private var token: String? = null

	fun handleFetch(context: Context) {
		if (token != null) {
			viewModelScope.launch {
				val mpController =
					MetaphysicsController(context, mimicryClient.get())
				data = mpController.requestUserData(environmentIndex, token!!)
				println(data)
			}
		} else {
			Log.e("Fetch Error", "Token cannot be left blank")
		}
	}

	fun handleClear() {}
	fun handleEnvironmentSelect(context: Context, position: Int) {
		environmentIndex = position
		with(MimicryPrefs.get(context).edit()) {
			putInt(context.getString(R.string.key_environment_index), position)
			apply()
		}
	}

	fun handlePersist(context: Context, isChecked: Boolean, accessToken: String?) {
		Log.d("MainActivityViewModel/AccessToken", accessToken.toString())
		persist = isChecked
		token = accessToken
		val tokenKey = context.resources.getStringArray(R.array.environments)[environmentIndex]
		with(MimicryPrefs.get(context).edit()) {
			putBoolean(context.getString(R.string.key_persist_token), isChecked)
			if (isChecked) putString(tokenKey, token) else remove(tokenKey)
			apply()
		}
	}

	fun handleRestore(context: Context): Preferences {
		val sharedPrefs = MimicryPrefs.get(context)
		persist = sharedPrefs.getBoolean(context.resources.getString(R.string.key_persist_token), false)
		environmentIndex =
			sharedPrefs.getInt(context.resources.getString(R.string.key_environment_index), 0)
		token = sharedPrefs.getString(
			context.resources.getStringArray(R.array.environments)[environmentIndex],
			null
		)

		return Preferences(environmentIndex, persist, token)
	}

	fun handleDestroy() {
		mimicryClient.get().close()
	}
}

class Preferences(
	val environmentIndex: Int,
	val persist: Boolean,
	val accessToken: String?
) {}
