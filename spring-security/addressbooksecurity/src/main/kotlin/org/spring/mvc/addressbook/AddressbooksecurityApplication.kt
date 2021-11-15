package org.spring.mvc.addressbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration
open class AddressbooksecurityApplication

fun main(args: Array<String>) {
	runApplication<AddressbooksecurityApplication>(*args)
}
