package trans;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 失了秩
 * @date 2020/6/2 20:49
 * @description 使用JDK的API来编写一个NIO服务器
 *              可以看到，使用NIO编程，比OIO代码多出了很多，如果每次都这么写那将会花费大量的时间
 */
public class PlainNioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 设置非阻塞
        serverChannel.configureBlocking(false);

        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket();
        InetSocketAddress address = new InetSocketAddress(2333);
        serverSocket.bind(address);

        //打开Selector来对Channel中的事件进行处理
        Selector selector = Selector.open();

        //将ServerSocket注册到Selector
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg =  ByteBuffer.wrap("HI!".getBytes(Charset.forName("UTF-8")));
        while (true) {
            //等待事件发生
            selector.select();
            Set<SelectionKey> readByKey = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readByKey.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel client = serverSocketChannel.accept();
                    //将客户端socket注册到Selector并监听事件
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                }
                //检查客户端是否做好了被写的准备
                if (key.isWritable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    while (byteBuffer.hasRemaining()) {
                        if (client.write(byteBuffer) == 0) {
                            break;
                        }
                    }
                    client.close();
                }
            }

        }

    }
}
