# SpringMvcNote
this is a note for spring mvc modelAndView

# SpringMvc结果跳转的方式：

## 一、ModelAndView  

思路：设置ModelAndView对象 , 根据view的名称与视图解析器跳到指定的页面 .

页面名组成 : {视图解析器前缀} + viewName +{视图解析器后缀}

#### 1.添加视图解析器到springmvc-servlet.xml中

```java
<!-- 视图解析器 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
     id="internalResourceViewResolver">
   <!-- 前缀 -->
   <property name="prefix" value="/WEB-INF/jsp/" />
   <!-- 后缀 -->
   <property name="suffix" value=".jsp" />
</bean>
```

#### 2.设置对应的controller ，返回一个ModelAndView类：

``` java
public class ControllerTest1 implements Controller {

   public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
       //返回一个模型视图对象
       ModelAndView mv = new ModelAndView();
       mv.addObject("msg","ControllerTest1");
       mv.setViewName("test");
       return mv;
  }
}
```
##   二、ServletAPI

 说明：这样做不需要视图解析器，设置ServletAPI即可

#### 1、通过HttpServletResponse进行输出

```java
@Controller
public class ResultGo {
   @RequestMapping("/result/t1")
   public void test1(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
       rsp.getWriter().println("Hello,Spring BY servlet API");
  }
}
```
#### 2、通过HttpServletResponse实现重定向

```java
@Controller
public class ResultGo{
    @RequestMapping("/result/t2")
    public void test2(HttpServletRequest request,HttpServletResponse)throws IOException {
       rsponse.sendRedirect("/index.jsp");
  }
}
```
#### 3、通过HttpServletResponse实现转发

```java
@Controller
public class ResultGo{
    @RequestMapping("/result/t2")
    public void test2(HttpServletRequest request,HttpServletResponse)throws IOException {
       //转发
       request.setAttribute("msg","/result/t3");
       request.getRequestDispatcher("/WEB-INF/jsp/test.jsp").forward(request,rsponse);
  }
}
```
## 三、SpringMVC

#### 1、通过SpringMVC来实现转发和重定向 - 无需视图解析器

```java
@Controller
public class ResultSpringMVC {
   @RequestMapping("/rsm/t1")
   public String test1(){
       //转发
       return "/index.jsp";
  }

   @RequestMapping("/rsm/t2")
   public String test2(){
       //转发二
       return "forward:/index.jsp";
  }

   @RequestMapping("/rsm/t3")
   public String test3(){
       //重定向
       return "redirect:/index.jsp";
  }
}
```
####  2、通过SpringMVC来实现转发和重定向 - 有视图解析器 

   ```java
@Controller
public class ResultSpringMVC2 {
   @RequestMapping("/rsm2/t1")
   public String test1(){
       //转发
       return "test";
  }

   @RequestMapping("/rsm2/t2")
   public String test2(){
       //重定向
       return "redirect:/index.jsp";
       //return "redirect:hello.do"; //hello.do为另一个请求/
  }

}
   ```
#  SpringMvc数据处理

处理前端提交的数据时，可能域名称与参数名一致，也可能不一致，也可能提交类型为对象

#### 1.提交的域名称和处理方法的参数名一致时：

提交数据为：http://localhost:8080/hello?name=huangyuyuan

处理方法：

   ```java
@RequestMapping("/hello")
public String hello(String name){
   System.out.println(name);
   return "hello";
}
   ```
后台输出：huangyuyuan

#### **2、提交的域名称和处理方法的参数名不一致**

提交数据 : http://localhost:8080/hello?username=huangyuyuan

处理方法 :

```  java
//@RequestParam("username") : username提交的域的名称 .
@RequestMapping("/hello")
public String hello(@RequestParam("username") String name){
   System.out.println(name);
   return "hello";
}
```
后台输出：huangyuyuan

#### **3、提交的是一个对象**

当要求提交的表单域和对象的属性名一致  , 参数使用对象即可

当前端传递的参数名和对象名不一致，会返回null

假设实体类为：

```java
public class User {
   private int id;
   private String name;
   private int age;
   //构造
   //get/set
   //tostring()
}  
```
提交数据为：http://localhost:8080/mvc04/user?name=huangyuyuan&id=1&age=15

处理方法为：

``` java
@RequestMapping("/user")
public String user(User user){
   System.out.println(user);
   return "hello";
}
```
后台输出 : User { id=1, name='kuangshen', age=15 }

# 数据显示到前端 

#### **第一种 : 通过ModelAndView**

```java
public class ControllerTest1 implements Controller {

   public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
       //返回一个模型视图对象
       ModelAndView mv = new ModelAndView();
       mv.addObject("msg","ControllerTest1");
       mv.setViewName("test");
       return mv;
  }
}
```

#### **第二种 : 通过ModelMap**

```java
@RequestMapping("/hello")
public String hello(@RequestParam("username") String name, ModelMap model){
   //封装要显示到视图中的数据
   //相当于req.setAttribute("name",name);
   model.addAttribute("name",name);
   System.out.println(name);
   return "hello";
}
```

####    **第三种 : 通过Model**

```java
@RequestMapping("/ct2/hello")
public String hello(@RequestParam("username") String name, Model model){
   //封装要显示到视图中的数据
   //相当于req.setAttribute("name",name);
   model.addAttribute("msg",name);
   System.out.println(name);
   return "test";
}
```

#### 总结：

1.Model 只有几个方法适合用于储存数据，方便使用

2.ModelMap 继承了 LinkedMap ，除了实现了自身的一些方法，同样的继承 LinkedMap 的方法和特性；

3.ModelAndView 可以在储存数据的同时，可以进行设置返回的逻辑视图，进行控制展示层的跳转。




