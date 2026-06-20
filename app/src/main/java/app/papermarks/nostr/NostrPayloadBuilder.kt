package app.papermarks.nostr

import app.papermarks.domain.model.BookPayload
import app.papermarks.domain.model.PageTextPayload
import app.papermarks.domain.model.Visibility
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NostrPayloadBuilder(
    private val json: Json = Json { encodeDefaults = true; ignoreUnknownKeys = true }
) {
    suspend fun bookEvent(book: BookPayload, signer: Signer): UnsignedNostrEvent {
        val pubkey = signer.publicKeyHex()
        val content = json.encodeToString(book)
        return UnsignedNostrEvent(
            pubkey = pubkey,
            created_at = book.updatedAt,
            kind = NostrKinds.BOOK,
            tags = listOf(
                listOf("d", book.bookId),
                listOf("client", "Papermarks"),
                listOf("visibility", book.visibility.name.lowercase())
            ),
            content = content
        )
    }

    suspend fun pageEvent(page: PageTextPayload, signer: Signer): UnsignedNostrEvent {
        val pubkey = signer.publicKeyHex()
        val plaintext = json.encodeToString(page)
        val content = when (page.visibility) {
            Visibility.PUBLIC -> plaintext
            Visibility.PRIVATE -> signer.nip44Encrypt(pubkey, plaintext)
        }
        return UnsignedNostrEvent(
            pubkey = pubkey,
            created_at = page.page.updatedAt,
            kind = NostrKinds.PAGE,
            tags = listOf(
                listOf("d", page.page.pageId),
                listOf("book", page.book.bookId),
                listOf("page", page.page.pageNumber?.toString().orEmpty()),
                listOf("client", "Papermarks"),
                listOf("visibility", page.visibility.name.lowercase())
            ),
            content = content
        )
    }
}
