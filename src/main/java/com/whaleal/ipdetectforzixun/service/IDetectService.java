package com.whaleal.ipdetectforzixun.service;

/**
 * @author lyz
 * @desc
 * @create: 2023-10-07 11:37
 **/
public interface IDetectService {

    Boolean ping(String ip);

    Boolean telnet(String ipAddress, int port);
}
