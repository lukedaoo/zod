package com.infamous.zod.eureka.server;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    private PeerAwareInstanceRegistry m_registry;

    @GetMapping(value = "/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Info>>> getApplications() {

        Map<String, List<Info>> map = new HashMap<>();
        m_registry.getApplications()
            .getRegisteredApplications()
            .stream()
            .flatMap((Function<Application, Stream<Application>>) Stream::of)
            .forEach(app -> {
                if (!map.containsKey(app.getName())) {
                    map.put(app.getName(), new ArrayList<>());
                }
                app.getInstances().forEach(instanceInfo -> map.get(app.getName()).add(new Info(instanceInfo)));
            });

        return ResponseEntity.ok(map);
    }
}


class Info {

    String m_host;
    int m_port;
    int m_securePort;

    public Info() {

    }

    public Info(InstanceInfo instanceInfo) {
        m_host = instanceInfo.getHostName();
        m_port = instanceInfo.getPort();
        m_securePort = instanceInfo.getSecurePort();
    }

    public String getHost() {
        return m_host;
    }

    public Info setHost(String host) {
        m_host = host;
        return this;
    }

    public int getPort() {
        return m_port;
    }

    public Info setPort(int port) {
        m_port = port;
        return this;
    }

    public int getSecurePort() {
        return m_securePort;
    }

    public Info setSecurePort(int securePort) {
        m_securePort = securePort;
        return this;
    }
}
