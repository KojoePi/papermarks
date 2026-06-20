package app.papermarks.nostr

/**
 * Architecture placeholder for NIP-46 / Nostr Connect.
 * A production implementation should speak to a configured bunker:// or nostrconnect:// signer.
 */
class Nip46RemoteSigner(
    private val connectionUri: String,
    private val appPubkey: String
) : Signer {
    override suspend fun publicKeyHex(): String = appPubkey

    override suspend fun sign(event: UnsignedNostrEvent): SignedNostrEvent {
        error("NIP-46 transport not wired yet for $connectionUri")
    }

    override suspend fun nip44Encrypt(peerPubkeyHex: String, plaintext: String): String {
        error("NIP-44 encryption should be delegated through NIP-46 or local signer")
    }

    override suspend fun nip44Decrypt(peerPubkeyHex: String, ciphertext: String): String {
        error("NIP-44 decryption should be delegated through NIP-46 or local signer")
    }
}
