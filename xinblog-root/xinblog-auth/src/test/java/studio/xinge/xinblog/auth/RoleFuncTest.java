package studio.xinge.xinblog.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import studio.xinge.xinblog.auth.entity.TFunc;
import studio.xinge.xinblog.auth.entity.TRole;
import studio.xinge.xinblog.auth.entity.TRoleFunc;
import studio.xinge.xinblog.auth.service.TFuncService;
import studio.xinge.xinblog.auth.service.TRoleFuncService;
import studio.xinge.xinblog.auth.service.TRoleService;

import java.time.LocalDateTime;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/4
 * @Description
 */
@SpringBootTest
public class RoleFuncTest {

    @Autowired
    private TFuncService funcService;

    @Autowired
    private TRoleService roleService;

    @Autowired
    private TRoleFuncService roleFuncService;

    @Test
    public void addRole() {
        TRole role = new TRole();
        role.setRoleName("普通用户");
        role.setRemark("博客的访问、评论");
        LocalDateTime now = LocalDateTime.now();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        roleService.save(role);
    }

    @Test
    public void addFuncAndRoleFunc() {
        TFunc func = new TFunc();
        func.setName("博客评论");
        funcService.save(func);

//        System.out.println(func.getId());

        TRoleFunc roleFunc = new TRoleFunc();
        roleFunc.setFuncId(func.getId());
        roleFunc.setRoleId(1521787586081280001L);

        roleFuncService.save(roleFunc);
    }
}
