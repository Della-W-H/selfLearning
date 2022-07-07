package org.itstack.demo.design.door.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 切面定义
 * 公众号：bugstack虫洞栈 | 沉淀、分享、成长，让自己和他人都能有所收获！
 * 博  客：http://bugstack.cn
 * Create by 小傅哥 on @2020
 */
@Configuration
@ConditionalOnClass(StarterService.class) //这个注解意味着 在classPath类路径中 存在指定value的对象 加上这个注解的类才会被加载
// todo 换而言之 只有 其他模块中 引入此类模块构成的jar包 这个类就会被自动配置 因为此模块中包含StarterService.class 打成jar包被通过maven引入其他模块自然就存在于classPath路径之中了

@EnableConfigurationProperties(StarterServiceProperties.class)
public class StarterAutoConfigure {

    @Autowired
    private StarterServiceProperties properties;

    @Bean
    @ConditionalOnMissingBean //没有指定value或者其他 满足的注解属性 此处默认的即为StarterService 即spring容器中没有此类就会这个方法就会被调用
    @ConditionalOnProperty(prefix = "itstack.door", value = "enabled", havingValue = "true")
    StarterService starterService() {
        return new StarterService(properties.getUserStr());
    }

}