package dev.astera.texnofinanceserver.feature.users.domain.services

import dev.astera.texnofinanceserver.core.extension.toEntity
import dev.astera.texnofinanceserver.core.extension.toModel
import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.feature.authority.data.entity.UserAuthorityEntity
import dev.astera.texnofinanceserver.repositories.AuthorityRepository
import dev.astera.texnofinanceserver.repositories.UserAuthorityRepository
import dev.astera.texnofinanceserver.feature.authority.domain.models.Authority
import dev.astera.texnofinanceserver.feature.partners.data.entity.UserPartnerEntity
import dev.astera.texnofinanceserver.repositories.PartnerRepository
import dev.astera.texnofinanceserver.repositories.UserPartnerRepository
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.repositories.UserRepository
import dev.astera.texnofinanceserver.feature.users.domain.errors.AdminAuthorityNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.errors.UserNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.models.UserAggregate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator

interface UserAggregateService {
    suspend fun save(user: UserAggregate)
    suspend fun get(id: String): UserAggregate
    suspend fun delete(id: String)
    suspend fun getAuthorities(authorityIds: List<String>): List<Authority>
    suspend fun getPartners(partnerIds: List<String>): List<Partner>
    suspend fun getByLogin(login: String): UserAggregate?
    suspend fun getByPhone(phone: String): UserAggregate?
    suspend fun getByEmail(email: String): UserAggregate?
    suspend fun existsWithLogin(login: String?): Boolean
    suspend fun existsWithEmail(email: String?): Boolean
    suspend fun existsWithPhone(phone: String?): Boolean
    suspend fun checkAdminPrivilegesBySession(sessionUser: SessionUser)
}

@Service
class UserAggregateServiceImpl(
    private val userRepository: UserRepository,
    private val transactionManager: ReactiveTransactionManager,
    private val userAuthorityRepository: UserAuthorityRepository,
    private val authorityRepository: AuthorityRepository,
    private val userPartnerRepository: UserPartnerRepository,
    private val partnerRepository: PartnerRepository
): UserAggregateService {

    override suspend fun save(user: UserAggregate) {
        val operator = TransactionalOperator.create(transactionManager)
        userRepository.saveAll(listOf(user.toEntity())).asFlux()
            .thenMany(
                userAuthorityRepository.saveAll(user.authorities.map {
                    val id = "${user.id}-${it.id}"
                    UserAuthorityEntity(id, user.id, it.id, it.version, it.createdAt, it.updatedAt)
                }).asFlux()
            ).thenMany(
                mono {
                    if (user.authorities.isNotEmpty()) {
                        userAuthorityRepository.deleteAllByUserIdAndAuthorityIdNotIn(
                            user.id,
                            user.authorities.map { it.id }
                        )
                    } else {
                        userAuthorityRepository.deleteAllByUserId(user.id)
                    }
                }
            ).thenMany(
                mono {
                    if (user.partners.isNotEmpty()) {
                        userPartnerRepository.deleteAllByUserIdAndPartnerIdNotIn(
                            user.id,
                            user.partners.map { it.id }
                        )
                    } else {
                        userPartnerRepository.deleteAllByUserId(user.id)
                    }
                }
            ).thenMany(
                userPartnerRepository.saveAll(user.partners.map {
                    val id = "${user.id}-${it.id}"
                    UserPartnerEntity(id, user.id, it.id, it.version, it.createdAt, it.updatedAt)
                }).asFlux()
            ).`as`(operator::transactional).asFlow().collect {}
    }

    override suspend fun getAuthorities(authorityIds: List<String>): List<Authority> {
        return authorityRepository.findAllById(authorityIds).map { authority ->
            authority.toModel() }.toList()
    }

    override suspend fun getPartners(partnerIds: List<String>): List<Partner> {
        return partnerRepository.findAllById(partnerIds).map { it.toModel() }.toList()
    }

    override suspend fun get(id: String): UserAggregate =
            userRepository.findById(id)?.run { toModel(getAuthorities(id), getPartners(id)) } ?: throw UserNotFoundException()

    override suspend fun delete(id: String) {
        val entity = userRepository.findById(id) ?: throw UserNotFoundException()
        userRepository.delete(entity)
    }

    override suspend fun getByLogin(login: String): UserAggregate? {
        return withContext(Dispatchers.IO) {
            userRepository.findByLogin(login)
                ?.run {
                    toModel(getAuthorities(id), getPartners(id))
                }
        }
    }

    override suspend fun getByPhone(phone: String): UserAggregate? {
        return withContext(Dispatchers.IO) {
            userRepository.findByPhone(phone)
                ?.run {
                    toModel(getAuthorities(id), getPartners(id))
                }
        }
    }

    override suspend fun getByEmail(email: String): UserAggregate? {
        return withContext(Dispatchers.IO) {
            userRepository.findByEmail(email)
                ?.run {
                    toModel(getAuthorities(id), getPartners(id))
                }
        }
    }

    override suspend fun existsWithLogin(login: String?): Boolean {
        return if (login != null) {
            val user = userRepository.findByLogin(login)
            return (user != null)
        } else {
            false
        }
    }

    override suspend fun existsWithEmail(email: String?): Boolean {
        return if (email != null) {
            val user = userRepository.findByEmail(email)
            return user != null
        } else {
            false
        }
    }

    override suspend fun existsWithPhone(phone: String?): Boolean {
        return if (phone != null) {
            val user = userRepository.findByPhone(phone)
            user != null
        } else {
            false
        }
    }

    override suspend fun checkAdminPrivilegesBySession(sessionUser: SessionUser) {
        val user = getByLogin(sessionUser.login) ?: throw UserNotFoundException()
        if (!user.checkAdminAuthority()) {
            throw AdminAuthorityNotFoundException()
        }
    }


    private suspend fun getAuthorities(userId: String) =
            withContext(Dispatchers.IO) {
                userAuthorityRepository.findAllByUserId(userId)
                    .run {
                        authorityRepository.findAllById(map { it.authorityId }).map { authority ->
                            authority.toModel(firstOrNull { it.authorityId == authority.id }?.version)
                        }
                    }.toList()
            }

    private suspend fun getPartners(userId: String) =
            withContext(Dispatchers.IO) {
                userPartnerRepository.findAllByUserId(userId)
                    .run {
                        partnerRepository.findAllById(map { (it.partnerId) }).map { partner ->
                            partner.toModel(firstOrNull { it.partnerId == partner.id }?.version)
                        }
                    }.toList()
            }


}