package com.final_project.ticketing.service

import com.final_project.server.dto.CustomerDTO
import com.final_project.server.dto.ProductDTO
import com.final_project.server.model.toModel
import com.final_project.server.service.FileStorageService
import com.final_project.ticketing.dto.*
import com.final_project.ticketing.model.Attachment
import com.final_project.ticketing.model.toModel
import com.final_project.ticketing.repository.MessageRepository
import com.final_project.ticketing.repository.TicketRepository
import com.final_project.ticketing.util.TicketState
import org.springframework.beans.factory.annotation.Autowired
import com.final_project.ticketing.dto.TicketCreationData
import com.final_project.ticketing.dto.TicketDTO
import com.final_project.ticketing.dto.toDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class TicketServiceImpl @Autowired constructor(private val ticketRepository: TicketRepository,
                                               private val messageRepository: MessageRepository,
                                               private val fileStorageService: FileStorageService) : TicketService{



    override fun getTicketDTOById(id: Long): TicketDTO? {
        return ticketRepository.findByIdOrNull(id)?.toDTO()
    }

    override fun createTicket(ticketDTO: TicketCreationData, customerDTO: CustomerDTO, productDTO: ProductDTO): TicketDTO? {
        val customer = customerDTO.toModel()
        val product = productDTO.toModel(customer)
        return ticketRepository.save(ticketDTO.toModel(customer, product)).toDTO()
    }

    override fun getAllExpertTickets(expertId: UUID): List<TicketDTO> {
        return ticketRepository.findByExpertId(expertId).map{it.toDTO()}
    }

    override fun changeTicketStatus(ticketId: Long, newState: TicketState) {
        return ticketRepository.updateTicketState(ticketId, newState)
    }

    override fun assignTicketToExpert(ticketId: Long, expertId: UUID) {
        return ticketRepository.assignTicketToExpert(ticketId, expertId)
    }

    override fun relieveExpertFromTicket(ticketId: Long) {
        return ticketRepository.relieveExpertFromTicket(ticketId)
    }

    override fun removeTicketById(ticketId: Long): Unit {
        ticketRepository.deleteById(ticketId)
    }

    @Transactional(readOnly=true)
    override fun getAllTicketsWithPaging(pageable: Pageable): Page<TicketDTO> {
        return ticketRepository.findAll(pageable)
            .map {
                it.toDTO()
            }
    }


    @Transactional(readOnly=true)
    override fun getAllTicketsWithPagingByCustomerId(customerId: UUID, pageable: Pageable): Page<TicketDTO> {
        return ticketRepository.findAllByCustomerId(customerId, pageable)
            .map {
                it.toDTO()
            }
    }

    @Transactional(readOnly=true)
    override fun getAllTicketsWithPagingByExpertId(expertId: UUID, pageable: Pageable): Page<TicketDTO> {
        return ticketRepository.findAllByExpertId(expertId, pageable)
            .map {
                it.toDTO()
            }
    }

    @Transactional
    override fun sendTicketMessage(message: MessageObject, ticketId: Long, sender: String?): MessageDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
        val attachmentSet = mutableSetOf<Attachment>()

        if(ticket == null){
            // error
            throw Exception()
        }

        if (message.attachments != null) {
            message.attachments.stream()
                .map { att -> fileStorageService.persistAttachmentFile(att) }
                .filter { attStored -> attStored != null }
                .map { attStored -> attStored!! }
                .forEach { attStored -> attachmentSet.add(attStored)}
        }

        // Attachments persisted via Cascading
        return messageRepository.save(message.toModel(attachmentSet, sender, ticket)).toDTO()
    }

    @Transactional(readOnly=true)
    override fun getAllMessagesWithPagingByTicketId(
        ticketId: Long,
        pageable: Pageable
    ): Page<MessageDTO> {
        return messageRepository.findAllByTicketId(ticketId, pageable)
            .map {
                it.toDTO()
            };
    }

}