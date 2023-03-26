package com.lab2.server

import com.lab2.server.model.Customer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.Date

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)

	var customer = Customer("a", "b", Date(), Date(), "c", "d1")
	println(customer.ID)
}
