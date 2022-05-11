package studio.xinge.xinblog.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.xinge.xinblog.auth.security.LoginUser;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.R;

import java.util.Collection;
import java.util.List;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/4/23
 * @Description
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private HashOperations hashOperations;

    @GetMapping("hello")
    @Secured("ROLE_1521787586081280001")
//    @PreAuthorize("hasAuthority('abc')")
    public R hello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mobile = (String) auth.getPrincipal();
        LoginUser loginUser = (LoginUser) hashOperations.get(Constant.TOKEN_KEY, mobile);

        return R.ok("say hello").put(mobile, loginUser);
    }

    @GetMapping("nums")
    @PreFilter("filterObject%2 == 0")
    public R preFilter(@RequestBody List<Integer> nums) {
        return R.ok().put("nums", nums);
    }

    ;
}
