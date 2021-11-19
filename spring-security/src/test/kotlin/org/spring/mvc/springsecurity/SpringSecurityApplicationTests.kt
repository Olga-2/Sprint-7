package org.spring.mvc.springsecurity

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.spring.mvc.springsecurity.models.Address
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser(username = "user", roles = ["APP"])
    fun testAddAndOneRecordView() {

        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .flashAttr("Address", Address("Oscar", "Tisovaya st."))
        )
            .andDo { println() }
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/app/list?fullName"))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/list/Oscar")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo { println() }
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Oscar")))
            .andExpect(content().string(containsString("Tisovaya")))
    }

    @Test
    @WithMockUser(username = "bob", roles = ["API"])
    fun testList() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/list")
                .param("fullName", "")
        )
            .andDo { println() }
            .andExpect(status().isOk())
            .andExpect(view().name("addressList"))
            .andExpect(content().string(containsString("Name")))
            .andExpect(content().string(containsString("Address")))
    }

    @Test
    @WithMockUser(username = "bob", roles = ["API"])
    fun testView() {

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/5/view")
            //.cookie(cookie)
        )
            .andDo { println() }
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Address not exist!")))

    }

    @Test
    @WithMockUser(username = "user", roles = ["APP"])
    fun testFind() {

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/list/Ivanov Ivan")
            //	.cookie(cookie)
        )
            .andDo { println() }
            .andExpect(status().isOk())
            .andExpect(view().name("addressFind"))
            .andExpect(content().string(containsString("Edit")))
            .andExpect(content().string(containsString("View")))
            .andExpect(content().string(containsString("Delete")))
    }
}
