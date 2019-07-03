package handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Zhengjingfeng
 * created 2019/7/3 11:01
 * comment
 */
public class HttpClient {

    /**
     * 服务器的IP
     */
    private final String host;
    /**
     * 服务器端口号
     */
    private final int port;

    public HttpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            String host = args[0];
            int port = Integer.valueOf(args[1]);
            new HttpClient(host, port).start();
        }
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            //.指定 EventLoopGroup 来处理客户端事件。由于我们使用 NIO 传输，所以用到了 NioEventLoopGroup 的实现
            bootstrap.group(group)
                    //使用的 channel 类型是一个用于 NIO 传输
                    .channel(NioSocketChannel.class)
                    //设置服务器的 InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host, port))
                    //当建立一个连接和一个新的通道时，创建添加到 HttpClientHandler 实例到 channel pipeline
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpClientHandler());

                        }
                    });
            //连接到远程;等待连接完成
            ChannelFuture future = bootstrap.connect().sync();
            //阻塞直到 Channel 关闭
            future.channel().closeFuture().sync();
        } finally {
            //调用 shutdownGracefully() 来关闭线程池和释放所有资源
            group.shutdownGracefully().sync();
        }
    }

}
