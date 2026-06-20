package app.papermarks.nostr

interface Signer {
    suspend fun publicKeyHex(): String
    suspend fun sign(event: UnsignedNostrEvent): SignedNostrEvent
    suspend fun nip44Encrypt(peerPubkeyHex: String, plaintext: String): String
    suspend fun nip44Decrypt(peerPubkeyHex: String, ciphertext: String): String
}

class ReadOnlySigner(private val npubOrHex: String) : Signer {
    override suspend fun publicKeyHex(): String = npubOrHex
    override suspend fun sign(event: UnsignedNostrEvent): SignedNostrEvent {
        error("Read-only login cannot sign events")
    }
    override suspend fun nip44Encrypt(peerPubkeyHex: String, plaintext: String): String {
        error("Read-only login cannot encrypt")
    }
    override suspend fun nip44Decrypt(peerPubkeyHex: String, ciphertext: String): String {
        error("Read-only login cannot decrypt")
    }
}
