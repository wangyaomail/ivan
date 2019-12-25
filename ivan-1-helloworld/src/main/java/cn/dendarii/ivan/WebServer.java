package cn.dendarii.ivan;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.jasper.servlet.JspServlet;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebServer implements AutoCloseable {
    public static Server server = null;

    public WebServer() {}

    private File getScratchDir() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "ivan_web_temp_dir");
        if (!scratchDir.exists()) {
            if (!scratchDir.mkdirs()) {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }
        return scratchDir;
    }

    private WebAppContext getWebAppContext(File scratchDir) {
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setAttribute("javax.servlet.context.tempdir", scratchDir);
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                             ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
        context.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        context.addBean(new ServletContainerInitializersStarter(context), true);
        context.setClassLoader(getUrlClassLoader());
        context.addServlet(jspServletHolder(), "*.jsp");
        return context;
    }

    private List<ContainerInitializer> jspInitializers() {
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
        initializers.add(initializer);
        return initializers;
    }

    private ClassLoader getUrlClassLoader() {
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0],
                                                        this.getClass().getClassLoader());
        return jspClassLoader;
    }

    private ServletHolder jspServletHolder() {
        ServletHolder holderJsp = new ServletHolder("jsp", JspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.8");
        holderJsp.setInitParameter("compilerSourceVM", "1.8");
        holderJsp.setInitParameter("keepgenerated", "true");
        return holderJsp;
    }

    public void start() throws Exception {
        if (server != null) {
            close();
        }
        server = new Server(8000);
        ServerConnector connector = new ServerConnector(server);
        connector.setName("main");
        connector.setHost("localhost");
        connector.setPort(8000);
        server.setConnectors(new Connector[] { connector });
        try {
            HandlerList handlerList = new HandlerList();
            WebAppContext context = getWebAppContext(getScratchDir());
            context.setResourceBase("webroot");
            context.setWelcomeFiles(new String[] { "/helloworld.jsp" });
            context.addServlet(TestAction.class, "/test");
            context.addServlet(HelloServlet.class, "/hello");
            handlerList.addHandler(context);
            server.setHandler(handlerList);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        server.stop();
    }
}
