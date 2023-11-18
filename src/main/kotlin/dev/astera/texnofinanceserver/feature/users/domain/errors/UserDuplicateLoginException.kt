package dev.astera.texnofinanceserver.feature.users.domain.errors

class UserDuplicateLoginException : RuntimeException("Duplicate email or phone number. Please try different input.")