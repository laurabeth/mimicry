package net.artsy.mimicry.data

import android.content.Context
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import net.artsy.mimicry.R
import net.artsy.mimicry.data.models.MetaphysicsData
import net.artsy.mimicry.data.models.MetaphysicsResponse

class MetaphysicsController(private val context: Context, private val client: HttpClient) {
	suspend fun requestUserData(environmentIndex: Int, accessToken: String): MetaphysicsData? {
		val query = """
      query {
        me {
          name
          email
        }
				
				artworksForUser(first: 10, includeBackfill: true) {
          edges {
            node {
              title
              id
            }
          }
        }
			}
  """.trimIndent()

		val response =
			client.post(context.resources.getStringArray(R.array.metaphysics_endpoints)[environmentIndex]) {
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
		} else {
			Log.e("MetaphysicsController/Failure", response.bodyAsText())
			return null
		}
		return response.body<MetaphysicsResponse>().data
	}
}
