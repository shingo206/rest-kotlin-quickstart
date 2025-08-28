package com.nipponexpress.master.user.application.service

import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class GreetingService {
    fun greeting(name: String) = Uni.createFrom().item("Hello, $name!")

    fun bye(name: String) = "Bye, $name!"
}
