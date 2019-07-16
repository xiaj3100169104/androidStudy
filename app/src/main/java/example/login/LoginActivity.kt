package example.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.Observable
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View

import com.style.base.activity.BaseTransparentStatusBarActivity
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
        val digits = getContext().getString(R.string.digits_password)
        bd.etPassword.keyListener = DigitsKeyListener.getInstance(digits)
        //keyListener与inputType有冲突，先设置的会被覆盖
        //bd.etPassword.inputType = InputType.TYPE_CLASS_TEXT
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
                login()
            }
        })
        bd.btSignIn.setOnClickListener { view ->
            view.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
        }
        login()
    }

    fun login() {
        val userId = bd.etAccount.text.toString()
        val password = bd.etPassword.text.toString()
        //mPresenter.login(userId, password);
        loginModel.login()
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