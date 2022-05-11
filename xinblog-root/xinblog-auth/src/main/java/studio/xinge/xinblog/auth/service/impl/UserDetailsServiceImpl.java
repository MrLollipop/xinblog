package studio.xinge.xinblog.auth.service.impl;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/4/24
 * @Description
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import studio.xinge.xinblog.auth.entity.TBlogUser;
import studio.xinge.xinblog.auth.entity.TUserRole;
import studio.xinge.xinblog.auth.mapper.TBlogUserMapper;
import studio.xinge.xinblog.auth.security.LoginUser;
import studio.xinge.xinblog.auth.service.TUserRoleService;
import studio.xinge.xinblog.common.utils.Constant;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 在登录校验前调用，获取用户信息以及权限
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/4/24
 * @Description
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private TBlogUserMapper blogUserMapper;

    @Resource
    private TUserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {

//        查询用户
        TBlogUser blogUser = blogUserMapper.selectOne(new QueryWrapper<TBlogUser>().eq("mobile", mobile).eq("status", 1));
        if (null == blogUser) {
            throw new UsernameNotFoundException("用户不存在");
        }

//        查询用户权限
        List<TUserRole> list = userRoleService.list(new QueryWrapper<TUserRole>().eq("user_id", blogUser.getId()));
//        auths权限对象不可为空
        List<GrantedAuthority> auths = new ArrayList<>();
        list.forEach(t -> {
            auths.add(new SimpleGrantedAuthority(Constant.ROlE_PREFIX + t.getRoleId().toString()));
        });

//        构造登录用户
        return new LoginUser(mobile, blogUser.getEncryptedPassword(), auths);

    }
}
