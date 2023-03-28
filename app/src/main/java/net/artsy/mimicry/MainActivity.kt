package net.artsy.mimicry

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import net.artsy.mimicry.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
	private lateinit var accessTokenInput: EditText
	private lateinit var binding: ActivityMainBinding
	private lateinit var envSpinner: Spinner
	private lateinit var fetchButton: Button
	private lateinit var persistCheckbox: CheckBox


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		assignHandlers()
		restoreData()
	}

	override fun onDestroy() {
		super.onDestroy()

		val vm = useMainActivityViewModel()
		vm.value.handleDestroy()
	}

	private fun assignHandlers() {
		accessTokenInput = findViewById(R.id.access_token_input)
		fetchButton = findViewById(R.id.fetch_button)
		envSpinner = findViewById(R.id.environment_spinner)
		persistCheckbox = findViewById(R.id.persist_token_checkbox)

		ArrayAdapter.createFromResource(
			this,
			R.array.environments,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			envSpinner.adapter = adapter
		}
		envSpinner.onItemSelectedListener = this

		persistCheckbox.setOnCheckedChangeListener { _, isChecked ->
			useMainActivityViewModel().value.handlePersist(
				this,
				isChecked,
				accessTokenInput.text.toString()
			)
		}

		fetchButton.setOnClickListener {
			useMainActivityViewModel().value.handleFetch(
				this
			)
		}
	}

	private fun restoreData() {
		val prefs = useMainActivityViewModel().value.handleRestore(this)
		Log.d("MainActivity/AccessToken", "${prefs.accessToken}")
		persistCheckbox.isChecked = prefs.persist
		envSpinner.setSelection(prefs.environmentIndex)
		accessTokenInput.setText(prefs.accessToken)
	}

	private fun useMainActivityViewModel(): Lazy<MainActivityViewModel> {
		return viewModels()
	}

	override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
		if (parent != null) {
			useMainActivityViewModel().value.handleEnvironmentSelect(this, position)
			restoreData()
		}
	}

	override fun onNothingSelected(parent: AdapterView<*>?) {
		TODO("Not yet implemented")
	}
}

