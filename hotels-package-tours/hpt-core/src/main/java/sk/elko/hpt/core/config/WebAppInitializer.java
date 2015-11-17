package sk.elko.hpt.core.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        WebApplicationContext rootContext = createRootContext(servletContext);

        configureSpringServlets(servletContext, rootContext);
    }

    private WebApplicationContext createRootContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(CoreConfig.class, RuntimeConfig.class);
        rootContext.refresh();

        servletContext.addListener(new ContextLoaderListener(rootContext));
        servletContext.setInitParameter("defaultHtmlEscape", "true");

        return rootContext;
    }

    private void configureSpringServlets(ServletContext servletContext, WebApplicationContext rootContext) {

        AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
        mvcContext.register(MVCConfig.class, ThymeleafConfig.class);
        mvcContext.setParent(rootContext);
        ServletRegistration.Dynamic mvcServlet = servletContext.addServlet("mvc", new DispatcherServlet(mvcContext));
        mvcServlet.setLoadOnStartup(1);
        mvcServlet.addMapping("/ui/*");

        AnnotationConfigWebApplicationContext wsContext = new AnnotationConfigWebApplicationContext();
        wsContext.register(WsConfig.class);
        wsContext.setParent(rootContext);
        ServletRegistration.Dynamic wsServlet = servletContext.addServlet("webservice", new MessageDispatcherServlet(
                wsContext));
        wsServlet.setInitParameter("transformWsdlLocations", "true");
        wsServlet.setLoadOnStartup(1);
        wsServlet.addMapping("/services/*");

    }

}
