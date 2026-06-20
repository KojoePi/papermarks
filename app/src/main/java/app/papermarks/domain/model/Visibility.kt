package app.papermarks.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Visibility {
    PUBLIC,
    PRIVATE
}
