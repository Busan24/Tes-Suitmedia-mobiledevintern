package com.example.palindromecheckerapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.palindromecheckerapp.adapter.UserAdapter
import com.example.palindromecheckerapp.api.ApiClient
import com.example.palindromecheckerapp.databinding.ActivityThirdScreenBinding
import com.example.palindromecheckerapp.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThirdScreen : AppCompatActivity() {

    private lateinit var binding: ActivityThirdScreenBinding
    private lateinit var userAdapter: UserAdapter

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSwipeRefresh()
        fetchUsers()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(mutableListOf()) { user ->
            // Kirim balik data ke SecondScreen
            val intent = Intent().apply {
                putExtra("selected_user", "${user.first_name} ${user.last_name}")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ThirdScreen)
            adapter = userAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            fetchUsers()
        }
    }

    private fun fetchUsers() {
        binding.swipeRefresh.isRefreshing = true
        coroutineScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getUsers(page = 1, perPage = 12)
                }

                userAdapter.clearUsers()
                userAdapter.addUsers(response.data)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Fetch failed", e)
                Toast.makeText(this@ThirdScreen, "Gagal memuat data: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
