package top.wusong.filters;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import top.wusong.common.BaseContext;
import top.wusong.common.Result;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


//校验是否登录的拦截器
@Slf4j                //名称                拦截路径
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //spring提供的转换路径的类
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setCharacterEncoding("UTF-8");
        //讲request转型
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //看看都是拦截
        String requestURI = httpServletRequest.getRequestURI();

        //1、新建一个数组，里面是不需要拦截的，登录求情和退出登录的请求，以及静态资源
        String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
        };
        //2、调用方法，判断路径是否匹配的上
        boolean flag = checkUrl(urls, httpServletRequest.getRequestURI());
        //3、根据判断结果看看是否需要拦截
        if (flag){
            log.info("放行的请求{}"+requestURI);
            //直接放行
            filterChain.doFilter(httpServletRequest,servletResponse);
            //放行直接终止请求
            return;
        }

        //4、判断请求是否携带了登录信息
        if (httpServletRequest.getSession().getAttribute("id") != null){
            log.info("已登录的请求{}"+requestURI);
            //获取用户的id
            Long id = (Long) httpServletRequest.getSession().getAttribute("id");
            //放入线程当中，用在后面自动填充当中
            BaseContext.setEmployeeId(id);
            //放行
            filterChain.doFilter(servletRequest,servletResponse);
            //放行直接终止请求
            return;
        }

        log.info("未登录的请求{}"+requestURI);
        //5、都没有匹配上，说明就是没有登录,发送拦截登录的信息
        servletResponse.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }
    //判断路径是否不需要拦截
    private boolean checkUrl(String[] urls,String url){
        //遍历比较
        for (String u : urls) {
            //调用方法匹配
            if (PATH_MATCHER.match(u,url)){
                //匹配上了直接返回true
                return true;
            }
        }
        //遍历玩都没有匹配上
        return false;
    }
}
