package com.ruoyi.project.elective.teacher.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.elective.teacher.domain.ElectiveTeacher;
import com.ruoyi.project.elective.teacher.mapper.ElectiveTeacherMapper;
import com.ruoyi.project.elective.teacher.service.IElectiveTeacherService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.domain.SysUserRole;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.mapper.SysUserRoleMapper;
import com.ruoyi.project.system.service.ISysConfigService;
import com.ruoyi.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 教师管理Service业务层处理
 *
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveTeacherServiceImpl implements IElectiveTeacherService {

    private static final Logger log = LoggerFactory.getLogger(ElectiveTeacherServiceImpl.class);

    @Autowired
    private ElectiveTeacherMapper electiveTeacherMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysConfigService sysConfigService;

    /**
     * 查询教师管理
     *
     * @param id 教师管理ID
     * @return 教师管理
     */
    @Override
    public ElectiveTeacher selectElectiveTeacherById(Long id) {
        return electiveTeacherMapper.selectElectiveTeacherById(id);
    }

    /**
     * 查询教师管理列表
     *
     * @param electiveTeacher 教师管理
     * @return 教师管理
     */
    @Override
    public List<ElectiveTeacher> selectElectiveTeacherList(ElectiveTeacher electiveTeacher) {
        return electiveTeacherMapper.selectElectiveTeacherList(electiveTeacher);
    }

    /**
     * 新增教师管理
     *
     * @param electiveTeacher 教师管理
     * @return 结果
     */
    @Override
    public int insertElectiveTeacher(ElectiveTeacher electiveTeacher) {
        // 新增教师先创建用户
        SysUser newUser = new SysUser();
        newUser.setDeptId(SecurityUtils.getSchoolId());
        newUser.setUserName(electiveTeacher.getUserName());
        newUser.setNickName(electiveTeacher.getName());
        newUser.setEmail(electiveTeacher.getEmail());
        newUser.setPhonenumber(electiveTeacher.getPhonenumber());
        newUser.setPassword(SecurityUtils.encryptPassword(electiveTeacher.getPassword()));
        newUser.setRemark(electiveTeacher.getRemark());
        newUser.setSex(electiveTeacher.getSex());
        newUser.preInsert();
        sysUserMapper.insertUser(newUser);
        // 建立关联的角色
        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(newUser.getUserId());
        sysUserRole.setRoleId(UserConstants.TEACHER_ROLE_ID);
        userRoleList.add(sysUserRole);
        sysUserRoleMapper.batchUserRole(userRoleList);
        // 新增教师
        electiveTeacher.setUserId(newUser.getUserId());
        electiveTeacher.preInsert();
        return electiveTeacherMapper.insertElectiveTeacher(electiveTeacher);
    }

    /**
     * 修改教师管理
     *
     * @param electiveTeacher 教师管理
     * @return 结果
     */
    @Override
    public int updateElectiveTeacher(ElectiveTeacher electiveTeacher) {
        // 更新用户
        SysUser user = new SysUser();
        user.setUserId(electiveTeacher.getUserId());
        sysUserService.checkUserAllowed(user);
        user.setNickName(electiveTeacher.getName());
        user.setEmail(electiveTeacher.getEmail());
        user.setPhonenumber(electiveTeacher.getPhonenumber());
        user.setRemark(electiveTeacher.getRemark());
        user.setSex(electiveTeacher.getSex());
        user.preUpdate();
        sysUserMapper.updateUser(user);
        // 更新教师
        electiveTeacher.preUpdate();
        return electiveTeacherMapper.updateElectiveTeacher(electiveTeacher);
    }

    /**
     * 批量删除教师管理
     *
     * @param ids 需要删除的教师管理ID
     * @return 结果
     */
    @Override
    public int deleteElectiveTeacherByIds(Long[] ids) {
        // 同时批量删除user
        Long[] userIds = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            userIds[i] = selectElectiveTeacherById(ids[i]).getUserId();
        }
        electiveTeacherMapper.deleteElectiveTeacherByIds(ids);
        return sysUserService.deleteUserByIds(userIds);
    }

    /**
     * 删除教师管理信息
     *
     * @param id 教师管理ID
     * @return 结果
     */
    @Override
    public int deleteElectiveTeacherById(Long id) {
        return electiveTeacherMapper.deleteElectiveTeacherById(id);
    }

    /**
     * 导入教师数据
     *
     * @param teacherList
     * @param updateSupport 是否支持更新
     * @param operName      操作用户
     * @return
     */
    @Override
    public String importTeacher(List<ElectiveTeacher> teacherList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(teacherList) || teacherList.size() == 0) {
            throw new CustomException("导入教师数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String initPwd = sysConfigService.selectConfigByKey("sys.user.initPassword");

        for (ElectiveTeacher teacher : teacherList) {
            try {
                SysUser u = sysUserMapper.selectUserByUserName(teacher.getUserName());
                if (StringUtils.isEmpty(teacher.getPassword()))
                    teacher.setPassword(initPwd);
                if (StringUtils.isNull(u)) {
                    this.insertElectiveTeacher(teacher);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(teacher.getUserName()).append(" 导入成功");
                } else if (updateSupport) {
                    teacher.setUserId(u.getUserId());
                    this.updateElectiveTeacher(teacher);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(teacher.getUserName()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(teacher.getUserName()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + teacher.getUserName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }

        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
