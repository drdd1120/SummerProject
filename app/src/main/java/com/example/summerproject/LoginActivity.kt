package com.example.summerproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.summerproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth



private var firebaseAuth: FirebaseAuth? = null
var backKeyPressedTime : Long = 0

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(firebaseAuth!!.currentUser!=null && firebaseAuth!!.currentUser!!.isEmailVerified){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.goToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
/*
        binding.password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                binding.btnLogin.isEnabled =  binding.password.length() >= 7
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnLogin.isEnabled = false
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
*/
        binding.btnLogin.setOnClickListener {
            login(binding.email.text.toString(), binding.password.text.toString())
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if(System.currentTimeMillis() > backKeyPressedTime+2500){
            backKeyPressedTime = System.currentTimeMillis()
            return
        }

        if(System.currentTimeMillis() <= backKeyPressedTime+2500){
            finishAffinity()
        }
    }

    private fun login(email: String, password: String){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "모든 칸을 입력해주세요", Toast.LENGTH_SHORT).show()
        } else if(email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    val user= firebaseAuth!!.currentUser!!.isEmailVerified
                    if (user) {
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "로그인 실패, 이메일 인증을 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}