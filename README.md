# SpringMvcNote
this is a blog for spring mvc
###写在开头：maven存在资源过滤的问题，如何改正？在pom.xml文件中使用如下配置将其配置完善  
   可以将java下的*.properties,*.xml文件过滤
``` 
    <build>
       <resources>
           <resource>
               <directory>src/main/java</directory>
               <includes>
                   <include>**/*.properties</include>
                   <include>**/*.xml</include>
               </includes>
               <filtering>false</filtering>
           </resource>
           <resource>
               <directory>src/main/resources</directory>
               <includes>
                   <include>**/*.properties</include>
                   <include>**/*.xml</include>
               </includes>
               <filtering>false</filtering>
           </resource>
       </resources>
    </build>
```

###一、如何新建spring mvc？
####1、新建一个maven Module ， springmvc-02-hello ， 添加web的支持
####2、确定导入了SpringMVC 的依赖！
'''
    <dependencies>
       <dependency>
           <groupId>junit</groupId>
           <artifactId>junit</artifactId>
           <version>4.12</version>
       </dependency>
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-webmvc</artifactId>
           <version>5.1.9.RELEASE</version>
       </dependency>
       <dependency>
           <groupId>javax.servlet</groupId>
           <artifactId>servlet-api</artifactId>
           <version>2.5</version>
       </dependency>
       <dependency>
           <groupId>javax.servlet.jsp</groupId>
           <artifactId>jsp-api</artifactId>
           <version>2.2</version>
       </dependency>
       <dependency>
           <groupId>javax.servlet</groupId>
           <artifactId>jstl</artifactId>
           <version>1.2</version>
       </dependency>
       //导入servlet 和 jsp 的 jar 依赖
       <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>servlet-api</artifactId>
          <version>2.5</version>
       </dependency>
       <dependency>
          <groupId>javax.servlet.jsp</groupId>
          <artifactId>jsp-api</artifactId>
          <version>2.2</version>
       </dependency>
    </dependencies>
'''
    
    
####3、配置web.xml  ， 注册DispatcherServlet
```
   <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
            version="4.0">
    
       <!--1.注册DispatcherServlet-->
       <servlet>
           <servlet-name>springmvc</servlet-name>
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
           <!--关联一个springmvc的配置文件:【servlet-name】-servlet.xml-->
           <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:springmvc-servlet.xml</param-value>//这里需要使用一个springmvc-servlet.xml,那就在下边配置一个servlet
           </init-param>
           <!--启动级别-1-->
           <load-on-startup>1</load-on-startup>
       </servlet>
    
       <!--/ 匹配所有的请求；（不包括.jsp）-->
       <!--/* 匹配所有的请求；（包括.jsp）-->
       <servlet-mapping>
           <servlet-name>springmvc</servlet-name>
           <url-pattern>/</url-pattern>
       </servlet-mapping>
    
    </web-app>
```
####4、编写SpringMVC 的 配置文件！名称：springmvc-servlet.xml  : [servletname]-servlet.xml
    说明，这里的名称要求是按照官方来的
```    
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    </beans>
```
####5、添加 处理映射器 到 springmvc=servlet.xml的容器中 
``` 
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
```
####6、添加 处理器适配器 到 springmvc=servlet.xml的容器中   
```
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
```
####7、添加 视图解析器 到 springmvc=servlet.xml的容器中   
    //注意这里的前缀和后缀需要修改
```   
    <!--视图解析器:DispatcherServlet给他的ModelAndView-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="InternalResourceViewResolver">
       <!--前缀-->
       <property name="prefix" value="/WEB-INF/jsp/"/>
       <!--后缀-->
       <property name="suffix" value=".jsp"/>
    </bean>
```
####8、编写我们要操作业务Controller ，要么实现Controller接口，要么增加注解；需要返回一个ModelAndView，装数据，封视图；
    我在src/main/java/com/huang/controller/HelloController下增加一个业务Controller：
####9、将自己的类交给SpringIOC容器，注册bean
```
    <!--Handler-->
    <bean id="/hello" class="com.kuang.controller.HelloController"/>
```
####10、写要跳转的jsp页面，显示ModelandView存放的数据，以及我们的正常页面； 
```   
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
       <title>Huang Yuyuan</title>
    </head>
    <body>
    ${msg}
    </body>
    </html>
```   
####11、配置Tomcat 启动测试！
    在http://localhost:8080/hello 测试
###心得：当出现问题时不要慌，检查三个事情是不是没有做好：
    1、缺少jar包
    2、在IDEA项目发布添加lib依赖
    3、重启Tomcat    
***
###二、如何用注解实现呢？
####1、新建一个module，springmvc-03-hello-annotation 。添加web支持！