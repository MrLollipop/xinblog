package studio.xinge.xinblog.auth.service.impl;

import studio.xinge.xinblog.auth.entity.TUserRole;
import studio.xinge.xinblog.auth.mapper.TUserRoleMapper;
import studio.xinge.xinblog.auth.service.TUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户与角色对应关系 服务实现类
 * </p>
 *
 * @author xinge
 * @since 2022-05-04
 */
@Service
public class TUserRoleServiceImpl extends ServiceImpl<TUserRoleMapper, TUserRole> implements TUserRoleService {

}
