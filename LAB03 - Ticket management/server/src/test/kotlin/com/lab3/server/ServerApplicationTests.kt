package com.lab3.server

import com.lab3.ticketing.repository.TicketRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.*


@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class DbT1ApplicationTests{
	companion object {
		@Container
		val postgres = PostgreSQLContainer("postgres:latest")
		@JvmStatic
		@DynamicPropertySource
		fun properties(registry: DynamicPropertyRegistry) {
			registry.add("spring.datasource.url", postgres::getJdbcUrl)
			registry.add("spring.datasource.username", postgres::getUsername)
			registry.add("spring.datasource.password", postgres::getPassword)
			registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
		}
	}
	@LocalServerPort
	protected var port: Int = 8080
	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Autowired
	lateinit var ticketRepository: TicketRepository

	//	1) /api/customers/:customerId/tickets GET

	@Test
	fun successAPI1(){
		val response = restTemplate.getForEntity("/api/customers/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		Assertions.assertEquals(1,1)
	}

	//	2) /api/customers/:customerId/ticket POST
	@Test
	fun successAPI2(){

	}

	//	3) /api/experts/:expertId/tickets
	@Test
	fun successAPI3(){
		val response = restTemplate.getForEntity("/api/experts/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		Assertions.assertEquals(1,1)
	}

	//	4) /api/managers/:managerId/tickets
	@Test
	fun successAPI4(){
		val response = restTemplate.getForEntity("/api/managers/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		Assertions.assertEquals(1,1)
	}

	//	5) /api/customers/:customerId/tickets/:ticketId
	@Test
	fun successAPI5(){
		val response = restTemplate.getForEntity("/api/customers/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		Assertions.assertEquals(1,1)
	}

	//	6) /api/experts/:expertId/tickets/:ticketId
	@Test
	fun successAPI6(){
		val response = restTemplate.getForEntity("/api/experts/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		Assertions.assertEquals(1,1)
	}

	//	7) /api/managers/:managerId/tickets/:ticketId
	@Test
	fun successAPI7(){
		val response = restTemplate.getForEntity("/api/managers/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		Assertions.assertEquals(1,1)
	}

	//	8) /api/managers/:managerId/tickets/:ticketId/assign
	@Test
	fun successAPI8(){
		val response = restTemplate.getForEntity("/api/managers/1/tickets/1/assign", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		Assertions.assertEquals(1,1)
	}
}