package com.lab3.server

import com.lab3.server.model.*
import com.lab3.server.repository.*
import com.lab3.ticketing.model.Ticket
import com.lab3.ticketing.repository.TicketRepository
import com.lab3.ticketing.util.*
import org.json.JSONObject
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.*
import java.text.SimpleDateFormat
import java.util.*


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DbT1ApplicationTests {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }
    }

    @LocalServerPort
    protected var port: Int = 8080

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired lateinit var ticketRepository: TicketRepository
    @Autowired lateinit var customerRepository: CustomerRepository
    @Autowired lateinit var productRepository: ProductRepository
    @Autowired lateinit var expertRepository: ExpertRepository
    @Autowired lateinit var managerRepository: ManagerRepository

    @BeforeEach
    fun repositoryClean() {
        ticketRepository.deleteAll()
        productRepository.deleteAll()
        customerRepository.deleteAll()
        managerRepository.deleteAll()
        expertRepository.deleteAll()
    }

	@Test /** GET /api/customers/:customerId/tickets*/
	fun successGetAllTicketsOfACustomer() {
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        val customerId = customerRepository.save(customer).getId()

        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()

        val product = Product("Iphone", "15", 1234, customer)
        productRepository.save(product).getId()

        val ticket = Ticket(
            TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()

        val url = "/api/customers/${customerId}/tickets"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
        val body = response.body
        val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId!!.toInt(), resTicket.getInt("expertId"))
        Assertions.assertEquals(customerId!!.toInt(), resTicket.getInt("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Test /** GET /api/customers/:customerId/tickets*/
    fun failGetAllTicketsOfANonExistentCustomer() {
        val customerId = (0..100).random()
        val url = "/api/customers/${customerId}/tickets"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test /** POST /api/customers/:customerId/ticket POST*/
    fun successCreationOfANewTicket() {
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        val customerId = customerRepository.save(customer).getId()

        val product = Product("Iphone", "15", 1234, customer)
        productRepository.save(product).getId()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val jsonRequest = JSONObject()
        jsonRequest.put("description", "myDescription")
        jsonRequest.put("serialNumber", product.serialNumber)

        val response = restTemplate.postForEntity(
            "/api/customers/${customerId}/tickets",
            HttpEntity(jsonRequest.toString(), headers),
            String::class.java)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CREATED,response.statusCode)
        val body = JSONObject(response.body)

		Assertions.assertEquals("OPEN", body.getString("ticketState"))
		Assertions.assertEquals("myDescription",body.getString("description"))
		Assertions.assertEquals(product.serialNumber.toInt(), body.getInt("serialNumber"))
		Assertions.assertEquals(customerId!!.toInt(), body.getInt("customerId"))
		Assertions.assertEquals(0,body.optInt("expertId"))

    }

    @Test /** GET /api/experts/:expertId/tickets*/
    fun successGetAllTicketsOfAnExpert() {
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        val customerId = customerRepository.save(customer).getId()

        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()

        val product = Product("Iphone", "15", 1234, customer)
        val productId = productRepository.save(product).getId()

        val ticket = Ticket(
            TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()

        val url = "/api/experts/${expertId}/tickets"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
        val body = response.body
        val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId!!.toInt(), resTicket.getInt("expertId"))
        Assertions.assertEquals(customerId!!.toInt(), resTicket.getInt("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Test /** GET /api/experts/:expertId/tickets*/
    fun failGetAllTicketsOfANonExistentExpert() {
        val expertId = (0..100).random()
        val url = "/api/experts/${expertId}/tickets"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test /** GET /api/managers/:managerId/tickets*/
    fun successGetAllTicketsOfAManager() {
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        val customerId = customerRepository.save(customer).getId()

        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()

        val product = Product("Iphone", "15", 1234, customer)
        productRepository.save(product).getId()

        val ticket = Ticket(
            TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = Manager("manager@mail.com")
        val managerId = managerRepository.save(manager).getId()

        val url = "/api/managers/${managerId}/tickets"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
        val body = response.body
        val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId!!.toInt(), resTicket.getInt("expertId"))
        Assertions.assertEquals(customerId!!.toInt(), resTicket.getInt("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Test /** GET /api/managers/:managerId/tickets*/
    fun failGetAllTicketsOfANonExistentManager() {
        val managerId = (0..100).random()
        val url = "/api/managers/${managerId}/tickets"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test /** GET /api/customers/:customerId/tickets/:ticketId*/
    fun successGetASingleTicketsOfACustomer() {
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        val customerId = customerRepository.save(customer).getId()

        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()

        val product = Product("Iphone", "15", 1234, customer)
        productRepository.save(product).getId()

        val ticket = Ticket(
            TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()

        val url = "/api/customers/${customerId}/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
        val body = response.body
        val resTicket = JSONObject(body)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId!!.toInt(), resTicket.getInt("expertId"))
        Assertions.assertEquals(customerId!!.toInt(), resTicket.getInt("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Test /** GET /api/customers/:customerId/tickets/:ticketId*/
    fun failGetASingleTicketOfANonExistentCustomer(){
        val customerId = (0..100).random()
        val ticketId = (0..100).random()
        val url = "/api/customers/${customerId}/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test /** GET /api/customers/:customerId/tickets/:ticketId*/
    fun failGetANonExistentTicketOfACustomer(){
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        val customerId = customerRepository.save(customer).getId()
        val ticketId = (0..100).random()
        val url = "/api/customers/${customerId}/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test /** GET /api/experts/:expertId/tickets/:ticketId */
    fun successGetASingleTicketsOfAnExpert() {
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        val customerId = customerRepository.save(customer).getId()

        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()

        val product = Product("Iphone", "15", 1234, customer)
        val productId = productRepository.save(product).getId()

        val ticket = Ticket(
            TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()

        val url = "/api/experts/${expertId}/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
        val body = response.body
        val resTicket = JSONObject(body)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId!!.toInt(), resTicket.getInt("expertId"))
        Assertions.assertEquals(customerId!!.toInt(), resTicket.getInt("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))

    }

    @Test /** GET /api/experts/:expertId/tickets/:ticketId */
    fun failGetASingleTicketOfANonExistentExpert(){
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        customerRepository.save(customer)
        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()
        val product = Product("Iphone", "15", 1234, customer)
        productRepository.save(product)
        val ticket = Ticket(
            TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()

        val url = "/api/experts/${expertId!!.toInt() + 1}/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test /** GET /api/experts/:expertId/tickets/:ticketId */
    fun failGetANonExistentTicketOfAnExpert(){
        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()
        val ticketId = (0..100).random()
        val url = "/api/experts/${expertId}/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test /** GET /api/managers/:managerId/tickets/:ticketId */
    fun successGetASingleTicketsOfAManager() {
        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        val customerId = customerRepository.save(customer).getId()

        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()

        val product = Product("Iphone", "15", 1234, customer)
        productRepository.save(product).getId()

        val ticket = Ticket(
            TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = Manager("manager@mail.com")
        val managerId = managerRepository.save(manager).getId()

        val url = "/api/managers/${managerId}/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
        val body = response.body
        val resTicket = JSONObject(body)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId!!.toInt(), resTicket.getInt("expertId"))
        Assertions.assertEquals(customerId!!.toInt(), resTicket.getInt("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Ignore
	@Test /** PATCH /api/managers/:managerId/tickets/:ticketId/assign */
    fun successAssignmentOfATicket() {

        val customer = Customer(
            "Mario", "Rossi",
            myDate(2022, 1, 1),
            myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
        customerRepository.save(customer).getId()
        val expert = Expert("expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
        val expertId = expertRepository.save(expert).getId()
        val product = Product("Iphone", "15", 1234, customer)
        productRepository.save(product).getId()

        val ticket = Ticket(
            TicketState.OPEN, customer, null, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()!!

        val manager = Manager("manager@mail.com")
        val managerId = managerRepository.save(manager).getId()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestObject = JSONObject()
        requestObject.put("expertId", expertId!!.toInt())

        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/assign",
            HttpMethod.PATCH,
            HttpEntity(requestObject.toString(), headers),
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId)
        Assertions.assertNotNull(actualTicket.expert)
        Assertions.assertEquals(actualTicket.expert!!.getId(), expertId )
        Assertions.assertEquals(actualTicket.state, TicketState.IN_PROGRESS)


    }


    private fun myDate(year: Int, month: Int, day: Int): Date {
        return Date(year - 1900, month - 1, day)
    }

    private fun Date.formatDate(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(this)
    }

}