package app.papermarks.data.local

import androidx.room.TypeConverter
import app.papermarks.domain.model.Visibility
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonConverters {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @TypeConverter
    fun visibilityToString(value: Visibility): String = value.name

    @TypeConverter
    fun stringToVisibility(value: String): Visibility = Visibility.valueOf(value)

    @TypeConverter
    fun stringListToJson(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> = json.decodeFromString(value)
}
