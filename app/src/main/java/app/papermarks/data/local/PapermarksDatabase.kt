package app.papermarks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        BookEntity::class,
        PageEntity::class,
        AnnotationEntity::class,
        RelaySettingEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(JsonConverters::class)
abstract class PapermarksDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun pageDao(): PageDao
    abstract fun annotationDao(): AnnotationDao
    abstract fun relayDao(): RelayDao
}
