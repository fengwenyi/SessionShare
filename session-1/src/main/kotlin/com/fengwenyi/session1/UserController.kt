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