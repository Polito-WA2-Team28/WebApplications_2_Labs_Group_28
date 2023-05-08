package com.lab3.server

import com.lab3.server.model.Customer
import com.lab3.server.model.Expert
import com.lab3.server.model.Product
import com.lab3.server.repository.CustomerRepository
import com.lab3.server.repository.ExpertRepository
import com.lab3.server.repository.ProductRepository
import com.lab3.ticketing.model.Ticket
import com.lab3.ticketing.repository.TicketRepository
import com.lab3.ticketing.util.ExpertiseFieldEnum
import com.lab3.ticketing.util.TicketState
import org.json.JSONObject
import org.junit.BeforeClass
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

	@BeforeClass
	fun setup() {
		restTemplate.restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()
	}



	@Autowired
	lateinit var ticketRepository: TicketRepository
	@Autowired
	lateinit var customerRepository: CustomerRepository
	@Autowired
	lateinit var productRepository: ProductRepository
	@Autowired
	lateinit var expertRepository: ExpertRepository

	@AfterEach
	fun repositoryClean(){
		ticketRepository.deleteAll()
		customerRepository.deleteAll()
		productRepository.deleteAll()
		expertRepository.deleteAll()
		println("Clean")
	}

	//	1) /api/customers/:customerId/tickets GET

	@Test
	fun successGetAllTicketsOfACustomer(){
		val customer = Customer("Mario", "Rossi", Date(2020,1,1),  Date(1990,1,1),"mario.rossi@mail.com","0123456789")
		customerRepository.save(customer)

		val expert= Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
		expertRepository.save(expert)

		val product = Product("Iphone", "15", 1234, customer)
		productRepository.save(product)

		val ticket = Ticket(TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(), Date(2020,1,1), Date(2020,1,1))
		ticketRepository.save(ticket)
		val response = restTemplate.getForEntity("/api/customers/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		val body = response.body
		val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
		println("resTicket $resTicket")
		Assertions.assertEquals(resTicket.getString("ticketState"), "OPEN")
		Assertions.assertEquals(resTicket.getInt("serialNumber"), 1234)
		Assertions.assertEquals(resTicket.getInt("expertId"), 1)
		Assertions.assertEquals(resTicket.getInt("expertId"),1)
		Assertions.assertEquals(resTicket.getInt("customerId"),1)
		Assertions.assertEquals(resTicket.getString("description"), "Description")
		Assertions.assertEquals(resTicket.getString("lastModified"), "2020-01-01")
		Assertions.assertEquals(resTicket.getString("creationDate"), "2020-01-01")
		Assertions.assertEquals(resTicket.getInt("ticketId"),1)
	}

	//	2) /api/customers/:customerId/ticket POST
	@Test
	fun successapi2(){

	}

	//	3) /api/experts/:expertId/tickets
	@Test
	fun successGetAllTicketsOfAnExpert(){
		val customer = Customer("Mario", "Rossi", Date(2020,1,1),  Date(1990,1,1),"mario.rossi@mail.com","0123456789")
		customerRepository.save(customer)

		val expert= Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
		expertRepository.save(expert)

		val product = Product("Iphone", "15", 1234, customer)
		productRepository.save(product)

		val ticket = Ticket(TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(), Date(2020,1,1), Date(2020,1,1))
		ticketRepository.save(ticket)
		val response = restTemplate.getForEntity("/api/experts/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		val body = response.body
		val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
		println("resTicket $resTicket")
		Assertions.assertEquals(resTicket.getString("ticketState"), "OPEN")
		Assertions.assertEquals(resTicket.getInt("serialNumber"), 1234)
		Assertions.assertEquals(resTicket.getInt("expertId"), 1)
		Assertions.assertEquals(resTicket.getInt("expertId"),1)
		Assertions.assertEquals(resTicket.getInt("customerId"),1)
		Assertions.assertEquals(resTicket.getString("description"), "Description")
		Assertions.assertEquals(resTicket.getString("lastModified"), "2020-01-01")
		Assertions.assertEquals(resTicket.getString("creationDate"), "2020-01-01")
		Assertions.assertEquals(resTicket.getInt("ticketId"),1)
	}

	//	4) /api/managers/:managerId/tickets
	@Test
	fun successGetAllTicketsOfAManager(){
		val customer = Customer("Mario", "Rossi", Date(2020,1,1),  Date(1990,1,1),"mario.rossi@mail.com","0123456789")
		customerRepository.save(customer)

		val expert= Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
		expertRepository.save(expert)

		val product = Product("Iphone", "15", 1234, customer)
		productRepository.save(product)

		val ticket = Ticket(TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(), Date(2020,1,1), Date(2020,1,1))
		ticketRepository.save(ticket)

		val response = restTemplate.getForEntity("/api/managers/1/tickets", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		val body = response.body
		val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
		println("resTicket $resTicket")
		Assertions.assertEquals(resTicket.getString("ticketState"), "OPEN")
		Assertions.assertEquals(resTicket.getInt("serialNumber"), 1234)
		Assertions.assertEquals(resTicket.getInt("expertId"), 1)
		Assertions.assertEquals(resTicket.getInt("expertId"),1)
		Assertions.assertEquals(resTicket.getInt("customerId"),1)
		Assertions.assertEquals(resTicket.getString("description"), "Description")
		Assertions.assertEquals(resTicket.getString("lastModified"), "2020-01-01")
		Assertions.assertEquals(resTicket.getString("creationDate"), "2020-01-01")
		Assertions.assertEquals(resTicket.getInt("ticketId"),1)
	}

	//	5) /api/customers/:customerId/tickets/:ticketId
	@Test
	fun successGetASingleTicketsOfACustomer(){
		val customer = Customer("Mario", "Rossi", Date(2020,1,1),  Date(1990,1,1),"mario.rossi@mail.com","0123456789")
		customerRepository.save(customer)

		val expert= Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
		expertRepository.save(expert)

		val product = Product("Iphone", "15", 1234, customer)
		productRepository.save(product)

		val ticket = Ticket(TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(), Date(2020,1,1), Date(2020,1,1))
		ticketRepository.save(ticket)

		val response = restTemplate.getForEntity("/api/customer/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		val body = response.body
		val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
		println("resTicket $resTicket")
		Assertions.assertEquals(resTicket.getString("ticketState"), "OPEN")
		Assertions.assertEquals(resTicket.getInt("serialNumber"), 1234)
		Assertions.assertEquals(resTicket.getInt("expertId"), 1)
		Assertions.assertEquals(resTicket.getInt("expertId"),1)
		Assertions.assertEquals(resTicket.getInt("customerId"),1)
		Assertions.assertEquals(resTicket.getString("description"), "Description")
		Assertions.assertEquals(resTicket.getString("lastModified"), "2020-01-01")
		Assertions.assertEquals(resTicket.getString("creationDate"), "2020-01-01")
		Assertions.assertEquals(resTicket.getInt("ticketId"),1)
	}

	//	6) /api/experts/:expertId/tickets/:ticketId
	@Test
	fun successGetASingleTicketsOfAnExpert(){
		val customer = Customer("Mario", "Rossi", Date(2020,1,1),  Date(1990,1,1),"mario.rossi@mail.com","0123456789")
		customerRepository.save(customer)

		val expert= Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
		expertRepository.save(expert)

		val product = Product("Iphone", "15", 1234, customer)
		productRepository.save(product)

		val ticket = Ticket(TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(), Date(2020,1,1), Date(2020,1,1))
		ticketRepository.save(ticket)

		val response = restTemplate.getForEntity("/api/expert/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		val body = response.body
		val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
		println("resTicket $resTicket")
		Assertions.assertEquals(resTicket.getString("ticketState"), "OPEN")
		Assertions.assertEquals(resTicket.getInt("serialNumber"), 1234)
		Assertions.assertEquals(resTicket.getInt("expertId"), 1)
		Assertions.assertEquals(resTicket.getInt("expertId"),1)
		Assertions.assertEquals(resTicket.getInt("customerId"),1)
		Assertions.assertEquals(resTicket.getString("description"), "Description")
		Assertions.assertEquals(resTicket.getString("lastModified"), "2020-01-01")
		Assertions.assertEquals(resTicket.getString("creationDate"), "2020-01-01")
		Assertions.assertEquals(resTicket.getInt("ticketId"),1)
	}

	//	7) /api/managers/:managerId/tickets/:ticketId
	@Test
	fun successGetASingleTicketsOfAManager(){
		val customer = Customer("Mario", "Rossi", Date(2020,1,1),  Date(1990,1,1),"mario.rossi@mail.com","0123456789")
		customerRepository.save(customer)

		val expert= Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
		expertRepository.save(expert)

		val product = Product("Iphone", "15", 1234, customer)
		productRepository.save(product)

		val ticket = Ticket(TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(), Date(2020,1,1), Date(2020,1,1))
		ticketRepository.save(ticket)

		val response = restTemplate.getForEntity("/api/managers/1/tickets/1", String::class.java)
		Assertions.assertNotNull(response)
		Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		val body = response.body
		val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
		println("resTicket $resTicket")
		Assertions.assertEquals(resTicket.getString("ticketState"), "OPEN")
		Assertions.assertEquals(resTicket.getInt("serialNumber"), 1234)
		Assertions.assertEquals(resTicket.getInt("expertId"), 1)
		Assertions.assertEquals(resTicket.getInt("expertId"),1)
		Assertions.assertEquals(resTicket.getInt("customerId"),1)
		Assertions.assertEquals(resTicket.getString("description"), "Description")
		Assertions.assertEquals(resTicket.getString("lastModified"), "2020-01-01")
		Assertions.assertEquals(resTicket.getString("creationDate"), "2020-01-01")
		Assertions.assertEquals(resTicket.getInt("ticketId"),1)
	}

	//	8) /api/managers/:managerId/tickets/:ticketId/assign
	@Test
	fun successapi8(){

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		val requestObject= JSONObject()
		requestObject.put("expertId", 1)
		println("requestObject $requestObject")

		val response = restTemplate.patchForObject("/api/managers/1/tickets/1/assign", requestObject.toString() ,String::class.java)
		//Assertions.assertNotNull(response)
		//Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
		println("ABECEDARIO $response")
	}
}