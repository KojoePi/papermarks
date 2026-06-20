package app.papermarks.nostr

import app.papermarks.domain.PrivacyPolicy

 data class RelayConfig(
    val url: String,
    val read: Boolean = true,
    val write: Boolean = true,
    val source: String = "manual"
)

class RelayConfigService {
    fun normalizeAndLimit(relays: List<RelayConfig>): List<RelayConfig> = relays
        .mapNotNull { relay ->
            val url = relay.url.trim()
            if (url.startsWith("wss://") || url.startsWith("ws://")) relay.copy(url = url) else null
        }
        .distinctBy { it.url.lowercase() }
        .take(PrivacyPolicy.MAX_RELAYS)

    fun fromNip65Tags(tags: List<List<String>>): List<RelayConfig> {
        val relays = tags.mapNotNull { tag ->
            if (tag.firstOrNull() != "r" || tag.size < 2) return@mapNotNull null
            val marker = tag.getOrNull(2)
            RelayConfig(
                url = tag[1],
                read = marker == null || marker == "read",
                write = marker == null || marker == "write",
                source = "nip65"
            )
        }
        return normalizeAndLimit(relays)
    }
}
