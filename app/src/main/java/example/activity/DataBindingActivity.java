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


    //根据你绑定的布局文件的名称去掉下划线后加上Binding，由系统自动生成。
    private ActivityDataBindingBinding binding;
    //不适合在复杂的界面和逻辑上使用,且出现错误不好定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        //2.绑定基本数据类型及String
        binding.setContent("对String类型数据的绑定");//setContent就是给布局文件text属性绑定的content设置值
        binding.setEnabled2(false);//设置enabled值为false
        binding.mainTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //3.绑定model对象数据
        User user = new User("绑定Model数据类型","sdsd");
        binding.setUser(user);

        binding.setVariable(BR.enabled2, true);

        binding.setTitle1("click1");
        binding.setTitle2("click2");
        binding.setTitle3("click3");
        binding.setTitle4("title4传给click3");

        binding.setEvent(new EventListener() {
            @Override
            public void click1(View v) {
                binding.setTitle1("title1改变");

            }

            @Override
            public void click2(View v) {
                binding.setTitle2("title2改变");

            }

            @Override
            public void click3(String s) {
                binding.setTitle3(s);

            }
        });
    }
}
