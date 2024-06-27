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
import androidx.lifecycle.Observer
import com.bangkit.intermediate.ceritaku.components.EmailCustomView
import com.bangkit.intermediate.ceritaku.components.PasswordCustomView
import com.bangkit.intermediate.ceritaku.components.UsernameCustomView
import com.bangkit.intermediate.ceritaku.databinding.ActivityRegisterBinding
import com.bangkit.intermediate.ceritaku.ui.viewModels.AuthViewModel
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import com.bangkit.intermediate.ceritaku.utils.ToastUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var userNameText: UsernameCustomView
    private lateinit var passwordText: PasswordCustomView
    private lateinit var emailText: EmailCustomView
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupTextChangeListeners()
        setupView()
        playAnimation()
        setupButtons()
    }

    private fun initializeViews() {
        userNameText = binding.edRegisterName
        passwordText = binding.edRegisterPassword
        emailText = binding.edRegisterEmail
    }

    private fun setupTextChangeListeners() {
        val textChangeListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        userNameText.addTextChangedListener(textChangeListener)
        emailText.addTextChangedListener(textChangeListener)
        passwordText.addTextChangedListener(textChangeListener)
    }

    private fun playAnimation() {
        val animators = listOf(
            ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.edRegisterNameLayout, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.edRegisterEmailLayout, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.edRegisterPasswordLayout, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f)
        )

        AnimatorSet().apply {
            playSequentially(animators)
            startDelay = 100
        }.start()
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

    private fun setupButtons() {
        binding.apply {
            btnRegister.setOnClickListener {
                val name = userNameText.text.toString()
                val email = emailText.text.toString()
                val password = passwordText.text.toString()
                registerUser(name, email, password)
            }
            btnLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
        setMyButtonState()
    }

    private fun setMyButtonState() {
        val resultUsername = userNameText.text?.isNotEmpty()
        val resultEmail = emailText.text?.isNotEmpty()
        val resultPassword = passwordText.text?.isNotEmpty()
        binding.btnRegister.isEnabled = resultUsername!! && resultEmail!! && resultPassword!!
    }

    private fun registerUser(name: String, email: String, password: String) {
        authViewModel.register(name, email, password)
        authViewModel.registerResult.observe(this, Observer { response ->
            when (response) {
                is ApiResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ApiResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    ToastUtils.successToast("Register Success", this)
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
                is ApiResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    ToastUtils.errorToast("Email atau password sudah terdaftar", this)
                }
            }
        })
    }
}
