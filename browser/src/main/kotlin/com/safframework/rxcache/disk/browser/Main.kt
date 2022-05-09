package com.safframework.rxcache.disk.browser

import com.safframework.rxcache.disk.browser.rxcache.rxCache
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.io.File
import java.text.DateFormat

/**
 *
 * @FileName:
 *          com.safframework.rxcache.disk.browser.Main
 * @author: Tony Shen
 * @date: 2020-06-30 14:24
 * @version: V1.0 <描述当前版本功能>
 */
fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
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
        get("/info") {

            val json = rxCache.info
            call.respondText(json)
        }
    }
}

fun main(args: Array<String>) {

    val parser = ArgParser("rxcache-browser")
    val port            by parser.option(ArgType.Int, shortName = "p", description = "Port number of the local web service")
    parser.parse(args)

    embeddedServer(Netty, port?:8080, watchPaths = listOf("MainKt"), module = Application::module).start()
}