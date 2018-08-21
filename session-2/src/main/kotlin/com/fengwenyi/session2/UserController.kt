package com.fengwenyi.session2

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * 系统2
 * @author Wenyi Feng
 */
@RestController
@RequestMapping(value = "/user", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class UserController {

    @RequestMapping("/test")
    fun test(request: HttpServletRequest) : String {
        var httpSession = request.session
        var name = httpSession.getAttribute("name")
        var age = httpSession.getAttribute("age")

        var callback = request.getParameter("callback")

        return "$callback({\"from\":\"session-2\",\"name\":\"$name\",\"age\":$age})"
    }

}