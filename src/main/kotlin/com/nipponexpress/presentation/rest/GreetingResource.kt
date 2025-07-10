package com.nipponexpress.presentation.rest

import com.nipponexpress.application.service.GreetingService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import io.smallrye.mutiny.Uni

@Path("/hello")
class GreetingResource(
    private val greetingService: GreetingService
) {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "Hello from Quarkus REST"

    @GET
    @Path("/greeting/{name}")
    fun greeting(@PathParam("name") name: String): Uni<String> =
        greetingService.greeting(name)
            .onFailure().transform { IllegalArgumentException("Hello, $name!") }

    @GET
    @Path("/bye/")
    @Produces(MediaType.TEXT_PLAIN)
    fun bye(@QueryParam("name") name: String) = greetingService.bye(name)
}
