package net.artsy.mimicry

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import net.artsy.mimicry.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
	private lateinit var binding: ActivityMainBinding
	private lateinit var fetchButton: Button
	private lateinit var accessTokenInput: EditText
	private lateinit var envSpinner: Spinner

	override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
		if (parent != null) {
			println(parent.getItemAtPosition(position))
		}
	}

	override fun onNothingSelected(parent: AdapterView<*>?) {
		TODO("Not yet implemented")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		accessTokenInput = findViewById(R.id.access_token_input)
		fetchButton = findViewById(R.id.fetch_button)
		envSpinner = findViewById(R.id.environment_spinner)

		ArrayAdapter.createFromResource(
			this,
			R.array.environments,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			envSpinner.adapter = adapter
		}
		envSpinner.onItemSelectedListener = this

		val vm = viewModels<MainActivityViewModel>()

		fetchButton.setOnClickListener {
			vm.value.handleClick(accessTokenInput.text.toString())
		}
	}

	override fun onDestroy() {
		super.onDestroy()

		val vm = viewModels<MainActivityViewModel>()
		vm.value.handleDestroy()
	}
}

