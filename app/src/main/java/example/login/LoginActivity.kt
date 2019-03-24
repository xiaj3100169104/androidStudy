package example.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.Observable
import android.os.Bundle
import android.view.View

import com.style.base.BaseTransparentStatusBarActivity
import com.style.entity.UserInfo
import com.style.framework.R
import com.style.framework.databinding.ActivityLoginBinding

import example.home.MainActivity

class LoginActivity : BaseTransparentStatusBarActivity() {

    private val userId: Long = 18
    private lateinit var bd: ActivityLoginBinding
    private lateinit var loginModel: LoginModel

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_login)
        bd = getBinding()
        loginModel = getViewModel(LoginModel::class.java)
        loginModel.loginSucceed.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, i: Int) {
                if (loginModel.loginSucceed.get()) {
                    startActivity(Intent(getContext(), MainActivity::class.java))
                    finish()
                }
            }
        })
        loginModel.user.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, i: Int) {
                setUserView(loginModel.user.get()!!)
            }
        })
        /*loginModel.loginState.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                startActivity(Intent(getContext(), MainActivity::class.java))
                finish()
            }
        })*/
        loginModel.loginState.observe(this, Observer<Boolean> { t ->
            if (t!!) {
                startActivity(Intent(getContext(), MainActivity::class.java))
                finish()
            } else {

            }
        })
       /* loginModel.loginState.observe(this, Observer<Boolean> {
            startActivity(Intent(getContext(), MainActivity::class.java))
            finish()
        })*/

        bd.btSignIn.setOnClickListener {}
        bd.btSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

            }
        })
        bd.btSignIn.setOnClickListener { view ->
            view.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
        }
        loginModel.login()
    }

    fun login(v: View) {
        val userId = bd.etAccount.text.toString()
        val password = bd.etPassword.text.toString()
        //mPresenter.login(userId, password);
    }

    fun setUserView(user: UserInfo) {
        bd.etAccount.setText(user.userName)
        bd.etPassword.setText(user.password)
    }

    fun loginSuccess() {

    }

    fun loginFailed() {

    }
}