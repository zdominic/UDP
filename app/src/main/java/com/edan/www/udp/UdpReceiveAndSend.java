package com.edan.www.udp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 创建者     Zhangyu
 * 创建时间   2018/7/12 20:28
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author
 * 更新时间   $Date
 * 更新描述   ${TODO}
 */

public class UdpReceiveAndSend extends Thread {

    private final Context context;
    MulticastSocket ms = null;
    DatagramPacket dp;
    Handler handler = new Handler();

    public UdpReceiveAndSend(Handler handler, MainActivity mainActivity) {
        this.handler = handler;
        this.context = mainActivity;
    }

    @Override
    public void run() {
        Message msg;
        String information;

        byte[] data = new byte[1024];
        try {
            InetAddress groupAddress = InetAddress.getByName("255.255.255.255");
            ms = new MulticastSocket(6789);
            ms.joinGroup(groupAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            Log.e("aaa", "run 接收");
            try {
                dp = new DatagramPacket(data, data.length);
                if (ms != null)
                    ms.receive(dp);
                final String codeString = new String(data, 0, dp.getLength());

                msg = new Message();
                msg.what = 0x222;
                information = "收到来自: \n" + "\n" + "的udp请求\n"
                        + "请求内容: " + codeString + "\n\n";
                msg.obj = information;
                handler.sendMessage(msg);
            } catch (Exception e) {
                Log.e("aaa", "run "+e.getMessage());
                e.printStackTrace();
            }

            if (dp.getAddress() != null) {
                final String quest_ip = dp.getAddress().toString();

                String host_ip = getLocalIpAddress(context);

                Log.e("aaa", "host_ip:  --------------------  " + host_ip);
                Log.e("aaa", "quest_ip:  --------------------  " + quest_ip.substring(1));

                /* 若udp包的ip地址 是 本机的ip地址的话，丢掉这个包(不处理)*/

                if ((!host_ip.equals("")) && host_ip.equals(quest_ip.substring(1))) {
                    Log.e("aaa", "run sdasd");
                    continue;
                }
            }
        }

    }

    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
        // return null;
    }

    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
}
