package com.edan.www.udp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    Button startBroadCast;
    Button stopBroadCast;

    EditText sendTextView;
    TextView receiveTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* start 按钮 和 stop 按钮 的 初始化 */
        startBroadCast = (Button) findViewById(R.id.start);
        stopBroadCast = (Button) findViewById(R.id.stop);

        sendTextView = (EditText) findViewById(R.id.send_information);
        receiveTextView = (TextView) findViewById(R.id.receive_information);

        sendTextView.append("\n\n");
        receiveTextView.append("\n\n");

        startBroadCast.setOnClickListener(listener);
        stopBroadCast.setOnClickListener(listener);


        Handler handlerForUdpReceiveAndtcpSend = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                /* 0x111: 在TextView "sendTextView" 上, append 发送tcp连接的信息 */
                if (msg.what == 0x111) {
                    sendTextView.setText((msg.obj).toString());
                }
                /* 0x222: 在TextView上 "receiveTextView" 加上收到tcp连接的信息, udp多播的信息 */
                else if (msg.what == 0x222) {
                    receiveTextView.setText((msg.obj).toString());
                }
            }
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        /*接收udp多播 并 发送tcp 连接*/
        UdpReceiveAndSend udpReceiveAndSend = new UdpReceiveAndSend(handlerForUdpReceiveAndtcpSend,this);
        executorService.execute(udpReceiveAndSend);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == startBroadCast) {
                /* 发送 udp 广播 */
                new UdpBroadCast(sendTextView.getText().toString()).start();

            } else {
                startBroadCast.setEnabled(true);
                stopBroadCast.setEnabled(false);
            }
        }
    };

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

}
