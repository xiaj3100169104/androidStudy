package example.newwork.response;


import com.style.entity.User;

import java.io.Serializable;

public class LoginBean implements Serializable {
    public String token;
    public User friend;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
