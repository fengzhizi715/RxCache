package com.safframework.rxcache.disk.browser

import com.safframework.rxcache.disk.browser.rxcache.rxCache
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.gson.gson
import io.ktor.http.Parameters
import io.ktor.http.content.defaultResource
import io.ktor.http.content.static
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.File
import java.text.DateFormat

/**
 *
 * @FileName:
 *          .Application
 * @author: Tony Shen
 * @date: 2020-06-30 14:24
 * @version: V1.0 <描述当前版本功能>
 */
fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        defaultEncoding = "utf-8"
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Routing) {
        static("/") {
            defaultResource("index.html", "web")
        }

        post("/saveConfig") {

            val postParameters: Parameters = call.receiveParameters()

            Config.path = postParameters["path"] ?: ""
            Config.type = postParameters["type"] ?: ""
            Config.converter = postParameters["converter"] ?: ""

            call.respond(FreeMarkerContent("save.ftl", mapOf("config" to Config)))
        }
        get("/list") {

            val file = File(Config.path)
            val array = file.list()
            call.respond(array)
        }
        get("/detail/{key}") {

            val key = call.parameters["key"]
            val json = rxCache.getStringData(key)
            call.respondText(json)
        }
    }
}

fun main() {
    embeddedServer(Netty, 8080, watchPaths = listOf("ApplicationKt"), module = Application::module).start()
}