package com.hong.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wanghong
 * @date 2022/8/2
 * @apiNote 传统的bio
 */
public class TranditionalBIOdemo {
    public static void main(String[] args) {
        try {
            //创建一个新的ServerSocket用以监听指定端口上的连接请求
            ServerSocket serverSocket = new ServerSocket(10021);
            //accept()是一个阻塞的方法哦 记得吧
            Socket client = serverSocket.accept();
            //将读取的数据转化为流的形式
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            String request, response;
            //readLine()同样是一个阻塞方法
            while ((request = in.readLine())!= null) {
                if ("Done".equals(request)) break;
                //请求被传给服务器的处理方法
                response = processRequest(request);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String processRequest(String request) {

        return "ok";
    }
}
