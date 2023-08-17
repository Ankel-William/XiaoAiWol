package com.ankel.xiaoaiwol.wol;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Component
public class WakeOnLan {

    public static String wakeUp(String macAddress) {
        if (macAddress==null)
         return "未提供MAC地址！广播失败";
        String ipAddress = "255.255.255.255";
        try {
            // 将MAC地址转换为字节数组
            byte[] macBytes = getMacBytes(macAddress);

            // 创建用于发送WOL包的数据报套接字
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            // 用于发送的数据包
            byte[] magicPacket = createMagicPacket(macBytes);
            DatagramPacket packet = new DatagramPacket(magicPacket, magicPacket.length, InetAddress.getByName(ipAddress), 9);

            // 发送数据包
            socket.send(packet);


        } catch (IOException e) {
            System.err.println("发送WOL包时出现错误：" + e.getMessage());
        }
        return "成功发送WOL包！";
    }

    // 将MAC地址字符串转换为字节数组
    private static byte[] getMacBytes(String macAddress) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hexDigits = macAddress.split("(\\:|\\-)");
        if (hexDigits.length != 6) {
            throw new IllegalArgumentException("无效的MAC地址");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hexDigits[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的MAC地址");
        }
        return bytes;
    }

    // 创建WOL包
    private static byte[] createMagicPacket(byte[] macBytes) {
        int packetSize = 102;
        byte[] magicPacket = new byte[packetSize];

        // WOL包的前6个字节为0xFF
        for (int i = 0; i < 6; i++) {
            magicPacket[i] = (byte) 0xFF;
        }

        // 之后将目标MAC地址重复16次
        for (int i = 6; i < packetSize; i += macBytes.length) {
            System.arraycopy(macBytes, 0, magicPacket, i, macBytes.length);
        }

        return magicPacket;
    }
}
