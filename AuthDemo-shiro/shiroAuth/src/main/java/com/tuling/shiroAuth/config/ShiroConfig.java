package com.tuling.shiroAuth.config;

import com.tuling.shiroAuth.utils.MyConstants;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //1、realm
//    @Bean
//    public Realm myRealm(){
//        return new MyRealm();
//    }
//    @Resource(name = "myRealm")
//    private Realm myRealm;
//    @Resource(name = "mobileRealm")
//    private Realm mobileRealm;

    //2、DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("myRealm") AuthorizingRealm myRealm, @Qualifier("mobileRealm") Realm mobileRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
//        securityManager.setRealm(myRealm);
        securityManager.setAuthenticator(getModularRealmAuthenticator());
//        securityManager.setAuthorizer();
        securityManager.setRealms(Arrays.asList(myRealm,mobileRealm));
        securityManager.setRememberMeManager(new CookieRememberMeManager());
        securityManager.setSessionManager(new DefaultWebSecurityManager());

        return securityManager;
    }

    //3、shiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        /*
        配合Shiro的内置过滤器,参见DefaultFilter
           anon : 无需认证就可以访问
           authc : 必须认证才可以访问 对应@RequiresAuthentication注解
           user : 用户登录且记住我 才可以访问 对应@RequiresUser注解
           perms : 拥有某个资源才可以访问 对应@RequiresPermissions注解
           roles : 拥有某个角色才可以访问  对应@RequiresRoles注解
         */
        Map<String, String> filterChainDefinition = new HashMap<>();
        //配置antMatcher，跟SpringSecurity一样，可以配**,*,?
        filterChainDefinition.put("/mobile/**","perms[{mobile}]");
//        filterChainDefinition.put("/salary/**","perms('salary')");
        filterChainDefinition.put("/main.html","authc");
        //配置登出过滤器
        filterChainDefinition.put("/logout","logout");

        bean.setFilterChainDefinitionMap(filterChainDefinition);
        bean.setLoginUrl("/index.html");
        bean.setSuccessUrl("/main.html");

        bean.setUnauthorizedUrl("/common/noauth");
        return bean;
    }


    public ModularRealmAuthenticator getModularRealmAuthenticator(){
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }
}
