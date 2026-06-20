package app.papermarks.domain.annotation

import app.papermarks.domain.model.AnnotationPayload
import app.papermarks.domain.model.AnnotationStyle
import app.papermarks.domain.model.AnnotationTarget
import java.security.MessageDigest

class TextRangeAnnotationEngine {
    fun highlight(
        pageId: String,
        ocrText: String,
        startOffset: Int,
        endOffset: Int,
        color: String = "yellow",
        note: String? = null,
        now: Long = System.currentTimeMillis() / 1000L
    ): AnnotationPayload {
        require(startOffset >= 0) { "startOffset must be non-negative" }
        require(endOffset <= ocrText.length) { "endOffset is outside OCR text" }
        require(startOffset < endOffset) { "startOffset must be smaller than endOffset" }

        val selected = ocrText.substring(startOffset, endOffset)
        val prefix = ocrText.substring(maxOf(0, startOffset - 32), startOffset)
        val suffix = ocrText.substring(endOffset, minOf(ocrText.length, endOffset + 32))
        val idSeed = "$pageId:$startOffset:$endOffset:$selected:$now"

        return AnnotationPayload(
            annotationId = "anno_${sha256(idSeed).take(24)}",
            target = AnnotationTarget(
                startOffset = startOffset,
                endOffset = endOffset,
                selectedText = selected,
                prefix = prefix,
                suffix = suffix
            ),
            style = AnnotationStyle(color = color),
            note = note,
            createdAt = now,
            updatedAt = now
        )
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
