package com.example.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TableLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.api.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    lateinit var adapter: ArticlesAdapter
    lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initRecyclerView()
        getDataFromApi()

    }

    private fun initRecyclerView() {
        adapter = ArticlesAdapter()
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        articlesRecyclerView.adapter = adapter
        articlesRecyclerView.layoutManager = layoutManager

    }

    private fun getDataFromApi() {
        ApiManager
            .getWebServices()
            .getSources()
            .enqueue(object : Callback<ResourcesResponse> {
                override fun onResponse(
                    call: Call<ResourcesResponse>,
                    response: Response<ResourcesResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status.equals("ok")) {
//                        Log.e("data response", "data response: " + response.body()?.sources)
                        setupTabs(response.body()?.sources)
                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            response.body()?.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }

                override fun onFailure(call: Call<ResourcesResponse>, t: Throwable) {
                    Toast.makeText(this@HomeActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun setupTabs(sources: List<SourcesItem?>?) {
        sources?.forEach { item ->
            val tab = sourcesTabLayout.newTab()
            tab.text = item?.name
            tab.tag = item
            sourcesTabLayout.addTab(tab)

        }

        sourcesTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val item = tab?.tag as SourcesItem
                getArticles(item.id)

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val item = tab?.tag as SourcesItem
                getArticles(item.id)
            }
        })
        val tab = sourcesTabLayout.getTabAt(0)
        tab?.select()
    }

    fun getArticles(articleSourceId: String?) {
        articleSourceId?.let {
            ApiManager.getWebServices()
                .getArticles(it)
                .enqueue(object : Callback<ArticlesResponse> {
                    override fun onResponse(
                        call: Call<ArticlesResponse>,
                        response: Response<ArticlesResponse>
                    ) {
                        progressBar.visibility = View.GONE

                        if (response.isSuccessful && response.body()?.status.equals("ok")) {
                            adapter.getData(response.body()?.articles)
                            Log.e("onResponse", "onResponse: " + response.body()?.articles)
                        } else {
                            Toast.makeText(
                                this@HomeActivity,
                                response.body()?.message.toString(),
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }

                    }

                    override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE

                        Toast.makeText(this@HomeActivity, t.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }
    }
}