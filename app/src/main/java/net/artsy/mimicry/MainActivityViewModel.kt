package net.artsy.mimicry

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.artsy.mimicry.data.MetaphysicsController
import net.artsy.mimicry.data.MimicryClient
import net.artsy.mimicry.data.MimicryPrefs
import net.artsy.mimicry.data.models.MetaphysicsData

class MainActivityViewModel : ViewModel() {
	private val mimicryClient = MimicryClient
	val data: MutableLiveData<MetaphysicsData?> by lazy {
		MutableLiveData<MetaphysicsData?>()
	}
	private var environmentIndex: Int = 0
	private var persist: Boolean = false
	private var token: String? = null
	private var url: String? = null

	fun handleFetch() {
		if (token != null && url != null) {
			viewModelScope.launch {
				val mpController =
					MetaphysicsController(url!!, mimicryClient.get())
				data.setValue(mpController.requestUserData(token!!))
			}
		} else {
			Log.e("Fetch Error", "Token cannot be left blank")
		}
	}

	fun handleEnvironmentSelect(context: Context, position: Int) {
		environmentIndex = position
		with(MimicryPrefs.get(context).edit()) {
			putInt(context.getString(R.string.key_environment_index), position)
			apply()
		}
	}

	fun handlePersist(context: Context, isChecked: Boolean, accessToken: String?) {
		persist = isChecked
		token = accessToken
		val tokenKey = context.resources.getStringArray(R.array.environments)[environmentIndex]
		val sharedPrefs = MimicryPrefs.get(context)
		with(sharedPrefs.edit()) {
			putBoolean("${context.getString(R.string.key_persist_token)}_${tokenKey}", isChecked)
			apply()
		}
		with(sharedPrefs.edit()) {
			putString(tokenKey, token)
			apply()
		}
	}

	fun handleRestore(context: Context): Preferences {
		val sharedPrefs = MimicryPrefs.get(context)
		environmentIndex =
			sharedPrefs.getInt(context.resources.getString(R.string.key_environment_index), 0)
		url = context.resources.getStringArray(R.array.metaphysics_endpoints)[environmentIndex]
		val tokenKey = context.resources.getStringArray(R.array.environments)[environmentIndex]
		persist = sharedPrefs.getBoolean(
			"${context.resources.getString(R.string.key_persist_token)}_${tokenKey}",
			false
		)
		token = sharedPrefs.getString(
			tokenKey,
			null
		)
		return Preferences(environmentIndex, url!!, persist, token)
	}

	fun handleDestroy() {
		mimicryClient.get().close()
	}
}

class Preferences(
	val environmentIndex: Int,
	val url: String,
	val persist: Boolean,
	val accessToken: String?
) {}
