package app.papermarks.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BookPayload(
    val schema: String = "papermarks.book.v1",
    val bookId: String,
    val ean13: String? = null,
    val isbn13: String? = null,
    val title: String,
    val authors: List<String>,
    val language: String? = null,
    val visibility: Visibility = Visibility.PRIVATE,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class BookRef(
    val bookId: String,
    val ean13: String? = null,
    val isbn13: String? = null,
    val title: String,
    val authors: List<String>
)

@Serializable
data class OcrBlock(
    val text: String,
    val x: Float,
    val y: Float,
    val w: Float,
    val h: Float,
    val startOffset: Int? = null,
    val endOffset: Int? = null
)

@Serializable
data class PagePayload(
    val pageId: String,
    val pageNumber: Int?,
    val scanIndex: Int,
    val ocrText: String,
    val ocrBlocks: List<OcrBlock>,
    val contentFingerprint: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class AnnotationTarget(
    val kind: String = "textRange",
    val startOffset: Int,
    val endOffset: Int,
    val selectedText: String,
    val prefix: String? = null,
    val suffix: String? = null
)

@Serializable
data class AnnotationStyle(
    val color: String = "yellow"
)

@Serializable
data class AnnotationPayload(
    val annotationId: String,
    val type: String = "highlight",
    val target: AnnotationTarget,
    val style: AnnotationStyle = AnnotationStyle(),
    val note: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class PageTextPayload(
    val schema: String = "papermarks.page.v1",
    val book: BookRef,
    val page: PagePayload,
    val annotations: List<AnnotationPayload>,
    val visibility: Visibility = Visibility.PRIVATE
)
