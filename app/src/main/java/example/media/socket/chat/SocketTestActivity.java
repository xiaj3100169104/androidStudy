package example.media.socket.chat;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.style.framework.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SocketTestActivity extends AppCompatActivity {

    @Bind(R.id.bt_connect)
    Button btConnect;
    @Bind(R.id.bt_disconnect)
    Button btDisconnect;
    @Bind(R.id.et_msg)
    EditText etMsg;
    @Bind(R.id.bt_send)
    Button btSend;
    @Bind(R.id.bt_send_udp)
    Button btSendUdp;
    private ChatSocketClient chatSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_test);
        ButterKnife.bind(this);
        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFile();
            }
        });
        btDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        btSendUdp.setOnClickListener(new View.OnClickListener() {
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

        UDPChat.send(etMsg.getText().toString());
    }

    private void send() {
        String msg = etMsg.getText().toString();
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
