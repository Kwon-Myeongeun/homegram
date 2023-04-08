package com.lovesme.homegram.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.UiState
import com.lovesme.homegram.databinding.ActivityMainBinding
import com.lovesme.homegram.ui.setting.SettingActivity
import com.lovesme.homegram.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var alertDialog: AlertDialog
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        mainViewModel.startSync()

        val navController =
            supportFragmentManager.findFragmentById(binding.homeFrmContainer.id)
                ?.findNavController()
        navController?.let { binding.homeBtmNavigation.setupWithNavController(it) }

        binding.homeToolbar.setOnMenuItemClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
            return@setOnMenuItemClickListener true
        }
    }

    private fun initData() {
        initDialog()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            alertDialog.show()
                        }
                        is UiState.Error -> {
                            alertDialog.dismiss()
                            Snackbar.make(
                                binding.root,
                                getString(R.string.load_data_fail),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            delay(300)
                            alertDialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun initDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_progress, null)
        alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
    }
}