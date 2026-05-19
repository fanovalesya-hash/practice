package ci.nsu.moble.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId")
    val id: Int,
    @SerialName("groupName")
    val name: String
)

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val birthDate: String, // Обычно формат "YYYY-MM-DD"
    val gender: String,
    val groupId: Int
)

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    @SerialName("roleId")
    val roleId: Int = 1, // Всегда 1, как в задании
    @SerialName("authAllowed")
    val authAllowed: Boolean = true,
    val person: PersonDto
)
@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)
@Serializable
data class LoginResponse(
    @SerialName("token")
    val token: String
)
@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String,
    val person: PersonDto? // Вложенная структура персоны
)
