package trans;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author 失了秩
 * @date 2020/6/2 20:31
 * @description 以前使用JDK的API进行网络编程的“美妙”乐趣
 *              这段程序可以支撑小连接，但是并发量一起来就撑不住了
 *              所以我们尝试去写一段NIO代码
 */

public class PlainOioServer {
    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(2333);
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(()->{
                    OutputStream outputStream;
                    try {
                        outputStream = clientSocket.getOutputStream();
                        outputStream.write("HI!".getBytes(Charset.forName("UTF-8")));
                        outputStream.flush();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
