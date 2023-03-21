package net.artsy.mimicry.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

const val BASE_URL = "www.artsy.net"
const val LOGIN_ROUTE = "/login"

class AuthController {
	suspend fun login(email: String, password: String): HttpResponse {
		val body = buildJsonObject {
			put("email", email)
			put("password", password)
			put("otpRequired", false)
		}

		val response = HttpClient(Android).use { client ->
			client.request {
				url {
					protocol = URLProtocol.HTTPS
					host = BASE_URL
					path(LOGIN_ROUTE)
				}
				headers {
					append(HttpHeaders.Accept, "application/json")
					append(HttpHeaders.ContentType, "application/json")
					append("X-Requested-With", "XMLHttpRequest")
				}
				method = HttpMethod.Post
				setBody(body)
			}
		}

		println(response)
		return response.body()
	}
}
