package com.lab5.server.controller

import io.micrometer.observation.annotation.Observed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController

@RestController
@Observed
class StaffController {

    val logger: Logger = LoggerFactory.getLogger(StaffController::class.java)


}