package org.spring.mvc.springsecurity.service

import org.spring.mvc.springsecurity.models.Address
import org.spring.mvc.springsecurity.models.Addresses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Service {
    @Autowired
    private lateinit var addresses: Addresses

    fun getList () = addresses.getListAddresses()

    fun getAddressByFio(fio: String) = addresses.getListAddresses().find { it.fullName.equals(fio, true) }

    fun getAddressById(id: Int) = addresses.getListAddresses().get(id)

    fun addAddress(address: Address) = addresses.addAddress(address)

    fun deleteAddress(id: Int) = addresses.removeAddress(id)

    fun editAddress(id: Int, address: Address) = addresses.getListAddresses().set(id, address)


}