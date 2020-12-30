# SpringMvcNote
this is a conclusion for spring mvc controller
###控制器Controller  
1.控制器通过接口定义或注解定义两种方法实现。

2.控制器负责解析用户的请求并将其转换为一个模型(Model)。

3.在Spring MVC中,一个控制器类可以包含多个方法,对于Controller的配置方式有很多种  
###控制器Controller 
   Controller是一个接口，在org.springframework.web.servlet.mvc包下，接口中只有一个方法；  
```
    //实现该接口的类获得控制器功能
    public interface Controller {
       //处理请求且返回一个模型与视图对象
       ModelAndView handleRequest(HttpServletRequest var1, HttpServletResponse var2) throws Exception;
    }
```   
 
###有两种方法实现controller接口，可以直接implement 上方的Controller接口即可：  
   1.在com.huang.controller包下编写一个Controller类，ControllerTest1： 
``` 
    //定义控制器
    public class ControllerTest1 implements Controller {
    
       public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
           //返回一个模型视图对象
           ModelAndView mv = new ModelAndView();
           mv.addObject("msg","Test1Controller");
           mv.setViewName("test");
           return mv;
      }
    }
```  
   2.去Spring配置文件中注册请求的bean；name对应请求路径，class对应处理请求的类  
```
    <bean name="/t1" class="com.huang.controller.ControllerTest1"/>
```   
   3.编写前端test.jsp，在WEB-INF/jsp目录下编写，对应视图解析器  
```
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
       <title>HelloController</title>
    </head>
    <body>
    ${msg}
    </body>
    </html>
```     
####总结：这个方法实现接口controller定义控制器比较老，贯彻步骤1可以发现缺点：一个控制器只能有一个方法  

###使用注解@Controller(常用)  
   1.在配置文件中声明组件扫描。  
```
       <!-- 自动扫描指定的包，下面所有注解类交给IOC容器管理 -->
       <context:component-scan base-package="com.kuang.controller"/>
```   
   2.增加一个ControllerTest2类，使用注解实现；  
```
    //@Controller注解的类会自动添加到Spring上下文中
    @Controller
    public class ControllerTest2{
    
       //映射访问路径
       @RequestMapping("/t2")
       public String index(Model model){
           //Spring MVC会自动实例化一个Model对象用于向视图中传值
           model.addAttribute("msg", "ControllerTest2");
           //返回视图位置
           return "test";
      }
    
    }
```        
   Spring可以使用扫描机制来找到应用程序中所有基于注解的控制器类  
###简要分析@RequestMapping  
   @RequestMapping注解用于映射url到控制器类或一个特定的处理程序方法。可用于类或方法上。用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径。  
   1.举例一：RequestMapping用于方法上：   
   此时的访问路径为： http://localhost:8080/ h1
   ```
        @Controller
        public class TestController {
           @RequestMapping("/h1")
           public String test(){
               return "test";
          }
        }
```  
   2.举例二：RequestMapping同时用于类与方法上：  
     
   此时的访问路径为：http://localhost:8080/ admin /h1，需要先指定类的路径再指定方法的路径
   ```
        @Controller
        @RequestMapping("/admin")
        public class TestController {
           @RequestMapping("/h1")
           public String test(){
               return "test";
          }
        }

```  
###RestFul风格：是一个资源定位及资源操作的风格。    
*资源：互联网所有的事物都可以被抽象为资源

*资源操作：使用POST、DELETE、PUT、GET，使用不同方法对资源进行操作。  
    *传统方式操作资源 ：  
        * http://127.0.0.1/item/queryItem.action?id=1 查询,GET  
        * http://127.0.0.1/item/saveItem.action 新增,POST  
        * http://127.0.0.1/item/updateItem.action 更新,POST  
        * http://127.0.0.1/item/deleteItem.action?id=1 删除,GET或POST    
    *RESTful方式操作资源 ： 可以通过不同的请求方式来实现不同的效果   
        * http://127.0.0.1/item/1 查询,GET    
        * http://127.0.0.1/item 新增,POST    
        * http://127.0.0.1/item 更新,PUT    
        * http://127.0.0.1/item/1 删除,DELETE      
    1.示例一：不使用Restful风格的controller   
    *使用http://localhost:8080/add?a=1&b=2访问  
```  
        @Controller
        public class NoRESTfulController {
            @RequestMapping("/add")
            public String test(int a, int b, Model model){
                int result = a+b;
                model.addAttribute("arg","the answer is "+result);
                return "test";
        
            }
        } 

```      
    2.示例二：使用Restful风格的controller   
    *使用http://localhost:8080/add/1/2访问  
``` 
    @Controller
    public class RestFulController {
    
       //映射访问路径
       @RequestMapping("/commit/{p1}/{p2}")
       public String index(@PathVariable int p1, @PathVariable int p2, Model model){
           
           int result = p1+p2;
           //Spring MVC会自动实例化一个Model对象用于向视图中传值
           model.addAttribute("msg", "结果："+result);
           //返回视图位置
           return "test";
           
      }
        //可以在RequestMapping设置method来分别处理GET POST PUT DELETE等请求
        @RequestMapping(value = "/hello",method = {RequestMethod.GET})
            public String index2(Model model){
                model.addAttribute("msg","hello!");
                return "test";
            }
       
    }
```       
###总结：
    *url更简洁  
    *参数更方便，框架负责自动的类型转换  
    *也可以约束访问参数  
    *GetMapping是一个组合注解，意义为： @RequestMapping(method =RequestMethod.GET)  
    *也可以用@GetMapping，@PostMapping，@PutMapping，@DeleteMapping，@PatchMapping  
                     
                     
                     
   
   