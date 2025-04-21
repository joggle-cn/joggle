package com.wuweibi.bullet.protocol.domain;

import lombok.Data;

/**
 * kscan 扫描结果数据结构
 *
 */
@Data
public class KscanResult {

    private String Digest;
    private String IP;
    private String Info;
    private String Keyword;
    private String Length;
    private String MatchRegexString;
    private String OperatingSystem;
    private String Port;
    private String ProbeName;
    private String ProductName;
    private String Response;
    private String Service;
    private String URL;
    private String Version;
    private String FingerPrint;
}


// kscan扫描结果json例子
//"Digest": "\"SSH-2.0-OpenSSH_8.9p1 Ub\"",
//"IP": "192.168.1.6",
//"Info": "Ubuntu Linux; protocol 2.0",
//"Keyword": "ssh",
//"Length": "42",
//"MatchRegexString": "^SSH-([\\d.]+)-OpenSSH_([\\w._-]+)[ -]{1,2}Ubuntu[ -_]([^\\r\\n]+)\\r?\\n",
//"OperatingSystem": "Linux",
//"Port": "22",
//"ProbeName": "TCP_NULL",
//"ProductName": "OpenSSH",
//"Response": "SSH-2.0-OpenSSH_8.9p1 Ubuntu-3ubuntu0.11\r\n",
//"Service": "ssh",
//"URL": "ssh://192.168.1.6:22",
//"Version": "8.9p1 Ubuntu 3ubuntu0.11"


