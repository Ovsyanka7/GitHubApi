package com.example.githubapi

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var userInfo = UserInfo()
    private var reposList: MutableList<Repository> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val svLoginName: SearchView = findViewById(R.id.svLoginName)
        svLoginName.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    thread {
                        getUserInfo(query)
                        getReposList()
                        runOnUiThread {
                            fillInTheInterface()
                            showRepositories()
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // TODO: Списочек последних запросов бы.
                return false
            }
        })
    }

    private fun getUserInfo(userName: String) {
        val gitHubEndpoint = URL("https://api.github.com/users/$userName")
        val connection: HttpsURLConnection = gitHubEndpoint.openConnection() as HttpsURLConnection

        if (connection.responseCode == 200) {
            userInfo = MyJsonReader().readUserInfo(connection)
        } else {
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "Нет подключения.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getReposList() {
        val gitHubEndpoint = URL(userInfo.repos_url.toString())
        val connection: HttpsURLConnection = gitHubEndpoint.openConnection() as HttpsURLConnection

        if (connection.responseCode == 200) {
            reposList = MyJsonReader().readRepositories(connection)
        } else {
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "Нет подключения.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fillInTheInterface() {
        val tvLogin: TextView = findViewById(R.id.tvLogin)
        val ivAvatar: ImageView = findViewById(R.id.ivAvatar)

        tvLogin.text = userInfo.login

        // Объявление исполнителя для анализа URL-адреса.
        val executor = Executors.newSingleThreadExecutor()

        // Как только исполнитель анализирует URL и получаем изображение, обработчик его загружает
        //  в ImageView.
        val handler = Handler(Looper.getMainLooper())

        // Инициализация образа.
        var image: Bitmap?

        // Только для фонового процесса (может занять время в зависимости от скорости интернета).
        executor.execute {
            // Пытается получить изображение и опубликовать его в ImageView.
            // с помощью обработчика
            try {
                val `in` = URL(userInfo.avatar_url).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Только для внесения изменений в интерфейс.
                handler.post {
                    ivAvatar.setImageBitmap(image)
                }
            }
            catch (_: Exception) { }
        }

    }

    private fun showRepositories() {
        val rvRepository: RecyclerView = findViewById(R.id.rvRepository)
        rvRepository.adapter = RepositoryRecyclerAdapter(reposList)
        rvRepository.layoutManager = GridLayoutManager(this, 1)
    }
}
