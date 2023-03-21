package net.artsy.mimicry

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.artsy.mimicry.data.MetaphysicsController
import net.artsy.mimicry.data.MimicryClient
import net.artsy.mimicry.data.models.User

class MainActivityViewModel : ViewModel() {
	private var mimicryClient = MimicryClient
	private var user: User? = null

	fun handleClick(context: Context, env: String, accessToken: String, persist: Boolean) {
		if (persist) {
			val sharedPrefs = context.getSharedPreferences("mimicry", Context.MODE_PRIVATE)
			with(sharedPrefs.edit()) {
				putString(env, accessToken)
			}
		}
		
		viewModelScope.launch {
			val mpController =
				MetaphysicsController(mimicryClient.get())
			user = mpController.requestUserData(accessToken)
		}
	}

	fun handleDestroy() {
		mimicryClient.get().close()
	}

	fun handleEnvironmentSelect(context: Context, env: String): String? {
		val sharedPrefs = context.getSharedPreferences("mimicry", Context.MODE_PRIVATE) ?: return null
		return sharedPrefs.getString(env, null)
	}
}
