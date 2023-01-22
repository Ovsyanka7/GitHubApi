package com.example.githubapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    var userInfo = UserInfo()
    var reposList: MutableList<Repository> = mutableListOf()
    var commitList: MutableList<Commit> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userName = "Ovsyanka7"

        // TODO: Сделать интерфейс поисковой строки более дружелюбным и интуитивно понятным.
        val svLoginName: SearchView = findViewById(R.id.svLoginName)
//        svLoginName.setOn {
//            Log.d("Debug", "HRUCK")
//        }



//        thread {
//            getUserInfo(userName)
//            getReposList()
////            reposList.forEach {
//            getCommitList("https://api.github.com/repos/Ovsyanka7/binlookup/commits")
////            }
//        }
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

    private fun getCommitList(commits_url: String) {
        val gitHubEndpoint = URL(commits_url)
        val connection: HttpsURLConnection = gitHubEndpoint.openConnection() as HttpsURLConnection

        if (connection.responseCode == 200) {
            commitList = MyJsonReader().readCommits(connection)
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
}
