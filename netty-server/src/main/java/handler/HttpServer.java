package handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Zhengjingfeng
 * created 2019/7/3 10:32
 * comment
 */
public class HttpServer {

    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            int port = Integer.valueOf(args[0]);
            new HttpServer(port).start();
        }
    }


    public void start() throws Exception {
        //创建 EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {

            //创建 ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    //指定使用 NIO 的传输 Channel
                    .channel(NioServerSocketChannel.class)
                    // 设置 socket 地址使用所选的端口
                    .localAddress(new InetSocketAddress(port))
                    //添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerHandler());
                        }
                    });
            //绑定的服务器;sync等待服务器关闭
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(HttpServer.class.getName() + " started and listen on " + future.channel().localAddress());
            //关闭 channel 和 块，直到它被关闭
            future.channel().closeFuture().sync();
        } finally {
            //关闭 EventLoopGroup，释放所有资源。
            group.shutdownGracefully().sync();
        }
    }
}
