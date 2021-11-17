package org.spring.mvc.springsecurity

import org.spring.mvc.config.SpringSecurityConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

//@SpringBootApplication()
@SpringBootApplication()

class SpringSecurityApplication

fun main(args: Array<String>) {
	runApplication<SpringSecurityApplication>(*args)
}

