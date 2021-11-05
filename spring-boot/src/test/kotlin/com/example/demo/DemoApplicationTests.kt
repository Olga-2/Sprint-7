package com.example.demo

import com.example.demo.controller.Controller
import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(Controller::class)
class DemoApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc
	@Test
	fun `shoud greeting get status OK`() {
		val result = mockMvc.perform (MockMvcRequestBuilders.get("/"))
		result
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.content().string("Hello, my frend!"))
	}
}

@DataJpaTest
class EntityRepositoryTest {
	@Autowired
	private lateinit var repository: EntityRepository
	@Test
	fun `find entitybyid`() {
		//given
		val savedEntity = repository.save(Entity(name = "Ivan"))
		//when
		val foundEntity = repository.findById(savedEntity.id!!)
		//then
		Assertions.assertTrue(savedEntity == foundEntity.get())
	}
}