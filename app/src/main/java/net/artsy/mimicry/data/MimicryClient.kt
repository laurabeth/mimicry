package net.artsy.mimicry.data

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object MimicryClient {
	private lateinit var client: HttpClient

	fun get(): HttpClient {
		if (this::client.isInitialized) {
			return client
		}
		client = HttpClient(Android) {
			install(ContentNegotiation) {
				json(Json {
					isLenient = true
					ignoreUnknownKeys = true
				})
			}
		}
		return client
	}
}



