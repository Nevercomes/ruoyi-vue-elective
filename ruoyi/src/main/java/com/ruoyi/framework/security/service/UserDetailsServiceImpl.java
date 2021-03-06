package com.ruoyi.framework.security.service;

import com.ruoyi.project.elective.student.domain.ElectiveStudent;
import com.ruoyi.project.elective.student.mapper.ElectiveStudentMapper;
import com.ruoyi.project.elective.teacher.domain.ElectiveTeacher;
import com.ruoyi.project.elective.teacher.mapper.ElectiveTeacherMapper;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ElectiveStudentMapper studentMapper;

    @Autowired
    private ElectiveTeacherMapper teacherMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser(user, permissionService.getMenuPermission(user));
        SysDept dept = user.getDept();
        if (dept.getDeptId().equals(100L)) {
            loginUser.setSchoolId(-1L);
            return loginUser;
        }
        // 100L为顶级部门 若衣科技
        while (!dept.getParentId().equals(100L)) {
            dept = deptService.selectDeptById(dept.getParentId());
        }
        loginUser.setSchoolId(dept.getDeptId());
        // 填入学生id
        ElectiveStudent student = studentMapper.selectStudentByUserId(user.getUserId());
        if(student != null) loginUser.setStudentId(student.getId());
        // 填入教师id
        ElectiveTeacher teacher = teacherMapper.selectTeacherByUserId(user.getUserId());
        if(teacher != null) loginUser.setTeacherId(teacher.getId());
        return loginUser;
    }
}
