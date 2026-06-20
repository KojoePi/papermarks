package app.papermarks.nostr

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsignedNostrEvent(
    val pubkey: String,
    val created_at: Long,
    val kind: Int,
    val tags: List<List<String>>,
    val content: String
)

@Serializable
data class SignedNostrEvent(
    val id: String,
    val pubkey: String,
    val created_at: Long,
    val kind: Int,
    val tags: List<List<String>>,
    val content: String,
    val sig: String
)

@Serializable
data class NostrFilter(
    val kinds: List<Int>? = null,
    val authors: List<String>? = null,
    @SerialName("#d") val d: List<String>? = null,
    val limit: Int? = null
)
