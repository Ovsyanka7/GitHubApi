package com.example.githubapi

import android.util.JsonReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.net.ssl.HttpsURLConnection

// Вся логика расшифровки Json в объект Bin перенесена в этот класс.
class MyJsonReader {

    fun readUserInfo(connection: HttpsURLConnection): UserInfo {
        val userInfo = UserInfo()
        val responseBody: InputStream = connection.inputStream
        val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
        val jsonReader = JsonReader(responseBodyReader)

        jsonReader.beginObject()

        while (jsonReader.hasNext()) {
            when (jsonReader.nextName()) {
                "login" -> {
                    userInfo.login = jsonReader.nextString()
                }
                "id" -> {
                    userInfo.id = jsonReader.nextString()
                }
                "avatar_url" -> {
                    userInfo.avatar_url = jsonReader.nextString()
                }
                "html_url" -> {
                    userInfo.html_url = jsonReader.nextString()
                }
                "repos_url" -> {
                    userInfo.repos_url = jsonReader.nextString()
                }
                else -> {
                    jsonReader.skipValue()
                }
            }
        }

        jsonReader.close()
        connection.disconnect()
        return userInfo
    }

    fun readRepositories(connection: HttpsURLConnection): MutableList<Repository> {
        val reposList: MutableList<Repository> = mutableListOf()

        val responseBody: InputStream = connection.inputStream
        val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
        val jsonReader = JsonReader(responseBodyReader)

        jsonReader.beginArray()
        while (jsonReader.hasNext()) {
            jsonReader.beginObject()
            val repository = Repository()

            while (jsonReader.hasNext()) {
                when (jsonReader.nextName()) {
                    "name" -> {
                        repository.name = jsonReader.nextString()
                    }
                    "html_url" -> {
                        repository.html_url = jsonReader.nextString()
                    }
                    "created_at" -> {
                        repository.created_at = jsonReader.nextString()
                    }
                    "updated_at" -> {
                        repository.updated_at = jsonReader.nextString()
                    }
                    "pushed_at" -> {
                        repository.pushed_at = jsonReader.nextString()
                    }
                    "language" -> {
                        repository.language = jsonReader.nextString()
                    }
                    "commits_url" -> {
                        repository.commits_url = jsonReader.nextString().replace("{/sha}", "")
                    }
                    else -> {
                        jsonReader.skipValue()
                    }
                }
            }
            reposList.add(repository)
            jsonReader.endObject()
        }
        jsonReader.endArray()

        jsonReader.close()
        connection.disconnect()
        return reposList
    }
}