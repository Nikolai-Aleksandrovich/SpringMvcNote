# SpringMvcNote
this is a blog for spring mvc  
###写在开头：maven存在资源过滤的问题，如何改正？
   在pom.xml文件中使用如下配置将其配置完善  
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
1.新建一个maven Module ， springmvc-02-hello ， 添加web的支持     
2.确定导入了SpringMVC 的依赖！   
```
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
```
    
    
3.配置web.xml  ， 注册DispatcherServlet
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
4.编写SpringMVC 的 配置文件！名称：springmvc-servlet.xml  : [servletname]-servlet.xml
    说明，这里的名称要求是按照官方来的
```    
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    </beans>
```
5.添加 处理映射器 到 springmvc=servlet.xml的容器中 
``` 
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
```
6.添加 处理器适配器 到 springmvc=servlet.xml的容器中   
```
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
```
7.添加 视图解析器 到 springmvc=servlet.xml的容器中   
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
8.编写我们要操作业务Controller ，要么实现Controller接口，要么增加注解；需要返回一个ModelAndView，装数据，封视图；   

   我在src/main/java/com/huang/controller/HelloController下增加一个业务Controller：  
```
    package com.huang.controller;
    
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.Controller;
    
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    
    //注意：先导入Controller接口
    public class HelloController implements Controller {
    
       public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
           //ModelAndView 模型和视图
           ModelAndView mv = new ModelAndView();
    
           //封装对象，放在ModelAndView中。Model
           mv.addObject("msg","HelloSpringMVC!");
           //封装要跳转的视图，放在ModelAndView中
           mv.setViewName("hello"); //: /WEB-INF/jsp/hello.jsp
           return mv;
      }
       
    }
``` 
    
9.将自己的类交给SpringIOC容器，注册bean   
```
    <!--Handler-->
    <bean id="/hello" class="com.kuang.controller.HelloController"/>
```
10.写要跳转的jsp页面，显示ModelandView存放的数据，以及我们的正常页面； 
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
11.配置Tomcat 启动测试！   

   在http://localhost:8080/hello 测试   
    
###心得：当出现问题时不要慌，检查三个事情是不是没有做好：   

    1.缺少jar包
    2.在IDEA项目发布添加lib依赖
    3.重启Tomcat    
***
###二、如何用注解实现呢？  
1.新建一个module，springmvc-03-hello-annotation 。添加web支持！  

2.同上，由于Maven可能存在资源过滤的问题，需将配置完善  
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
3.在pom.xml文件引入相关的依赖：主要有Spring框架核心库、Spring MVC、servlet , JSTL  
4.配置web.xml  
```     
    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
            version="4.0">
    
       <!--1.注册servlet-->
       <servlet>
           <servlet-name>SpringMVC</servlet-name>
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
           <!--通过初始化参数指定SpringMVC配置文件的位置，进行关联-->
           <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:springmvc-servlet.xml</param-value>
           </init-param>
           <!-- 启动顺序，数字越小，启动越早 -->
           <load-on-startup>1</load-on-startup>
       </servlet>
    
       <!--所有请求都会被springmvc拦截 -->
       <servlet-mapping>
           <servlet-name>SpringMVC</servlet-name>
           <url-pattern>/</url-pattern>
       </servlet-mapping>
    
    </web-app>
``` 
    注意：在<servlet-mapping><url-pattern>/</url-pattern></servlet-mapping>中的/不可换为/*，< url-pattern > / </ url-pattern > 不会匹配到.jsp， 只针对我们编写的请求；即：.jsp 不会进入spring的 DispatcherServlet类 。< url-pattern > /* </ url-pattern > 会匹配 *.jsp，会出现返回 jsp视图 时再次进入spring的DispatcherServlet 类，导致找不到对应的controller所以报404错。  
5.  添加Spring MVC配置文件   
    在resource目录下添加springmvc-servlet.xml配置文件，配置的形式与Spring容器配置基本类似，为了支持基于注解的IOC，设置了自动扫描包的功能，具体配置信息如下：
```
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           https://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/mvc
           https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    
       <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
       <context:component-scan base-package="com.kuang.controller"/>
       <!-- 让Spring MVC不处理静态资源 -->
       <mvc:default-servlet-handler />
       <!--
       支持mvc注解驱动
           在spring中一般采用@RequestMapping注解来完成映射关系
           要想使@RequestMapping注解生效
           必须向上下文中注册DefaultAnnotationHandlerMapping
           和一个AnnotationMethodHandlerAdapter实例
           这两个实例分别在类级别和方法级别处理。
           而annotation-driven配置帮助我们自动完成上述两个实例的注入。
        -->
       <mvc:annotation-driven />
    
       <!-- 视图解析器 -->
       <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
             id="internalResourceViewResolver">
           <!-- 前缀 -->
           <property name="prefix" value="/WEB-INF/jsp/" />
           <!-- 后缀 -->
           <property name="suffix" value=".jsp" />
       </bean>
    
    </beans>
```          
   在视图解析器中我们把所有的视图都存放在/WEB-INF/目录下，这样可以保证视图安全，因为这个目录下的文件，客户端不能直接访问。  
6.创建Controller   
    编写一个Java控制类：com.huang.controller.HelloController
```   
    package com.huang.controller;
    
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.RequestMapping;
    
    @Controller  //是为了让Spring IOC容器初始化时自动扫描到；
    @RequestMapping("/HelloController") //为了映射请求路径，这里因为类与方法上都有映射所以访问时应该是/HelloController/hello；
    public class HelloController {
    
       //真实访问地址 : 项目名/HelloController/hello
       @RequestMapping("/hello")
        方法中声明Model类型的参数是为了把Action中的数据带到视图中；//
       public String sayHello(Model model){
           //向模型中添加属性msg与值，可以在JSP页面中取出并渲染
           model.addAttribute("msg","hello,SpringMVC");
           //web-inf/jsp/hello.jsp
           return "hello";//方法返回的结果是视图的名称hello，加上配置文件中的前后缀变成WEB-INF/jsp/hello.jsp。
      }
    }
```           
7.创建视图层  
    在WEB-INF/ jsp目录中创建hello.jsp ， 视图可以直接取出并展示从Controller带回的信息；可以通过EL表示取出Model中存放的值，或者对象；
``` 
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
       <title>SpringMVC</title>
    </head>
    <body>
    ${msg}
    </body>
    </html>
```     
8.配置Tomcat运行  
###总结  
具体步骤总结：  
1.新建一个web项目
2.导入相关jar包
3.编写web.xml , 注册DispatcherServlet
4.编写springmvc配置文件
5.接下来就是去创建对应的控制类 , controller
6.最后完善前端视图和controller之间的对应
7.测试运行调试.  
###使用springMVC必须配置的三大件：  
   处理器映射器、处理器适配器、视图解析器  
   通常，我们只需要手动配置视图解析器，而处理器映射器和处理器适配器只需要开启注解驱动即可，而省去了大段的xml配置