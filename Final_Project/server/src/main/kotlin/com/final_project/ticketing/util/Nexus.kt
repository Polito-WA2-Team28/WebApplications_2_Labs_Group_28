package com.final_project.ticketing.util

import com.final_project.server.dto.CustomerDTO
import com.final_project.server.dto.ExpertDTO
import com.final_project.server.dto.ManagerDTO
import com.final_project.server.dto.ProductDTO
import com.final_project.server.exception.Exception
import java.util.UUID
import com.final_project.server.service.*
import com.final_project.ticketing.dto.TicketDTO
import com.final_project.ticketing.exception.TicketException
import com.final_project.ticketing.service.TicketService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Nexus () {

    /* services */
    private lateinit var customerService: CustomerService
    private lateinit var expertService: ExpertService
    private lateinit var managerService: ManagerService
    private lateinit var ticketService: TicketService
    private lateinit var productService: ProductService

    constructor(customerService: CustomerService): this() {
        this.customerService = customerService
    }

    constructor(customerService: CustomerService, ticketService: TicketService, productService: ProductService) : this() {
        this.customerService = customerService
        this.ticketService = ticketService
        this.productService = productService
    }

    constructor(expertService: ExpertService, ticketService: TicketService) : this() {
        this.expertService = expertService
        this.ticketService = ticketService
    }

    constructor(managerService: ManagerService, expertService: ExpertService, ticketService: TicketService) : this() {
        this.managerService = managerService
        this.expertService = expertService
        this.ticketService = ticketService
    }

    /* logging */
    private val endpointHolder: ThreadLocal<String> = ThreadLocal()
    private val logger: Logger = LoggerFactory.getLogger(Nexus::class.java)

    /* assertions */
    var customer: CustomerDTO? = null
    var expert: ExpertDTO? = null
    var manager: ManagerDTO? = null
    var ticket: TicketDTO? = null
    var product: ProductDTO? = null

    fun setEndpointForLogger(endpoint: String): Nexus {
        endpointHolder.set(endpoint)
        return this
    }

    fun assertCustomerExists(customerId: UUID): Nexus {
        this.customer = customerService.getCustomerById(customerId) ?: run {
            logger.error("Endpoint: ${endpointHolder.get()} Error: No customer profile found with this UUID.")
            throw Exception.CustomerNotFoundException("No customer profile found with this UUID.")
        }
        return this
    }

    fun assertExpertExists(expertId: UUID): Nexus {
        this.expert = expertService.getExpertById(expertId) ?: run {
            logger.error("Endpoint: ${endpointHolder.get()} Error: No expert profile found with this UUID.")
            throw Exception.ExpertNotFoundException("No expert profile found with this UUID.")
        }
        return this
    }

    fun assertManagerExists(managerId: UUID): Nexus {
        this.manager = managerService.getManagerById(managerId) ?: run {
            logger.error("Endpoint: ${endpointHolder.get()} Error: No manager profile found with this UUID.")
            throw Exception.ManagerNotFoundException("No manager profile found with this UUID.")
        }
        return this
    }

    fun assertTicketNonNull(ticket: TicketDTO?): Nexus {
        ticket ?: run {
            logger.error("Endpoint: ${endpointHolder.get()} Error: Ticket creation error.")
            throw TicketException.TicketCreationException("Ticket creation error.")
        }
        return this
    }

    fun assertTicketExists(ticketId: Long): Nexus {
        this.ticket = ticketService.getTicketDTOById(ticketId) ?: run {
            logger.error("Endpoint: ${endpointHolder.get()} Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }
        return this
    }

    fun assertTicketOwnership(): Nexus {
        if (this.ticket!!.customerId != this.customer!!.id) {
            logger.error("Endpoint: ${endpointHolder.get()} Error: Forbidden.")
            throw TicketException.TicketForbiddenException("Forbidden.")
        }
        return this
    }

    fun assertTicketAssignment(): Nexus {
        if (this.ticket!!.expertId == null || this.ticket!!.expertId != this.expert!!.id) {
            logger.error("Endpoint: ${endpointHolder.get()} Error: Expert not assigned to this ticket.")
            throw Exception.ExpertNotFoundException("Expert not assigned to this ticket.")
        }
        return this
    }

    fun assertTicketStatus(allowedStates: Set<TicketState>): Nexus {
        if (!allowedStates.contains(this.ticket!!.ticketState)) {
            logger.error("Endpoint: ${endpointHolder.get()} Error: Invalid ticket status for this operation..")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }
        return this
    }

    fun assertProductExists(serialNumber: UUID): Nexus {
        this.product = productService.customerGetProductBySerialNumber(this.customer!!.id, serialNumber) ?: run {
            logger.error("Endpoint: ${endpointHolder.get()} Error: Forbidden.")
            throw Exception.ProductNotFoundException("Forbidden.")
        }
        return this
    }

    fun assertProductOwnership(): Nexus {
        if (this.product!!.owner != this.customer!!.id) {
            logger.error("Endpoint: ${endpointHolder.get()} Error: Customer is not the owner of this product.")
            throw Exception.CustomerNotOwnerException("Customer is not the owner of this product.")
        }
        return this
    }

    /* operations */
    fun assignTicketToExpert(ticketId: Long, expertId: UUID): Nexus {
        this.ticket!!.assignExpert(expertId)
        this.ticket!!.changeState(TicketState.IN_PROGRESS)
        ticketService.assignTicketToExpert(ticketId, expertId)
        ticketService.changeTicketStatus(ticketId, TicketState.IN_PROGRESS)
        return this
    }

    fun relieveExpertFromTicket(ticketId: Long): Nexus {
        this.ticket!!.relieveExpert()
        this.ticket!!.changeState(TicketState.OPEN)
        ticketService.relieveExpertFromTicket(ticketId)
        ticketService.changeTicketStatus(ticketId, TicketState.OPEN)
        return this
    }

    fun closeTicket(ticketId: Long): Nexus {
        this.ticket!!.changeState(TicketState.CLOSED)
        ticketService.changeTicketStatus(ticketId, TicketState.CLOSED)
        return this
    }

    fun resolveTicket(ticketId: Long): Nexus {
        this.ticket!!.changeState(TicketState.RESOLVED)
        ticketService.changeTicketStatus(ticketId, TicketState.RESOLVED)
        return this
    }

    fun reopenTicket(ticketId: Long): Nexus {
        this.ticket!!.changeState(TicketState.REOPENED)
        ticketService.changeTicketStatus(ticketId, TicketState.REOPENED)
        return this
    }

}