package net.artsy.mimicry

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import net.artsy.mimicry.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
	private val vm: MainActivityViewModel by viewModels()

	private lateinit var accessTokenInput: EditText
	private lateinit var binding: ActivityMainBinding
	private lateinit var envSpinner: Spinner
	private lateinit var fetchButton: Button
	private lateinit var persistCheckbox: CheckBox
	private lateinit var urlTextView: TextView
	private lateinit var dataTextView: TextView


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		assignUiElements()
		createObservers()
		restoreData()
		assignHandlers()
	}

	override fun onDestroy() {
		super.onDestroy()

		vm.handleDestroy()
	}

	private fun createObservers() {
//		val dataObserver = Observer<MetaphysicsData?> { newData ->
//			dataTextView.setText(newData.toString())
//		}
		val artworksObserver = Observer<String> { newArtwork ->
			dataTextView.setText(newArtwork.toString())
		}

		vm.artworks.observe(this, artworksObserver)
	}

	private fun assignUiElements() {
		dataTextView = findViewById(R.id.data_display_text)
		envSpinner = findViewById(R.id.environment_spinner)
		ArrayAdapter.createFromResource(
			this,
			R.array.environments,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			envSpinner.adapter = adapter
		}
		urlTextView = findViewById(R.id.url_display_text)
		accessTokenInput = findViewById(R.id.access_token_input)
		fetchButton = findViewById(R.id.fetch_button)
		persistCheckbox = findViewById(R.id.persist_token_checkbox)
	}

	private fun assignHandlers() {
		persistCheckbox.setOnCheckedChangeListener { _, isChecked ->
			vm.handlePersist(
				this,
				isChecked,
				accessTokenInput.text.toString()
			)
		}

		fetchButton.setOnClickListener {
			vm.handleFetch()
		}

		envSpinner.onItemSelectedListener = this
	}

	private fun restoreData() {
		val prefs = vm.handleRestore(this)

		envSpinner.setSelection(prefs.environmentIndex)
		urlTextView.setText(prefs.url)
		persistCheckbox.isChecked = prefs.persist
		accessTokenInput.setText(prefs.accessToken)
	}

	override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
		if (parent != null) {
			vm.handleEnvironmentSelect(this, position)
			restoreData()
		}
	}

	override fun onNothingSelected(parent: AdapterView<*>?) {
		TODO("Not yet implemented")
	}
}

