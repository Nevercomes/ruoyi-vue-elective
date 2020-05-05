package com.ruoyi.project.system.controller;

import java.io.IOException;

import com.ruoyi.project.elective.student.domain.ElectiveStudent;
import com.ruoyi.project.elective.student.mapper.ElectiveStudentMapper;
import com.ruoyi.project.system.domain.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ElectiveStudentMapper studentMapper;

    /**
     * 个人信息
     */
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult profile(@PathVariable(value = "userId", required = false) Long userId) {
        SysUser user = new SysUser();
        if (userId != null) {
            user = userService.selectUserById(userId);
        } else {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            // update profile 后loginUser内的user不会被刷新，所以这里使用数据库的user
            user = userService.selectUserById(loginUser.getUser().getUserId());
            ElectiveStudent studentId = studentMapper.selectStudentByUserId(user.getUserId());
            if (studentId != null) {
                ElectiveStudent student =  studentMapper.selectElectiveStudentById(studentId.getId());
                SysDept dept =  user.getDept();
                dept.setDeptName(student.getClassName());
                user.setDept(dept);
            }
        }
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(user.getUserName()));
        ajax.put("postGroup", userService.selectUserPostGroup(user.getUserName()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        // 将avatar置空 不进行更新
        user.setAvatar(null);
        return toAjax(userService.updateUserProfile(user));
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        return toAjax(userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)));
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping(value = {"/avatar"})
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file, @RequestParam(value = "userId", required = false) Long userId) throws IOException {
        if (!file.isEmpty()) {
            SysUser user = new SysUser();
            if (userId != null) {
                user = userService.selectUserById(userId);
            } else {
                LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
                user = loginUser.getUser();
            }
//            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
            if (userService.updateUserAvatar(user.getUserName(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                user.setAvatar(avatar);
                // 表示当前用户登录
                if (user.getUserName().equals(SecurityUtils.getUsername())) {
                    LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
                    loginUser.setUser(user);
                    tokenService.setLoginUser(loginUser);
                }
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
