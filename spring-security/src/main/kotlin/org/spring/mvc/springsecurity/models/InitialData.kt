package org.spring.mvc.springsecurity.models

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Component
class InitialData {

    val addresses = mutableListOf(
        Address("Ivanov Ivan", "Matrosova,  45"),
        Address("Petrov Ivan", "Nikitina,  14"),
        Address("Sidorov Vladimir", "Orechovajа,  26"),
        Address("Ohlabyistin Ivan", "Petrovaka, 38")
    )

}