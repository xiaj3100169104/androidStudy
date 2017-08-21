package example.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.style.bean.User;
import com.style.framework.BR;
import com.style.framework.R;
import com.style.framework.databinding.ActivityDataBindingBinding;

public class DataBindingActivity extends AppCompatActivity {

    private ActivityDataBindingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        //2.绑定基本数据类型及String
        binding.setContent("对String类型数据的绑定");//setContent就是给布局文件text属性绑定的content设置值
        binding.setEnabled(false);//设置enabled值为false
        //给控件设置点击事件，发现其实点击无效，因为在布局文件中给cilckable属性绑定了enabled,在代码中设置enabled值为false，所以点击事件无效

        binding.mainTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //3.绑定model对象数据
        User user = new User("绑定Model数据类型","sdsd");
        binding.setUser(user);
        binding.setVariable(BR.enabled, true);

        binding.setTitle1("事件绑定1");
        binding.setTitle2("事件绑定2");
        binding.setTitle3("事件绑定3");
        binding.setTitle4("change ok");

        binding.setEvent(new EventListener() {
            @Override
            public void click1(View v) {
                binding.setTitle1("事件1方法调用");

            }

            @Override
            public void click2(View v) {
                binding.setTitle2("事件2方法调用");

            }

            @Override
            public void click3(String s) {
                binding.setTitle3(s);

            }
        });
    }
}
