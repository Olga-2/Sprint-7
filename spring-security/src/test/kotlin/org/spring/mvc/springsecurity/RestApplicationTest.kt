package org.spring.mvc.springsecurity

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.spring.mvc.springsecurity.models.Address
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


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

        Assertions.assertNotNull(response)
        Assertions.assertEquals(200, response.statusCodeValue)
        Assertions.assertTrue(address!!.size == 4)
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
        Assertions.assertNotNull(resp)
        Assertions.assertEquals(200, resp.statusCodeValue)
        Assertions.assertEquals("Petrov Ivan", address.fullName)
        Assertions.assertEquals("Nikitina,  14", address.address)
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

        Assertions.assertNotNull(resp)
        Assertions.assertEquals(200, resp.statusCodeValue)

        resp = restTemplate.exchange<Address>(
            url("/api/{id}/view"), HttpMethod.GET, HttpEntity<Address>(headers),
            Address::class.java, 4
        )

        val address = resp.body!!
        Assertions.assertNotNull(resp)
        Assertions.assertEquals(200, resp.statusCodeValue)
        Assertions.assertEquals("Oscar", address.fullName)
        Assertions.assertEquals("Tisovaya st.", address.address)
    }

}

