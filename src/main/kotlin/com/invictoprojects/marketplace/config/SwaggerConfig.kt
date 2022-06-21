package com.invictoprojects.marketplace.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spi.DocumentationType
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.*
import springfox.documentation.spi.service.contexts.SecurityContext

@Configuration
class SwaggerConfig {

    @Bean
    fun redditCloneApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf<SecurityScheme>(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo)
    }

    private val apiInfo: ApiInfo
        get() = ApiInfoBuilder()
            .title("Marketplace API")
            .version("1.0")
            .description("API for Marketplace Application")
            .contact(Contact("Development Team", "team", "marketplaceofficial59@gmail.com@email.com"))
            .license("Apache License Version 2.0")
            .build()

    private fun apiKey(): ApiKey {
        return ApiKey("JWT", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder().securityReferences(defaultAuth()).build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return listOf(SecurityReference("JWT", authorizationScopes))
    }
}
