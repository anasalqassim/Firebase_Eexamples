package com.tuwaiq.firebaseeexamples.registerFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.tuwaiq.firebaseeexamples.R

private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {

    private lateinit var usernameET:EditText
    private lateinit var emailET:EditText
    private lateinit var passwordET:EditText
    private lateinit var confirmPasswordET:EditText
    private lateinit var submitBtn:Button

    private lateinit var auth: FirebaseAuth

    private val viewModel: RegisterViewModel by lazy { ViewModelProvider(this)[RegisterViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            Log.d(TAG , "hi ${currentUser.displayName}")
        }


        submitBtn.setOnClickListener {
            val username:String = usernameET.text.toString()
            val email:String = emailET.text.toString()
            val password:String = passwordET.text.toString()
            val confirmPassword = confirmPasswordET.text.toString()


            when{
                username.isEmpty() -> showToast("plz enter a username")
                email.isEmpty() -> showToast("plz enter an email")
                password.isEmpty() -> showToast("plz enter password")
                password != confirmPassword -> showToast("passwords must be matched" )
                else ->{
                    registerUser(username,email,password)
                }
            }
        }

    }

    private fun registerUser(username:String,email: String, password: String) {
         auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    showToast("good job")
                }else{
                    Log.e(TAG , "there was something wrong",task.exception)
                }

            }
        val updateProfile = userProfileChangeRequest{
            displayName = username
        }

        auth.currentUser?.updateProfile(updateProfile)

    }

    private fun showToast(msg:String){
        Toast.makeText( requireContext(), msg  ,Toast.LENGTH_LONG).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.register_fragment, container, false)
        init(v)
        return v
    }
    private fun init(v: View) {

        usernameET = v.findViewById(R.id.username_et)
        emailET = v.findViewById(R.id.email_et)
        passwordET = v.findViewById(R.id.password_et)
        confirmPasswordET = v.findViewById(R.id.confierm_password_et)
        submitBtn = v.findViewById(R.id.submit_btn)
    }




}