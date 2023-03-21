package net.artsy.mimicry.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import net.artsy.mimicry.data.models.MetaphysicsResponse
import net.artsy.mimicry.data.models.User

const val MP_LOCAL_URL = "https://c24d-24-88-45-60.ngrok.io/v2"
const val MP_STAGE_URL = ""
const val MP_PROD_URL = ""

class MetaphysicsController(private val client: HttpClient) {
	suspend fun requestUserData(accessToken: String): User? {
		val query = """
      query {
        me {
          name
          email
        }
			}
  """.trimIndent()

		val response = client.post(MP_LOCAL_URL) {
			headers {
				append(
					"X-Access-Token",
					accessToken
				)
				append("Content-Type", "application/json")
			}
			contentType(ContentType.Application.Json)
			setBody(buildJsonObject {
				put("query", query)
			}.toString())
		}

		if (response.status.value == 200) {
			println("Success")
			val responseBody = response.body<MetaphysicsResponse>()
			return responseBody.data.me
		} else {
			println("Failure")
			println(response.bodyAsText())
			return null
		}
	}
}
