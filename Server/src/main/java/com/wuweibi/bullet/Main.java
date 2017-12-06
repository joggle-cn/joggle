//package com.wuweibi.bullet;/**
// * Created by marker on 2017/11/19.
// */
//
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.webapp.WebAppContext;
//
///**
// *
// * 启动实例
// *
// * @author marker
// * @create 2017-11-19 下午4:33
// **/
//public class Main {
//
//
//    public static void main(String[] args) {
//        Server server = new Server(8080);
//
//        // 关联一个已经存在的上下文
//        WebAppContext context = new WebAppContext();
//        // 设置描述符位置
//        context.setDescriptor("./src/main/webapp/WEB-INF/web.xml");
//        // 设置Web内容上下文路径
//        context.setResourceBase("./src/main/webapp");
//        // 设置上下文路径
//        context.setContextPath("/");
//        context.setParentLoaderPriority(true);
//        server.setHandler(context);
//
//
//        try {
//            server.start();
//            // server.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("server is  start");
//
//
//
//    }
//
//}
