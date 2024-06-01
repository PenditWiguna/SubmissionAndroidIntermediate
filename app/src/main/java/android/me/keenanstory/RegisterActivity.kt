package android.me.keenanstory

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.me.keenanstory.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edRegisterEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val email = editable.toString()
                if (viewModel.isEmailValid(email)) {
                    binding.tvEmailAlert.text = ""
                } else {
                    binding.tvEmailAlert.text = "Email salah"
                }
            }

        })
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edRegisterPassword.alertCode == 1) {
                    binding.tvPasswordAlert.text = binding.edRegisterPassword.alertMsg
                    binding.tvPasswordAlert.visibility = TextView.VISIBLE
                } else {
                    binding.tvPasswordAlert.visibility = TextView.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.edRegisterPasswordConfirmation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (binding.edRegisterPasswordConfirmation.alertCode == 1) {
                    binding.tvPassword2Alert.text = binding.edRegisterPasswordConfirmation.alertMsg
                    binding.tvPassword2Alert.visibility = TextView.VISIBLE
                } else {
                    if (binding.edRegisterPassword.text.toString() != password) {
                        binding.tvPassword2Alert.text = "Password tidak sama"
                        binding.tvPassword2Alert.visibility = View.VISIBLE
                    } else {
                        binding.tvPassword2Alert.text = "Password sama"
                        binding.tvPassword2Alert.visibility = View.INVISIBLE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val passwordConfirmation = binding.edRegisterPasswordConfirmation.text.toString()

            if (password == passwordConfirmation) {
                binding.tvPasswordAlert.visibility = TextView.INVISIBLE
                binding.tvPassword2Alert.visibility = TextView.INVISIBLE
                viewModel.registerUser(name, email, password)
                viewModel.isSuccess.observe(this) { isSuccess ->
                    showRegisterResponse(isSuccess)
                }
            }
        }
    }

    private fun showRegisterResponse(isSuccess: Boolean) {
        if (isSuccess) {
            binding.tvPassword2Alert.apply {
                visibility = View.VISIBLE
                setTextColor(Color.BLUE)
                text = "Pendaftaran berhasil"
            }
        } else {
            binding.tvPassword2Alert.apply {
                visibility = View.INVISIBLE
                setTextColor(Color.RED)
                text = "Gagal melakukan pendaftaran"
            }
        }
    }
}