# 分布式session共享——基于Spring Boot + Redis + ajax + jsonp实现

## 写在前面的话

各位小伙伴，你们有福了，这一节不仅教大家怎么实现分布式session的问题，还用kotlin开发，喜欢kotlin的小伙伴是不是很开心！

以前在写Android的时候，就对客户端请求有一定的认识，比如为什么要用token认证。这一节我们基于Spring Boot + Redis来实现session共享，因为session共享在分布式开发中很常见，所以起“spring-cloud | 分布式session”没毛病吧，另外，我也用kotlin的代码编写。在单点登录也是可以使用的。

## 理论分析

1、首先我们为什么要用session？
2、如果是用浏览器去访问，ok！没问题，怎么都好说，而我们是先后端分离，怎么样才能找到session，或者说session存在哪里呢？Redis，那么这个session怎么共享呢？于是我想到用cookie，浏览器上这确实不是太大的问题，但现在是ajax请求，相当于客户端，这不仅以为这cookie丢失，还存在跨域的问题。
3、经过一番搜索，得到的答案就一个，因为跨域所以cookie丢失，因为cookie丢失，所以不能获取session。
4、在我测试期间，发生了各种各样的错误，其中还没有“token”等字眼出现，这时候会发现，token也成了一种认证的趋势。
5、跨域如何解决？以前，我请教过别人，得到的答案是jsonp，那还是在实习的时候，也是第一次听到这个，没人指点，调试了很久，也弄不通，后来放弃了，直接在后端放行。但是今天不行了，硬着头皮去调试jsonp，神奇的是，居然一下子懂了，而且还调试成功了。
6、最后，cookie和session这个两个密不可分，但这并不是我们学习的重要，但一定要掌握。是不是又一次感受到了spring的强大。

## 测试架构

![image.png](https://upload-images.jianshu.io/upload_images/5805596-5edb479401c88829.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

用户通过浏览器访问前端服务（ui）进行登录操作，login服务会将用户信息的session存放到Redis中实现多个服务的session共享，以便其他服务都能访问到此session。


## 测试开始

![项目目录](https://upload-images.jianshu.io/upload_images/5805596-5667ed4aefb9f865.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

本次测试主要分为三块，session-1代码一个分布式系统，session-2代码第二个分布式系统，ui是前端页面，前后端分离。`RedisSessionConfig` 是使用Redis来保存session，后端逻辑代码放在 `UserController`中。

![测试前清空Redis](https://upload-images.jianshu.io/upload_images/5805596-d83b96ead775d616.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

测试前，我们先清空之前编写代码时候产生很多的sessionId。

![启动三个服务](https://upload-images.jianshu.io/upload_images/5805596-e6ec44a02bbe8d25.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们启动着三个服务。

![测试完毕，Redis中只有一条session](https://upload-images.jianshu.io/upload_images/5805596-949802eafe306f19.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

测试完毕，我们看一下Redis中sessionId：与我们期望的一致。

## 测试流程

只访问ui工程

**登录**

```
# 浏览器访问：
http://localhost:8082/login.html

# 响应：
{code: 0, msg: "登录成功"}
```

**session-1**

```
# 浏览器访问：
http://localhost:8082/session-1.html

# 响应：
{from: "session-1", name: "张三", age: 20}
```

**session-2**
```
# 浏览器访问：
http://localhost:8082/session-2.html

# 响应：
{from: "session-2", name: "张三", age: 20}
```

## 部分代码

pom.xml

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```

RedisSessionConfig

```kotlin
package com.fengwenyi.session1

import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

/**
 *
 * @author Wenyi Feng
 */
@Configuration
@EnableRedisHttpSession
class RedisSessionConfig {
}
```

UserController

```
package com.fengwenyi.session1

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 系统1
 * @author Wenyi Feng
 */
@RestController
@RequestMapping(value = "/user", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class UserController {

    @RequestMapping("/login")
    fun login(request: HttpServletRequest, response : HttpServletResponse) : String {
        var httpSession = request.session
        httpSession.setAttribute("name", "张三")
        httpSession.setAttribute("age", 20)
        var date = Date()

        var callback = request.getParameter("callback")

        return "$callback({\"code\":0,\"msg\":\"登录成功\"})"
    }

    @RequestMapping("/test")
    fun test(request: HttpServletRequest) : String {
        var httpSession = request.session
        var name = httpSession.getAttribute("name")
        var age = httpSession.getAttribute("age")

        var callback = request.getParameter("callback")

        return "$callback({\"from\":\"session-1\",\"name\":\"$name\",\"age\":$age})"
    }

}
```
session-1.html

```html
<!DOCTYPE html>
<html>
<head>
	<title>session-1</title>
</head>
<body>
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script>
function _jsonp(resultData){
    console.log(resultData)
}
</script>
<script src="http://localhost:8080/user/test?callback=_jsonp"></script>
</body>
</html>
```

## 参考文章

[1] [springboot集成springsession利用redis来实现session共享](https://www.cnblogs.com/fengli9998/p/7881331.html)

[2] [jsonp返回的数据格式与普通json的区别](https://segmentfault.com/q/1010000008772557)

[3] [简单透彻理解JSONP原理及使用](https://blog.csdn.net/u011897301/article/details/52679486)

[4] [java的会话管理：Cookie和Session](https://www.cnblogs.com/cenyu/p/6160470.html)

[5] [Linux | Redis](https://www.imooc.com/article/70330)




