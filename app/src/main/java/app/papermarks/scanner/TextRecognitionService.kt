package app.papermarks.scanner

import app.papermarks.domain.model.OcrBlock
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.security.MessageDigest

/**
 * Transient OCR service. It receives an InputImage, extracts text/blocks, and returns text data only.
 * It never writes the source image to disk.
 */
class TextRecognitionService {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun recognize(image: InputImage): Task<OcrPageResult> {
        val width = image.width.coerceAtLeast(1)
        val height = image.height.coerceAtLeast(1)
        return recognizer.process(image).continueWith { task ->
            val result = task.result
            var cursor = 0
            val blocks = result.textBlocks.map { block ->
                val text = block.text
                val start = result.text.indexOf(text, startIndex = cursor).takeIf { it >= 0 }
                val end = start?.plus(text.length)
                if (end != null) cursor = end
                val box = block.boundingBox
                OcrBlock(
                    text = text,
                    x = (box?.left ?: 0).toFloat() / width,
                    y = (box?.top ?: 0).toFloat() / height,
                    w = (box?.width() ?: 0).toFloat() / width,
                    h = (box?.height() ?: 0).toFloat() / height,
                    startOffset = start,
                    endOffset = end
                )
            }
            OcrPageResult(
                text = result.text,
                blocks = blocks,
                contentFingerprint = "sha256:${sha256(result.text)}"
            )
        }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

data class OcrPageResult(
    val text: String,
    val blocks: List<OcrBlock>,
    val contentFingerprint: String
)
