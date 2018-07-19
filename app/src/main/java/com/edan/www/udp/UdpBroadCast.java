package com.edan.www.udp;

import android.os.SystemClock;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 创建者     Zhangyu
 * 创建时间   2018/7/12 20:27
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author
 * 更新时间   $Date
 * 更新描述   ${TODO}
 */

public class UdpBroadCast extends Thread {
    MulticastSocket sender = null;
    DatagramPacket dj = null;
    InetAddress group = null;
    private int num = 0;

    byte[] data = new byte[1024];
    private String mDataString;

    public UdpBroadCast(String dataString) {
        data = dataString.getBytes();
        mDataString = dataString;
    }

    @Override
    public void run() {

        while (true) {
            try {
                sender = new MulticastSocket();
                group = InetAddress.getByName("255.255.255.255");
                String message = String.valueOf(num);
                dj = new DatagramPacket(message.getBytes(),message.getBytes().length, group, 6789);
                sender.send(dj);
                sender.close();
                SystemClock.sleep(1000);
                num ++ ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
