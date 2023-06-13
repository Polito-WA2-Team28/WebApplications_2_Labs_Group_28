package com.final_project.server

import com.final_project.security.dto.UserCredentialsDTO
import com.final_project.server.config.GlobalConfig
import com.final_project.server.model.*
import com.final_project.server.repository.*
import com.final_project.ticketing.model.Ticket
import com.final_project.ticketing.repository.TicketRepository
import com.final_project.ticketing.util.*
import dasniko.testcontainers.keycloak.KeycloakContainer
import org.json.JSONObject
import org.junit.Ignore
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.*
import org.springframework.http.*
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.test.context.*
import org.testcontainers.containers.PostgreSQLContainer
import org.springframework.boot.test.web.server.LocalServerPort
import org.testcontainers.junit.jupiter.*
import org.springframework.util.MultiValueMap
import java.text.SimpleDateFormat
import java.util.*

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DbT1ApplicationTests {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")

        @Container
        val keycloak = KeycloakContainer("quay.io/keycloak/keycloak:latest")
            .withRealmImportFile("keycloak/realm.json")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }

            /* keycloak container */
            val keycloakBaseUrl = keycloak.authServerUrl
            registry.add("keycloakBaseUrl") { keycloakBaseUrl }
            registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri") { keycloakBaseUrl + "realms/TicketingServiceRealm" }
            registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri") { keycloakBaseUrl + "realms/TicketingServiceRealm/protocol/openid-connect/certs" }
        }
    }

    @Autowired private lateinit var globalConfig: GlobalConfig
    @Autowired lateinit var restTemplate: TestRestTemplate
    @Autowired lateinit var jwtDecoder: JwtDecoder
    @LocalServerPort protected var port: Int = 8080
    @Autowired lateinit var ticketRepository: TicketRepository
    @Autowired lateinit var customerRepository: CustomerRepository
    @Autowired lateinit var productRepository: ProductRepository
    @Autowired lateinit var expertRepository: ExpertRepository
    @Autowired lateinit var managerRepository: ManagerRepository

    @BeforeEach
    fun setUp() {
        ticketRepository.deleteAll()
        productRepository.deleteAll()
        customerRepository.deleteAll()
        managerRepository.deleteAll()
        expertRepository.deleteAll()
        val allocatedPort = keycloak.getMappedPort(8080)
        globalConfig.keycloakPort = allocatedPort.toString()
        globalConfig.keycloakURL = keycloak.host
    }


    @Test /** POST /api/auth/login */
    fun `Successful login`() {

        /* crafting the request */
        val credentials = UserCredentialsDTO("customer-test-1", "test")
        val body = HttpEntity(credentials)
        /* login */
        val response = restTemplate.postForEntity<String>(
            "/api/auth/login",
            body,
            HttpMethod.POST
        )
        /* assertions */
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }



    @Test /** GET /api/customers/tickets */
    fun `Customer retrieve all the tickets`() {
        /* adding data to database */
        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer, product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        /* customer login */
        val accessToken = customerLogin()

        /* crafting the request */
        val headers: MultiValueMap<String, String> = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        /* retrieving all the tickets */
        val response: ResponseEntity<String> = restTemplate.exchange(
            "http://localhost:$port/api/customers/tickets",
            HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java
        )

        val resTicket = JSONObject(response.body).getJSONArray("content").getJSONObject(0)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId.toString(), resTicket.getString("expertId"))
        Assertions.assertEquals(customerId.toString(), resTicket.getString("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Test /** GET /api/customers/tickets*/
    fun `Fail get all tickets without login`() {
        val url = "/api/customers/tickets"
        val response = restTemplate.getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response?.statusCode)
    }


    @Ignore
    @Test /** POST /api/customers/ticket POST*/
    fun `Successful creation of a new ticket`() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()


        val jsonRequest = JSONObject()
        jsonRequest.put("description", "myDescription")
        jsonRequest.put("serialNumber", product.serialNumber)

        /* customer login */
        val accessToken = customerLogin()

        /* crafting the request */
        val headers: MultiValueMap<String, String> = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
            add("content-type", "application/json")
        }

        /* retrieving all the tickets */
        val response: ResponseEntity<String> = restTemplate.exchange(
            "http://localhost:$port/api/customers/tickets",
            HttpMethod.POST,
            HttpEntity(jsonRequest.toString(), headers),
            String::class.java
        )


        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CREATED,response.statusCode)
        val body = JSONObject(response.body)
        Assertions.assertEquals("OPEN", body.getString("ticketState"))
        Assertions.assertEquals("myDescription",body.getString("description"))
        Assertions.assertEquals(product.serialNumber.toInt(), body.getInt("serialNumber"))
        Assertions.assertEquals(customerId.toString(), body.getString("customerId"))
        Assertions.assertEquals(0,body.optInt("expertId"))

    }

    @Test /** GET /api/experts/tickets*/
    fun successGetAllTicketsOfAnExpert() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket =createTestTicket(customer, product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        /* expert login */
        val accessToken = expertLogin()

        /* crafting the request */
        val headers: MultiValueMap<String, String> = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        /* retrieving all the tickets */
        val response: ResponseEntity<String> = restTemplate.exchange(
            "http://localhost:$port/api/experts/tickets",
            HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java
        )


        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        val body = response.body
        val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId.toString(), resTicket.getString("expertId"))
        Assertions.assertEquals(customerId.toString(), resTicket.getString("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Test /** GET /api/experts/tickets*/
    fun failGetAllTicketsOfANonExistentExpert() {
        val url = "/api/experts/tickets"
        val response = restTemplate.getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response?.statusCode)
    }

    @Test /** GET /api/managers/tickets*/

    fun successGetAllTicketsOfAManager() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket =createTestTicket(customer, product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        managerRepository.save(manager).id

        /* manager login */
        val accessToken = managerLogin()

        /* crafting the request */
        val headers: MultiValueMap<String, String> = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        /* retrieving all the tickets */
        val response: ResponseEntity<String> = restTemplate.exchange(
            "http://localhost:$port/api/managers/tickets",
            HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        val body = response.body
        val resTicket = JSONObject(body).getJSONArray("content").getJSONObject(0)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId.toString(), resTicket.getString("expertId"))
        Assertions.assertEquals(customerId.toString(), resTicket.getString("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Test /** GET /api/managers/tickets*/
    fun failGetAllTicketsOfANonExistentManager() {

        val url = "/api/managers/tickets"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response?.statusCode)
    }
     @Test /** GET /api/customers/:customerId/tickets/:ticketId*/
    fun successGetASingleTicketsOfACustomer() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer, product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

         /* customer login */
         val accessToken = customerLogin()

         /* crafting the request */
         val headers: MultiValueMap<String, String> = HttpHeaders().apply {
             add("Authorization", "Bearer $accessToken")
         }

         /* retrieving all the tickets */
         val response: ResponseEntity<String> = restTemplate.exchange(
             "http://localhost:$port/api/customers/tickets/${ticketId}",
             HttpMethod.GET,
             HttpEntity(null, headers),
             String::class.java
         )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
        val body = response.body
        val resTicket = JSONObject(body)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId.toString(), resTicket.getString("expertId"))
        Assertions.assertEquals(customerId.toString(), resTicket.getString("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

    @Test
    /** GET /api/customers/tickets/:ticketId*/
    fun failGetASingleTicketOfANonExistentCustomer(){
        val ticketId = (0..100).random()
        val url = "/api/customers/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response?.statusCode)
    }

    @Test
    /** GET /api/customers/tickets/:ticketId*/
    fun failGetANonExistentTicketOfACustomer(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        /* customer login */
        val accessToken = customerLogin()

        /* crafting the request */
        val headers: MultiValueMap<String, String> = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        val ticketId = (0..100).random()
        /* retrieving all the tickets */
        val response: ResponseEntity<String> = restTemplate.exchange(
            "http://localhost:$port/api/customers/tickets/${ticketId}",
            HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Ignore @Test
    /** GET /api/experts/:expertId/tickets/:ticketId */
    fun successGetASingleTicketsOfAnExpert() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer, product, expert)
        val ticketId = ticketRepository.save(ticket).getId()


        /* customer login */
        val accessToken = customerLogin()

        /* crafting the request */
        val headers: MultiValueMap<String, String> = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        /* retrieving all the tickets */
        val response: ResponseEntity<String> = restTemplate.exchange(
            "http://localhost:$port/api/experts/tickets/${ticketId}",
            HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java
        )
        val body = response.body
        val resTicket = JSONObject(body)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId.toString(), resTicket.getString("expertId"))
        Assertions.assertEquals(customerId.toString(), resTicket.getString("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))

    }

    @Test /** GET /api/experts/:expertId/tickets/:ticketId */
    fun failGetASingleTicketOfAnExpertWithoutLogin(){
        val ticketId = (0..100).random()
        val url = "/api/experts/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response?.statusCode)
    }

    @Test /** GET /api/experts/tickets/:ticketId */
    fun failGetANonExistentTicketOfAnExpert(){
        val expert = createTestExpert()
        expertRepository.save(expert).id
        val ticketId = (0..100).random()

        /* expert login */
        val accessToken = expertLogin()

        /* crafting the request */
        val headers: MultiValueMap<String, String> = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        /* retrieving all the tickets */
        val response: ResponseEntity<String> = restTemplate.exchange(
            "http://localhost:$port/api/experts/tickets/${ticketId}",
            HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    /** GET /api/managers/:managerId/tickets/:ticketId */
    fun successGetASingleTicketsOfAManager() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val url = "/api/managers/${managerId}/tickets/${ticketId}"
        val response = restTemplate
            .getForEntity(url, String::class.java)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response?.statusCode)
        val body = response.body
        val resTicket = JSONObject(body)
        Assertions.assertEquals("OPEN", resTicket.getString("ticketState"))
        Assertions.assertEquals(product.serialNumber.toInt(), resTicket.getInt("serialNumber"))
        Assertions.assertEquals(expertId.toString(), resTicket.getString("expertId"))
        Assertions.assertEquals(customerId.toString(), resTicket.getString("customerId"))
        Assertions.assertEquals(ticket.description, resTicket.getString("description"))
        Assertions.assertEquals(ticket.lastModified.formatDate(), resTicket.getString("lastModified"))
        Assertions.assertEquals(ticket.creationDate.formatDate(), resTicket.getString("creationDate"))
        Assertions.assertEquals(ticketId!!.toInt(), resTicket.getInt("ticketId"))
    }

	@Test
    /** PATCH /api/managers/:managerId/tickets/:ticketId/assign */
    fun successAssignmentOfATicket() {

        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestObject = JSONObject()
        requestObject.put("expertId", expertId.toString())

        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/assign",
            HttpMethod.PATCH,
            HttpEntity(requestObject.toString(), headers),
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertNotNull(actualTicket.expert)
        Assertions.assertEquals(actualTicket.expert!!.id, expertId )
        Assertions.assertEquals(actualTicket.state, TicketState.IN_PROGRESS)


    }

    @Test
    /** PATCH /api/managers/:managerId/tickets/:ticketId/relieveExpert */
    fun successRelieveExpert(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestObject = JSONObject()
        requestObject.put("expertId", expertId.toString())

        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/relieveExpert",
            HttpMethod.PATCH,
            HttpEntity(requestObject.toString(), headers),
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertNull(actualTicket.expert)
        Assertions.assertEquals(actualTicket.state, TicketState.OPEN)
    }

    @Test
    /** PATCH /api/managers/:managerId/tickets/:ticketId/relieveExpert */
    fun failRelieveExpertWithNonExistentIds(){
        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id
        val ticketId = (0..100).random()
        val url = "/api/managers/${managerId}/tickets/${ticketId}/relieveExpert"
        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/relieveExpert",
            HttpMethod.PATCH,
            null,
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test /** PATCH /api/customers/:customerId/tickets/:ticketId/reopen */
    fun successReopenClosedTicket(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/customers/${customer.id}/tickets/${ticketId}/reopen",
            HttpMethod.PATCH,
            null,
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.REOPENED, actualTicket.state)
    }

    @Test /** PATCH /api/customers/:customerId/tickets/:ticketId/reopen */
    fun successReopenResolvedTicket(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/customers/${customer.id}/tickets/${ticketId}/reopen",
            HttpMethod.PATCH,
            null,
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.REOPENED, actualTicket.state)
    }

    @Test /** PATCH /api/customers/:customerId/tickets/:ticketId/reopen */
    fun failReopenAlreadyOpenTicket(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val url = "/api/managers/${managerId}/tickets/${ticketId}/relieveExpert"
        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/relieveExpert",
            HttpMethod.PATCH,
            null,
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CONFLICT, response?.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.OPEN, actualTicket.state)
    }

    @Test /** PATCH /api/experts/:expertId/tickets/:ticketId/resolve */
    fun successResolveOpenTicket(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/experts/${expertId}/tickets/${ticketId}/resolve",
            HttpMethod.PATCH,
            null,
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.RESOLVED, actualTicket.state)
    }

    @Test /** PATCH /api/experts/:expertId/tickets/:ticketId/resolve */
    fun failResolveClosedTicket(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/experts/${expertId}/tickets/${ticketId}/resolve",
            HttpMethod.PATCH,
            null,
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.CLOSED, actualTicket.state)
    }

    @Test /** PATCH /api/managers/:managerId/tickets/:ticketId/close */
    fun successCloseOpenedTicketByManager(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/close",
            HttpMethod.PATCH,
            null,
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(actualTicket.state, TicketState.CLOSED)
    }

    @Test /** PATCH /api/managers/:managerId/tickets/:ticketId/close */
    fun failCloseAlreadyClosedTicketByManager() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/close",
            HttpMethod.PATCH,
            null,
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.CLOSED, actualTicket.state)
    }

    @Test /** PATCH /api/managers/:managerId/tickets/:ticketId/resumeProgress */
    fun succeedResumeProgress() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id


        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestObject = JSONObject()
        requestObject.put("expertId", expert.id)

        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/resumeProgress",
            HttpMethod.PATCH,
            HttpEntity(requestObject.toString(), headers),
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.IN_PROGRESS, actualTicket.state)

    }

    @Test /** PATCH /api/managers/:managerId/tickets/:ticketId/resumeProgress */
    fun failResumeProgressAlreadyClosedTicket() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestObject = JSONObject()
        requestObject.put("expertId", expert.id)

        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/resumeProgress",
            HttpMethod.PATCH,
            HttpEntity(requestObject.toString(), headers),
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.CLOSED, actualTicket.state)
    }

    @Test /** PATCH '/api/experts/:expertId/tickets/:ticketId/close' */
    fun successCloseOpenedTicketByExpert(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/experts/${expertId}/tickets/${ticketId}/close",
            HttpMethod.PATCH,
            null,
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(actualTicket.state, TicketState.CLOSED)
    }

    @Test /** PATCH '/api/experts/:expertId/tickets/:ticketId/close' */
    fun failCloseAlreadyClosedTicketByExpert() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestObject = JSONObject()
        requestObject.put("expertId", expert.id)

        val response = restTemplate.exchange(
            "/api/experts/${expertId}/tickets/${ticketId}/close",
            HttpMethod.PATCH,
            HttpEntity(requestObject.toString(), headers),
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.CLOSED, actualTicket.state)
    }

    @Test /** PATCH /api/customers/:customerId/tickets/:ticketId/compileSurvey */
    fun successCompileSurvey(){
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/customers/${customer.id}/tickets/${ticketId}/compileSurvey",
            HttpMethod.PATCH,
            null,
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(actualTicket.state, TicketState.CLOSED)
    }

    @Test
    /** PATCH /api/customers/:customerId/tickets/:ticketId/compileSurvey */
    fun failCompileSurveyTicketAlreadyClosed() {
        val customer = createTestCustomer()
        val customerId = customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = createTestTicket(customer,product, expert)
        val ticketId = ticketRepository.save(ticket).getId()

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestObject = JSONObject()
        requestObject.put("expertId", expert.id)

        val response = restTemplate.exchange(
            "/api/customers/${customer.id}/tickets/${ticketId}/compileSurvey",
            HttpMethod.PATCH,
            HttpEntity(requestObject.toString(), headers),
            String::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode)

        val actualTicket = ticketRepository.getReferenceById(ticketId!!)
        Assertions.assertEquals(TicketState.CLOSED, actualTicket.state)
    }

    @Test
    /** PATCH /api/managers/tickets/:ticketId/remove*/
    fun successRemoveTicket(){
        val customer = createTestCustomer()
        customerRepository.save(customer).id

        val expert = createTestExpert()
        val expertId = expertRepository.save(expert).id

        val product = createTestProduct(customer)
        productRepository.save(product).getId()

        val ticket = Ticket(
            TicketState.RESOLVED, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
        val ticketId = ticketRepository.save(ticket).getId()!!

        val manager = createTestManager()
        val managerId = managerRepository.save(manager).id

        val response = restTemplate.exchange(
            "/api/managers/${managerId}/tickets/${ticketId}/remove",
            HttpMethod.DELETE,
            null,
            String::class.java
        )
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    private fun myDate(year: Int, month: Int, day: Int): Date {
        return Date(year - 1900, month - 1, day)
    }
    private fun Date.formatDate(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(this)
    }
    private fun createTestCustomer(): Customer{
        return Customer(
            UUID.fromString("0ae24126-7590-4e62-9f05-199f61824ed6"),
            "Mario", "Rossi", "mariorossi",
            myDate(2022, 1, 1), myDate(1990, 1, 1),
            "mario.rossi@mail.com", "0123456789"
        )
    }
    private fun createTestExpert(): Expert{
        return Expert(UUID.fromString("6e2f3411-1f7b-4da4-9128-2bac562b3687"),
            "expert01@mail.com", mutableSetOf(ExpertiseFieldEnum.APPLIANCES))
    }
    private fun createTestProduct(customer: Customer): Product{
        return Product(1,1234,"Iphone", "15", customer)
    }
    private fun createTestTicket(customer: Customer, product: Product, expert:Expert):Ticket{
        return Ticket(
            TicketState.OPEN, customer, expert, "Description", product, mutableSetOf(),
            myDate(2020, 1, 1), myDate(2020, 1, 1)
        )
    }
    private fun createTestManager(): Manager{
        return Manager(UUID.fromString("3eb963ee-1404-45e1-bef2-9583d4b6243f"),"manager@ticketingservice.it")
    }
    private fun customerLogin():String{return login("customer-test-1", "test")}
    private fun expertLogin():String{return login("expert-1", "test")}
    private fun managerLogin():String{return login("manager-1","test")}
    private fun login(username: String, password: String): String {

        /* crafting the request */
        val credentials = UserCredentialsDTO(username, password)
        val body = HttpEntity(credentials)

        /* login */
        val response = restTemplate.postForEntity<String>(
            "/api/auth/login",
            body,
            HttpMethod.POST
        )

        /* retrieving the access token */
        return JSONObject(response.body)["accessToken"].toString()
    }
}