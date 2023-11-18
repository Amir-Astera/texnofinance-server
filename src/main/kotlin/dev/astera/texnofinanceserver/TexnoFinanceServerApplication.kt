package dev.astera.texnofinanceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication//(scanBasePackages = ["dev.astera.texnofinanceserver", "dev.astera.texnofinanceserver.feature"])
@ConfigurationPropertiesScan
@EnableTransactionManagement
@EnableWebFluxSecurity
class TexnoFinanceServerApplication

fun main(args: Array<String>) {
	runApplication<TexnoFinanceServerApplication>(*args)
}
