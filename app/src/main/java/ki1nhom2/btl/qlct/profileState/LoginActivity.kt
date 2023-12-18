package ki1nhom2.btl.qlct.profileState

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ki1nhom2.btl.qlct.MainActivity
import ki1nhom2.btl.qlct.R
import ki1nhom2.btl.qlct.homeState.HomeStateActivity
import ki1nhom2.btl.qlct.profileState.Data.CallDataInterface
import ki1nhom2.btl.qlct.profileState.Data.User

class LoginActivity : AppCompatActivity() {
    private lateinit var userList: MutableList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        tvRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        val imgLogin = findViewById<ImageView>(R.id.imgLogin)
        imgLogin.setOnClickListener {
            val edtEmail = findViewById<EditText>(R.id.edtEmail)
            val edtPassword = findViewById<EditText>(R.id.edtPassword)
            getData(edtEmail.text.toString(), edtPassword.text.toString())
        }


    }

    private fun getData(email:String, pass:String) {
        userList = mutableListOf()
        val dbRef = FirebaseDatabase.getInstance().getReference("User")
        var isLoginSuccessful = false

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()) {
                    for (value in snapshot.children) {
                        val account = value.getValue(User::class.java)
                        userList.add(account!!)

                        for (value in snapshot.children) {
                            val account = value.getValue(User::class.java)
                            userList.add(account!!)
                            for(i in userList) {
                                if (i.email.toString() == email && i.password.toString() == pass) {
//                                    isLoginSuccessful = true
                                    var intent = Intent(this@LoginActivity, HomeStateActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(this@LoginActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                    break
                                }
                            }
//                            if(!isLoginSuccessful) {
//                                Toast.makeText(this@LoginActivity, "Đăng nhập ko thành công", Toast.LENGTH_SHORT).show()
//                            }
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}