package com.lab3.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
	runApplication<com.lab3.server.ServerApplication>(*args)

}
