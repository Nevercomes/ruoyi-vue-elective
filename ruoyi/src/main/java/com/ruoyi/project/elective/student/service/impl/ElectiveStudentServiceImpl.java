package com.ruoyi.project.elective.student.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.constant.ElectiveDict;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.elective.teacher.service.impl.ElectiveTeacherServiceImpl;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.domain.SysUserRole;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.mapper.SysUserRoleMapper;
import com.ruoyi.project.system.service.ISysConfigService;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.student.mapper.ElectiveStudentMapper;
import com.ruoyi.project.elective.student.domain.ElectiveStudent;
import com.ruoyi.project.elective.student.service.IElectiveStudentService;
import org.springframework.transaction.annotation.Transactional;

/**
 * studentService业务层处理
 *
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveStudentServiceImpl implements IElectiveStudentService {

    private static final Logger log = LoggerFactory.getLogger(ElectiveTeacherServiceImpl.class);

    @Autowired
    private ElectiveStudentMapper electiveStudentMapper;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private ISysUserService sysUserService;


    /**
     * 查询student
     *
     * @param id studentID
     * @return student
     */
    @Override
    public ElectiveStudent selectElectiveStudentById(Long id) {
        return electiveStudentMapper.selectElectiveStudentById(id);
    }

    /**
     * 查询student列表
     *
     * @param electiveStudent student
     * @return student
     */
    @Override
    public List<ElectiveStudent> selectElectiveStudentList(ElectiveStudent electiveStudent) {
        return electiveStudentMapper.selectElectiveStudentList(electiveStudent);
    }

    /**
     * 新增student
     *
     * @param electiveStudent student
     * @return 结果
     */
    @Override
    @Transactional
    public int insertElectiveStudent(ElectiveStudent electiveStudent) {
        // 创建学生先创建用户
        SysUser newUser = new SysUser();
        newUser.setDeptId(electiveStudent.getDeptId());
        newUser.setUserName(electiveStudent.getUserName());
        newUser.setNickName(electiveStudent.getName());
        newUser.setPassword(SecurityUtils.encryptPassword(electiveStudent.getPassword()));
        newUser.setRemark(electiveStudent.getRemark());
        newUser.setSex(electiveStudent.getSex());
        newUser.preInsert();
        sysUserMapper.insertUser(newUser);
        // 建立关联的角色
        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(newUser.getUserId());
        sysUserRole.setRoleId(UserConstants.STUDENT_ROLE_ID);
        userRoleList.add(sysUserRole);
        sysUserRoleMapper.batchUserRole(userRoleList);
        // 新增学生
        electiveStudent.setUserId(newUser.getUserId());
        electiveStudent.preInsert();
        return electiveStudentMapper.insertElectiveStudent(electiveStudent);
    }

    /**
     * 修改student
     *
     * @param electiveStudent student
     * @return 结果
     */
    @Override
    @Transactional
    public int updateElectiveStudent(ElectiveStudent electiveStudent) {
        SysUser user = new SysUser();
        user.setUserId(electiveStudent.getUserId());
        sysUserService.checkUserAllowed(user);
        SysUser oldUser = sysUserMapper.selectUserById(electiveStudent.getUserId());
        if (!oldUser.getUserName().equals(electiveStudent.getUserName())) {
            if (sysUserService.checkUserNameUnique(electiveStudent.getUserName()).equals(UserConstants.UNIQUE)) {
                user.setUserName(electiveStudent.getUserName());
            } else {
                throw new CustomException("修改学生失败，登录名已经存在");
            }
        }
        user.setNickName(electiveStudent.getName());
        user.setRemark(electiveStudent.getRemark());
        user.setSex(electiveStudent.getSex());
        user.preUpdate();
        // 更新学生
        electiveStudent.preUpdate();
        sysUserMapper.updateUser(user);
        return electiveStudentMapper.updateElectiveStudent(electiveStudent);
    }

    /**
     * 批量删除student
     *
     * @param ids 需要删除的studentID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteElectiveStudentByIds(Long[] ids) {
        // 同时批量删除user
        Long[] userIds = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            userIds[i] = this.selectElectiveStudentById(ids[i]).getUserId();
        }
        electiveStudentMapper.deleteElectiveStudentByIds(ids);
        return sysUserService.deleteUserByIds(userIds);
    }

    /**
     * 删除student信息
     *
     * @param id studentID
     * @return 结果
     */
    @Override
    public int deleteElectiveStudentById(Long id) {
        return electiveStudentMapper.deleteElectiveStudentById(id);
    }

    /**
     * 导入学生数据
     * @param studentList
     * @param updateSupport
     * @param operName
     * @return
     */
    @Override
    public String importStudent(List<ElectiveStudent> studentList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(studentList) || studentList.size() == 0) {
            throw new CustomException("导入学生数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String initPwd = sysConfigService.selectConfigByKey("sys.user.initPassword");

        // 获取年级List
        SysDept query = new SysDept();
//        query.setType(ElectiveDict.DEPT_TYPE_GRADE);
//        List<SysDept> gradeList = sysDeptService.selectDeptList(query);
        // 获取班级List
        query.setType(ElectiveDict.DEPT_TYPE_CLASS);
        List<SysDept> classList = sysDeptService.selectDeptList(query);

        for (ElectiveStudent student : studentList) {
            try {
                SysUser u = sysUserMapper.selectUserByUserName(student.getUserName());
                if (StringUtils.isEmpty(student.getPassword()))
                    student.setPassword(initPwd);
                if (StringUtils.isNull(u)) {
                    // 获取班级Id
                    Long deptId = getDeptId(classList, student.getClassName());
                    if (deptId != null) {
                        student.setDeptId(deptId);
                        this.insertElectiveStudent(student);
                        successNum++;
                        successMsg.append("<br/>").append(successNum).append("、账号 ").append(student.getUserName()).append(" 导入成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(student.getUserName()).append(" 导入失败，班级不匹配");
                    }
                } else if (updateSupport) {
                    student.setUserId(u.getUserId());
                    this.updateElectiveStudent(student);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(student.getUserName()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(student.getUserName()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + student.getUserName() + " 导入失败：";
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

    /**
     *
     * @param electiveStudent
     * @return
     */
    @Override
    public List<ElectiveStudent> selectExportList(ElectiveStudent electiveStudent) {
        return electiveStudentMapper.selectExportList(electiveStudent);
    }

    @Override
    public void deleteStudentByDeptId(Long deptId) {
        electiveStudentMapper.deleteStudentByDeptId(deptId);
    }

    private Long getDeptId(List<SysDept> list, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (SysDept dept : list) {
            if (dept.getDeptName().equals(StringUtils.trim(name))) {
                return dept.getDeptId();
            }
        }
        return null;
    }

}
