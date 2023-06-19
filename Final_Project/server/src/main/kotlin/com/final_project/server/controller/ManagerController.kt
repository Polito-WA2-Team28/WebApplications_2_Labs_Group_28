package com.final_project.server.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.dto.*
import com.final_project.server.exception.Exception
import com.final_project.server.service.CustomerServiceImpl
import com.final_project.server.service.ExpertService
import com.final_project.server.service.ManagerService
import com.final_project.ticketing.dto.MessageDTO
import com.final_project.ticketing.dto.PageResponseDTO
import com.final_project.ticketing.dto.PageResponseDTO.Companion.toDTO
import com.final_project.ticketing.dto.computePageSize
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Observed
class ManagerController @Autowired constructor(
    val managerService: ManagerService,
    val expertService: ExpertService,
    val securityConfig: SecurityConfig
) {

    val logger: Logger = LoggerFactory.getLogger(CustomerController::class.java)

    @GetMapping("/api/managers/experts")
    @ResponseStatus(HttpStatus.OK)
    fun getAllExperts(
        @RequestParam("pageNo", defaultValue = "1") pageNo: Int
    ): PageResponseDTO<ExpertDTO> {

        /* checking that manager exists */
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        val manager = managerService.getManagerById(managerId)
        manager ?: run {
            logger.error("Endpoint: /api/manager/experts Error: Manager not found.")
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* fetching page from DB */
        var result: PageResponseDTO<ExpertDTO> = PageResponseDTO()
        val page: Pageable = PageRequest.of(pageNo-1, result.computePageSize())  /* ticketing-service uses 1-based paged mechanism while Pageable uses 0-based paged mechanism*/

        /* return result to client */
        result = expertService.getAllExpertsWithPaging(page).toDTO()
        return result

    }
}