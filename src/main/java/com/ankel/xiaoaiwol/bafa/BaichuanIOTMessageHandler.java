package com.ankel.xiaoaiwol.bafa;

import com.ankel.xiaoaiwol.wol.WakeOnLan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@MessageEndpoint
public class BaichuanIOTMessageHandler {

    @Value("${wol.macaddresses}")
    String macAddresses;


    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        String payload = message.getPayload().toString();
        System.out.println("Received message: " + payload);
        if (Objects.equals(payload, "on")&&macAddresses!=null){
            List<String> macaddrList = new ArrayList<>(Arrays.asList(macAddresses.split(";")));
            macaddrList.forEach(mac->{
                System.out.println("MAC:"+mac);
                System.out.println(WakeOnLan.wakeUp(mac));
            });
        }else if (macAddresses==null){
            System.out.println("未获取到MAC地址，请检查配置文件");
        }
    }
}
