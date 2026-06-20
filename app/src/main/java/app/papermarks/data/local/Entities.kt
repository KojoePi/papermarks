package app.papermarks.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.papermarks.domain.model.Visibility

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val bookId: String,
    val ean13: String?,
    val isbn13: String?,
    val title: String,
    val authorsJson: String,
    val language: String?,
    val visibility: Visibility,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "pages",
    foreignKeys = [ForeignKey(
        entity = BookEntity::class,
        parentColumns = ["bookId"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("bookId"), Index(value = ["bookId", "pageNumber"])]
)
data class PageEntity(
    @PrimaryKey val pageId: String,
    val bookId: String,
    val pageNumber: Int?,
    val scanIndex: Int,
    val ocrText: String,
    val ocrBlocksJson: String,
    val contentFingerprint: String,
    val visibility: Visibility,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "annotations",
    foreignKeys = [ForeignKey(
        entity = PageEntity::class,
        parentColumns = ["pageId"],
        childColumns = ["pageId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("pageId")]
)
data class AnnotationEntity(
    @PrimaryKey val annotationId: String,
    val pageId: String,
    val startOffset: Int,
    val endOffset: Int,
    val selectedText: String,
    val prefix: String?,
    val suffix: String?,
    val color: String,
    val note: String?,
    val visibility: Visibility,
    val createdAt: Long,
    val updatedAt: Long,
    val dirty: Boolean = true
)

@Entity(tableName = "relay_settings")
data class RelaySettingEntity(
    @PrimaryKey val url: String,
    val read: Boolean = true,
    val write: Boolean = true,
    val source: String = "manual",
    val createdAt: Long
)
