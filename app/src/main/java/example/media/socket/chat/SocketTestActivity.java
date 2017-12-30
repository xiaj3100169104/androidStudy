package example.media.socket.chat;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.style.framework.R;
import com.style.framework.databinding.ActivitySocketTestBinding;

public class SocketTestActivity extends AppCompatActivity {

    private ChatSocketClient chatSocketClient;
    private ActivitySocketTestBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_socket_test);
        bd.btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFile();
            }
        });
        bd.btDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });
        bd.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        bd.btSendUdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUdp();
            }
        });
        //SocketChatManager.getInstance().init();
        //chatSocketClient = new ChatSocketClient();
        UDPChat.init();
    }

    private void sendUdp() {

        UDPChat.send(bd.etMsg.getText().toString());
    }

    private void send() {
        String msg = bd.etMsg.getText().toString();
        SocketUtil.sendMsg(msg);
        //chatSocketClient.sendMsg(msg);
    }

    private void sendFile() {
        //String path = Environment.getExternalStorageDirectory() + "/aatest/test.mp4";
        String path = Environment.getExternalStorageDirectory() + "/aatest/test.jpeg";
        Log.e("path", path);
        SocketUtil.sendFile(path);
    }

    private void disconnect() {
        //SocketChatManager.getInstance().close();
    }


}
