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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RestApplicationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun url(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    @Test
    fun getList() {

        val cookie = getCookieForUser("bob", "Bob123", url("/login"))
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        val response = restTemplate
            .exchange(
                url("/api/list"), HttpMethod.GET, HttpEntity<String>(headers),
                object : ParameterizedTypeReference<List<Address>>() {}
            )
        val address = response.getBody()

        assertNotNull(response)
        assertEquals(200, response.statusCodeValue)
        assertTrue(address!!.size == 4)
    }


    private fun getCookieForUser(username: String, password: String, loginUrl: String): String? {
        val form: MultiValueMap<String, String> = LinkedMultiValueMap()
        form.set("username", username);
        form.set("password", password);
        val loginResponse = restTemplate.postForEntity(
            loginUrl,
            HttpEntity(form, HttpHeaders()), String::class.java
        )
        var jSessionId: String? = null
        for (cookie in loginResponse.headers.get("Set-Cookie")!!)
            if (cookie.startsWith("JSESSIONID"))
                jSessionId = cookie

            return jSessionId
    }


	@Test
	fun getViewId() {

		val cookie = getCookieForUser("bob", "Bob123", url("/login"))
		val headers = HttpHeaders()
		headers.add("Cookie", cookie)

		val resp = restTemplate.exchange<Address>(
			url("/api/1/view"), HttpMethod.GET,  HttpEntity<Address>(headers),
			Address::class.java

		)

		val address = resp.body!!
		assertNotNull(resp)
		assertEquals(200, resp.statusCodeValue)
		assertEquals("Petrov Ivan", address.fullName)
		assertEquals("Nikitina,  14", address.address)
	}

    @Test
    fun testAddAndOneRecordView() {

        val cookie = getCookieForUser("bob", "Bob123", url("/login"))
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        var resp = restTemplate.exchange<Address>(
            url("/api/add"), HttpMethod.PUT,
            HttpEntity<Address>(Address("Oscar", "Tisovaya st."), headers),
            Address::class.java
        )

        assertNotNull(resp)
        assertEquals(200, resp.statusCodeValue)

        resp = restTemplate.exchange<Address>(
            url("/api/{id}/view"), HttpMethod.GET, HttpEntity<Address>(headers),
            Address::class.java, 4
        )

        val address = resp.body!!
        assertNotNull(resp)
        assertEquals(200, resp.statusCodeValue)
        assertEquals("Oscar", address.fullName)
        assertEquals("Tisovaya st.", address.address)
    }

}

