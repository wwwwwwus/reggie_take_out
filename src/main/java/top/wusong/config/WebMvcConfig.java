package top.wusong.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import top.wusong.common.JacksonObjectMapper;

import java.util.List;

//表示该类为配置类
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    //放行一些静态资源文件
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //遇到该路径下的请求都放行到后面类路径下的请求
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
    }

    //添加拦截器
    @Bean
    public MybatisPlusInterceptor interceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //添加分页拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    //消息转换器，把前后端交互的信息做转换
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建转换器对象
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        //放到转换器容器当中，放到最前的位置
        converters.add(0,mappingJackson2HttpMessageConverter);
    }
}
