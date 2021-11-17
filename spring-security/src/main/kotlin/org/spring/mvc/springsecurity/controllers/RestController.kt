package org.spring.mvc.springsecurity.controllers

import org.spring.mvc.springsecurity.models.*
import org.spring.mvc.springsecurity.service.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class RestController(@Autowired private val initialData: InitialData,
    @Autowired private var service: Service)
{
    @GetMapping(value = ["/list"])
    fun getAddressList(): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(service.getList())
     }

    @GetMapping(value = ["/list/{fio}"])
    fun getFindAddressByFio(@PathVariable fio : String): ResponseEntity<Any> {
        var address = service.getAddressByFio(fio)
        if (address != null)
            return ResponseEntity.status(HttpStatus.OK).body(service.getAddressByFio(fio))
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message("Not found"))
    }

    @PutMapping(value = ["/add"])
    fun addAddress(@RequestBody address: Address): ResponseEntity<Any> {
        try {
            service.addAddress(address)
            return ResponseEntity.status(HttpStatus.OK).body(Message("Address added"))
        }
        catch (e: Exception)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message("Error add address"))
        }
    }
    @GetMapping(value = ["/{id}/view"])
    fun getAddress(@PathVariable id: Int): ResponseEntity<Any> {

        try {
            var address = service.getAddressById(id)
            if (address != null)
                return ResponseEntity.status(HttpStatus.OK).body(address)
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message("Not found"))
        }
        catch (e: IndexOutOfBoundsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message("Error addresses id"))
        }
    }
    @DeleteMapping(value = ["/{id}/delete"])
    @Secured("APP")
    fun deleteAddress(@PathVariable id: Int): ResponseEntity<Any> {
        try {
            service.deleteAddress(id)
            return ResponseEntity.status(HttpStatus.OK).body(Message("Address removed"))
        } catch (e: IndexOutOfBoundsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Message("Error addresses id"))
        }
    }

    @PutMapping(value = ["/{id}/edit"])
    fun editAddress(@PathVariable id: Int, @RequestBody address: Address): ResponseEntity<Any> {
        var address = service.editAddress(id, address)
        try {
            if (address != null)
                return ResponseEntity.status(HttpStatus.OK).body(service.getAddressById(id))
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message("Not found"))
        } catch (e: IndexOutOfBoundsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message("Error addresses id"))
        }
    }

}