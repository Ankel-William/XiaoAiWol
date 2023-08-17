package com.ankel.xiaoaiwol.bafa;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.validation.annotation.Validated;

@Configuration
@EnableIntegration
public class BaichuanIOTConfig {

    private static final String[] MQTT_BROKER = {"ssl://bemfa.com:9503"};
    @Value("${bafacloud.private_key}")
    private String CLIENT_ID ;
    @Value("${bafacloud.topic}")
    private String TOPIC;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        System.out.println("CLIENT_ID"+CLIENT_ID+"========"+"TOPIC"+TOPIC);
        if (CLIENT_ID==null||TOPIC==null){
            System.out.println("未获取到配置文件，请检查yaml信息是否配置正确");
            System.exit(0);
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setServerURIs(MQTT_BROKER);
        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory(MqttConnectOptions mqttConnectOptions) {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow mqttInbound(BaichuanIOTMessageHandler messageHandler) {
        return IntegrationFlows.from(mqttMessageDrivenChannelAdapter())
                .handle(messageHandler)
                .get();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttMessageDrivenChannelAdapter() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(CLIENT_ID, mqttClientFactory(new MqttConnectOptions()));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        adapter.addTopic(TOPIC);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public BaichuanIOTMessageHandler messageHandler() {
        return new BaichuanIOTMessageHandler();
    }
}
