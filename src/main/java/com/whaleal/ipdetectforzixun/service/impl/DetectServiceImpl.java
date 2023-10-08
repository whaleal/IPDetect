package com.whaleal.ipdetectforzixun.service.impl;

import com.whaleal.ipdetectforzixun.controller.DetectController;
import com.whaleal.ipdetectforzixun.service.IDetectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author lyz
 * @desc
 * @create: 2023-10-07 11:38
 **/

@Service
public class DetectServiceImpl implements IDetectService {

    private static final Logger log = LogManager.getLogger(DetectController.class);

    @Override
    public Boolean ping(String ip) {
        Boolean canPing = false;
        try {
            InetAddress address = InetAddress.getByName(ip);
            canPing = address.isReachable(1000);
        } catch (IOException e) {
            log.error("在ping {}时出现了异常{}",ip,e.getMessage());
        }
        return canPing;
    }

    @Override
    public Boolean telnet(String ipAddress, int port) {
        Boolean canTelnet = false;
        try {
            Socket server = new Socket();
            InetSocketAddress address = new InetSocketAddress(ipAddress,
                    port);
            server.connect(address, 1000);
            server.close();
            canTelnet = true;
        } catch (IOException e) {
            log.error("在telnet {}:{} 时出现了异常{}",ipAddress,port,e.getMessage());
        }
        return canTelnet;
    }


}
