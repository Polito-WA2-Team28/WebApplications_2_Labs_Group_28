package com.lab3.server

import com.lab3.server.model.Customer
import com.lab3.server.model.Expert
import com.lab3.server.model.Product
import com.lab3.server.repository.CustomerRepository
import com.lab3.server.repository.ProductRepository
import com.lab3.ticketing.model.Ticket
import com.lab3.ticketing.repository.TicketRepository
import com.lab3.ticketing.util.ExpertiseFieldEnum
import com.lab3.ticketing.util.TicketState
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.*
import java.util.Date


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

	@BeforeEach
	fun setup() {
		restTemplate.restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()
	}

	@Autowired
	lateinit var ticketRepository: TicketRepository
	@Autowired
	lateinit var customerRepository: CustomerRepository
	@Autowired
	lateinit var productRepository: ProductRepository

	//	1) /api/customers/:customerId/tickets GET

	@Test
	fun successAPI1(){
		ticketRepository.deleteAll()
		//customerRepository.deleteAll()
		//productRepository.deleteAll()

		val customer = Customer("Mario", "Rossi", Date(2020,1,1),  Date(1990,1,1),"mario.rossi@mail.com","0123456789")
		customerRepository.save(customer)

		val expert= Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))


		val product = Product("Iphone", "15", 1234, customer)
		productRepository.save(product)

		val ticket = Ticket(TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(), Date(2020,1,1), Date(2020,1,1))
		ticketRepository.save(ticket)
		val response = restTemplate.getForEntity("/API/customers/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
	}

	//	2) /api/customers/:customerId/ticket POST
	@Test
	fun successAPI2(){

	}

	//	3) /api/experts/:expertId/tickets
	@Test
	fun successAPI3(){
		val response = restTemplate.getForEntity("/API/experts/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
	}

	//	4) /api/managers/:managerId/tickets
	@Test
	fun successAPI4(){
		val response = restTemplate.getForEntity("/API/managers/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
	}

	//	5) /api/customers/:customerId/tickets/:ticketId
	@Test
	fun successAPI5(){
		val response = restTemplate.getForEntity("/API/customers/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
	}

	//	6) /api/experts/:expertId/tickets/:ticketId
	@Test
	fun successAPI6(){
		val response = restTemplate.getForEntity("/API/experts/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
	}

	//	7) /api/managers/:managerId/tickets/:ticketId
	@Test
	fun successAPI7(){
		val response = restTemplate.getForEntity("/API/managers/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
	}

	//	8) /api/managers/:managerId/tickets/:ticketId/assign
	@Test
	fun successAPI8(){

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		val requestObject= JSONObject()
		requestObject.put("expertId", 1)
		println("requestObject $requestObject")

		val response = restTemplate.patchForObject("/API/managers/1/tickets/1/assign", requestObject.toString() ,String::class.java)
		//Assertions.assertNotNull(response)
		//Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		println("ABECEDARIO $response")
	}
}