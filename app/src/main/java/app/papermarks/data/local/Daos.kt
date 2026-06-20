package app.papermarks.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(book: BookEntity)

    @Query("SELECT * FROM books ORDER BY updatedAt DESC")
    fun observeBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE bookId = :bookId LIMIT 1")
    suspend fun getBook(bookId: String): BookEntity?
}

@Dao
interface PageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(page: PageEntity)

    @Query("SELECT * FROM pages WHERE bookId = :bookId ORDER BY COALESCE(pageNumber, scanIndex), scanIndex")
    fun observePages(bookId: String): Flow<List<PageEntity>>

    @Query("SELECT * FROM pages WHERE pageId = :pageId LIMIT 1")
    suspend fun getPage(pageId: String): PageEntity?
}

@Dao
interface AnnotationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(annotation: AnnotationEntity)

    @Query("SELECT * FROM annotations WHERE pageId = :pageId ORDER BY startOffset ASC")
    fun observeAnnotations(pageId: String): Flow<List<AnnotationEntity>>

    @Query("SELECT * FROM annotations WHERE dirty = 1")
    suspend fun dirtyAnnotations(): List<AnnotationEntity>
}

@Dao
interface RelayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(relay: RelaySettingEntity)

    @Query("SELECT * FROM relay_settings ORDER BY createdAt ASC LIMIT 5")
    fun observeRelays(): Flow<List<RelaySettingEntity>>

    @Query("DELETE FROM relay_settings")
    suspend fun clear()
}
