package com.bangkit.intermediate.ceritaku.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.intermediate.ceritaku.components.EmailCustomView
import com.bangkit.intermediate.ceritaku.components.PasswordCustomView
import com.bangkit.intermediate.ceritaku.databinding.ActivityLoginBinding
import com.bangkit.intermediate.ceritaku.ui.home.HomeActivity
import com.bangkit.intermediate.ceritaku.ui.viewModels.AuthViewModel
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import com.bangkit.intermediate.ceritaku.utils.ToastUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var passwordEditText: PasswordCustomView
    private lateinit var emailEditText: EmailCustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupTextChangeListeners()
        setupView()
        playAnimation()
        setupButtons()
    }

    private fun initializeViews() {
        passwordEditText = binding.edLoginPassword
        emailEditText = binding.edLoginEmail
    }


    private fun setupTextChangeListeners() {
        val textChangeListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        emailEditText.addTextChangedListener(textChangeListener)
        passwordEditText.addTextChangedListener(textChangeListener)
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.edLoginEmailLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.edLoginPasswordLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, login)
            startDelay = 100
        }.start()
    }

    private fun setupButtons() {
        binding.apply {
            btnLogin.setOnClickListener { login() }
            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
        setMyButtonState()
    }

    private fun setMyButtonState() {
        val resultEmail = emailEditText.text.toString().isNotEmpty()
        val resultPassword = passwordEditText.text.toString().isNotEmpty()
        binding.btnLogin.isEnabled = resultEmail && resultPassword
    }

    private fun login() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        authViewModel.login(email, password)
        authViewModel.loginResult.observe(this) { response ->
            when (response) {
                is ApiResult.Loading -> binding.progressBar.visibility = View.VISIBLE
                is ApiResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    ToastUtils.successToast("Login Success", this)
                    finish()
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                is ApiResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    ToastUtils.errorToast("Periksa Kembali Email atau Password", this)
                }
            }
        }
    }
}
