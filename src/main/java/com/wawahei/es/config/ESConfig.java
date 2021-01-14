package com.wawahei.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description:
 * @author: yanghailang
 * @create: 2021-01-14 09:52
 **/
@Configuration
public class ESConfig {

    @Bean
    public TransportClient client() throws UnknownHostException {
        // 9300是es的tcp服务端口
        final String host = "127.0.0.1";
        final int port = 9300;
        InetSocketTransportAddress node = new InetSocketTransportAddress(InetAddress.getByName(host),port);

        //设置es节点的配置信息
        Settings settings = Settings.builder()
                            .put("cluster.name","elasticsearch").build();

        //实例化es的客户端对象
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);
        return client;
    }
}