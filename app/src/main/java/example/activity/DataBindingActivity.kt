package example.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.style.bean.User;
import com.style.framework.BR;
import com.style.framework.R;
import com.style.framework.databinding.ActivityDataBindingBinding;


public class DataBindingActivity : AppCompatActivity() {

    private lateinit var bd: ActivityDataBindingBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        //2.绑定基本数据类型及String
        bd.tv1.setText("gergserger");
        bd.setContent("对String类型数据的绑定");//setContent就是给布局文件text属性绑定的content设置值
        bd.setEnabled2(false);//设置enabled值为false
        bd.mainTv1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

            }
        });
        //3.绑定model对象数据
        var user = User("绑定Model数据类型", "sdsd");
        bd.setUser(user);

        //bd.setVariable(BR.enabled2, true);

        bd.setTitle1("click1");
        bd.setTitle2("click2");
        bd.setTitle3("click3");
        bd.setTitle4("title4传给click3");

        bd.setEvent(object : EventListener {

            override fun click1(v: View) {
                bd.setTitle1("事件1方法调用");
            }

            override fun click2(v: View) {
                bd.setTitle2("事件2方法调用");
            }

            override fun cilck3(s: String) {
                bd.setTitle3(s);
            }
        });

    }

    /* public class EventListener {
         public void click1(View v) {
             bd.setTitle1("title1改变");

         }

         public void click2(View v) {
             bd.setTitle2("title2改变");

         }

         public void click3(String s) {
             bd.setTitle3(s);

         }
     }*/

    public interface EventListener {
        public fun click1(v: View);
        public fun click2(v: View);
        public fun cilck3(s: String);
    }
}
