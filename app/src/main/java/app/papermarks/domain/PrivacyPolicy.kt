package app.papermarks.domain

/**
 * Hard product rule: camera frames are transient OCR input only.
 * Papermarks must not persist photographed pages as files, blobs, or remote media.
 */
object PrivacyPolicy {
    const val STORE_PAGE_PHOTOS: Boolean = false
    const val MAX_RELAYS: Int = 5
}
