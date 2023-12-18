package ki1nhom2.btl.qlct.profileState

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import ki1nhom2.btl.qlct.R
import ki1nhom2.btl.qlct.MainActivity

class ProfileStateActivity : MainActivity() {

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_state)

        changeColor(4)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}