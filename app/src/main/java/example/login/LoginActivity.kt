package example.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ViewDataBinding
import android.view.View

import com.style.base.BaseActivity
import com.style.bean.User
import com.style.framework.R
import com.style.framework.databinding.ActivityLoginBinding

import example.home.MainActivity

class LoginActivity : BaseActivity() {

    private val userId: Long = 18
    private lateinit var bd: ActivityLoginBinding
    private lateinit var loginModel: LoginModel


    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun isGeneralTitleBar(): Boolean {
        return false
    }

    public override fun initData() {
        bd = getBinding()
        loginModel = getViewModel(LoginModel::class.java)
        loginModel.loginSucceed.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, i: Int) {
                if (loginModel.loginSucceed.get()) {
                    startActivity(Intent(context, MainActivity::class.java))
                    finish()
                }
            }
        })
        loginModel.user.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, i: Int) {
                setUserView(loginModel.user.get())
            }
        })
        loginModel.loginState.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                startActivity(Intent(context, MainActivity::class.java))
                finish()
            }
        })
        loginModel.loginState.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    startActivity(Intent(context, MainActivity::class.java))
                    finish()
                } else{

                }
            }
        })
        loginModel.loginState.observe(this, Observer<Boolean> {
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        })

        bd.btSignIn.setOnClickListener { }
        bd.btSignIn.setOnClickListener({})
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

    fun setUserView(user: User) {
        bd.etAccount.setText(user.userName)
        bd.etPassword.setText(user.password)
    }

    fun loginSuccess() {

    }

    fun loginFailed() {

    }
}