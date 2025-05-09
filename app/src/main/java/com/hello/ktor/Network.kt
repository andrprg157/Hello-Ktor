package com.hello.ktor


import io.ktor.client.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class Network {

    val mclient = HttpClient() {
        install(ContentNegotiation) {
            json(json = Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
        install(io.ktor.client.plugins.logging.Logging) {
            level = io.ktor.client.plugins.logging.LogLevel.ALL
            logger = io.ktor.client.plugins.logging.Logger.DEFAULT
        }
    }



    suspend fun getLogin_json(postJson:String): String {
        val response = mclient.post("https://jsonplaceholder.typicode.com/posts")
        {
            contentType(ContentType.Application.Json)
            setBody(postJson)
            //setBody(TextContent(postJson,ContentType.Application.Json))
        }
        return response.bodyAsText();
    }
    suspend fun Demo_GetApi():String
    {

        val response =  mclient.get("https://api.restful-api.dev/objects")
        return  response.bodyAsText()
    }
}