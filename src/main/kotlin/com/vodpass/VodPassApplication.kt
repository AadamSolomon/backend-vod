package com.vodpass

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class VodPassApplication

fun main(args: Array<String>) {
    runApplication<VodPassApplication>(*args)
}
