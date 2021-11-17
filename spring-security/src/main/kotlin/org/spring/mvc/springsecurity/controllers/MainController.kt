package org.spring.mvc.springsecurity.controllers

import org.spring.mvc.springsecurity.models.Address
import org.spring.mvc.springsecurity.models.InitialData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping(value = ["/app"])
class MainController (@Autowired private val initialData: InitialData){

    private val errorMessage: String = "Name and address can't be  empty"
    @RequestMapping(value = ["/list"], method = [RequestMethod.GET, RequestMethod.POST])
    fun getAddressList(model: Model, @RequestParam fullName : String?, request: HttpServletRequest): String {
        if  (request.method.equals(RequestMethod.POST.name, true))
        if (fullName != null)
            return "redirect:/app/list/${fullName}"
        model.addAttribute("addresses", initialData.addresses)
        model.addAttribute("fullName", "")

        return "addressList"
    }

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST, RequestMethod.GET])
    fun addAddress(
        model: Model,
        @ModelAttribute("Address") address: Address,
        request: HttpServletRequest): String {

        val fio = address.fullName
        val addr = address.address
        if (fio != null && fio.length > 0
            && addr != null && addr.length > 0)
         {
            initialData.addresses.add(Address(fio, addr))
            return "redirect:/app/list?fullName"
        }
        model.addAttribute("errorMessage", errorMessage);
        return "addAddress";
    }

    @GetMapping(value = ["/{id}/view"])
    fun getAddress(model: Model, request: HttpServletRequest, @PathVariable id: Int): String {
        if (id !in initialData.addresses.indices) return "errorPage"
        val address = initialData.addresses[id]
        model.addAttribute("address", address)
        return "addressView"

    }

    @RequestMapping(value = ["/{id}/delete"], method = [RequestMethod.POST])
    fun deleteAddress(model: Model, request: HttpServletRequest, @PathVariable id: Int): String {
        if (id !in initialData.addresses.indices) return "errorPage"
        initialData.addresses.removeAt(id)
        model.addAttribute("addresses", initialData.addresses)

        return "redirect:/app/list?fullName"
    }

    @RequestMapping(value = ["/{id}/edit"], method = [RequestMethod.POST, RequestMethod.GET])
    fun editAddress(model: Model, request: HttpServletRequest,
                    @ModelAttribute("Address") address: Address,
                    @PathVariable id: Int): String {
        if (id !in initialData.addresses.indices) return "errorPage"

        if (address.fullName == null && address.address == null ) {
            model.addAttribute("address",  initialData.addresses[id])
            return "addressEdit"
        }
        val fio = address.fullName
        val addr = address.address

        if (fio != null && fio.length > 0
            && addr != null && addr.length > 0)
            initialData.addresses[id] = Address(fio, addr)
        model.addAttribute("addresses", initialData.addresses)
        return "redirect:/app/list?fullName"
    }

    @RequestMapping(value = ["/list/{fio}"],  method = [RequestMethod.GET])
    fun findAddress(model: Model, request: HttpServletRequest, @PathVariable fio: String): String {
        val address = initialData.addresses.find { it.fullName.equals(fio, true)}
        val id = if (address == null)  -1 else initialData.addresses.indexOf(address)
        if (id !in initialData.addresses.indices) return "errorPage"
        model.addAttribute("address", address)
        model.addAttribute("id", id)
        return "addressFind"
    }

    @RequestMapping(value = ["/errorPage"],  method = [RequestMethod.GET])
    fun errorPage(): String {
        return "errorPage"
    }
}