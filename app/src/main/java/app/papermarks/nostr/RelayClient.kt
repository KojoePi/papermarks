package app.papermarks.nostr

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class RelayClient(
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val json: Json = Json { encodeDefaults = true; ignoreUnknownKeys = true }
) {
    fun publish(relayUrl: String, event: SignedNostrEvent, onResult: (String) -> Unit): WebSocket {
        val request = Request.Builder().url(relayUrl).build()
        val payload = buildJsonArray {
            add(JsonPrimitive("EVENT"))
            add(json.encodeToJsonElement(SignedNostrEvent.serializer(), event))
        }.toString()
        return okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send(payload)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                onResult(text)
            }
        })
    }
}
