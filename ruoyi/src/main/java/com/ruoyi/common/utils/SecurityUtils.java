package com.ruoyi.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.framework.security.LoginUser;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
public class SecurityUtils {
    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new CustomException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户id
     */
    public static Long getLoginUserId() {
        try {
            return getLoginUser().getUser().getUserId();
        } catch (Exception e) {
            throw new CustomException("获取用户id异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取当前的登陆学校
     */
    public static Long getSchoolId() {
        try {
            return getLoginUser().getSchoolId();
        } catch (Exception e) {
            throw new CustomException("获取用户登录校区异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取学生id
     * @return
     */
    public static Long getStudentId() {
        try {
            return getLoginUser().getStudentId();
        } catch (Exception e) {
            throw new CustomException("获取用户学生Id异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取教师id
     * @return
     */
    public static Long getTeacherId() {
        try {
            return getLoginUser().getTeacherId();
        } catch (Exception e) {
            throw new CustomException("获取用户教师Id异常", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 是否为管理员
     *
     * @return 结果
     */
    public static boolean isAdmin() {
        return getLoginUser().getUser().getUserId() == 1L;
    }
}
