package test.bean;

/**
 * Created by xiajun on 2016/10/8.
 */
public class TestBean {
    private String name;
    private String phone;

    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
