package dev.astera.texnofinanceserver.feature.authorization.presentation.dto

data class AuthRequestDto(
  val email: String,
  val password: String,
  val returnSecureToken: Boolean
)