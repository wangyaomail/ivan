package cn.dendarii.ivan;

@SuppressWarnings("resource")
public class AppStarter {
    public static void main(String[] args) throws Exception {
        WebServer server = new WebServer();
        server.start();
    }
}
