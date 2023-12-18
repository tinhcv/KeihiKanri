package ki1nhom2.btl.qlct.profileState

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListUtil.DataCallback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ki1nhom2.btl.qlct.R
import ki1nhom2.btl.qlct.profileState.Data.CallDataInterface
import ki1nhom2.btl.qlct.profileState.Data.User

class SignupActivity : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val imgLogin = findViewById<ImageView>(R.id.imgLogin)

        imgLogin.setOnClickListener {
            dbRef = FirebaseDatabase.getInstance().reference.child("User")
            val edtUsename = findViewById<EditText>(R.id.edtUsename)
            val edtEmail = findViewById<EditText>(R.id.edtEmail)
            val edtPassword = findViewById<EditText>(R.id.edtPassword)
            val username = edtUsename.text.toString()
            val password = edtPassword.text.toString()
            val email = edtEmail.text.toString()
            val userId = dbRef.push().key!!
            if (username.isEmpty()) {
                edtUsename.error = "Không được để trống tên đăng nhập!"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                edtEmail.error = "Không được để trống email!"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                edtPassword.error = "Không được để trống mật khẩu!"
                return@setOnClickListener
            }
            val user = User(userId ,username, password, email)
            dbRef.child(userId).setValue(user)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}