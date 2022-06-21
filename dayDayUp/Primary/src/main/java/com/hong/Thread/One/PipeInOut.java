package com.hong.Thread.One;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * @author wanghong
 * @date 2022/6/7
 * @apiNote 管道输入输出流 keyboard键入
 */
public class PipeInOut {
    public static void main(String[] args) throws IOException {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        //将输入输出流连接起来 不然会报错
        out.connect(in);
        Thread printThread = new Thread(new Print(in), "PrintThread");
        printThread.start();
        int receive = 0;
        try {
            while ((receive = System.in.read()) != -1){
                out.write(receive);
            }
        } finally {
            out.close();
        }
    }

    static class Print implements Runnable{
        private PipedReader in;
        public Print(PipedReader in){this.in = in;}

        @Override
        public void run() {
            int receive = 0;
            try {
                while ((receive = in.read()) != -1){
                    System.out.println((char)receive);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
