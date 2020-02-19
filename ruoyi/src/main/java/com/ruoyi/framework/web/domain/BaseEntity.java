package com.ruoyi.framework.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author ruoyi
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @JsonIgnore
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据权限
     */
    private String dataScope;

    /**
     * 开始时间
     */
    @JsonIgnore
    private String beginTime;

    /**
     * 结束时间
     */
    @JsonIgnore
    private String endTime;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    /**
     * 登录的TeacherId
     */
    private Long loginTeacherId;

    /**
     * 登录的学生Id
     */
    private Long loginStudentId;

    /**
     * 对应部门表中的dept_id 这里代表学校
     */
    private Long schoolId;

    /**
     * 该方法在redis中的缓存值为-1
     * 在Authentication环境中为真实值
     */
    public Long getSchoolId() {
        try {
            return SecurityUtils.getSchoolId();
        } catch (Exception e) {
            return -1L;
        }
    }

    public Long getLoginTeacherId() {
        try {
            return SecurityUtils.getTeacherId();
        } catch (Exception e) {
            return -1L;
        }
    }

    public Long getLoginStudentId() {
        try {
            return SecurityUtils.getStudentId();
        } catch (Exception e) {
            return -1L;
        }
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    /**
     * 插入前设置通用值
     */
    public void preInsert() {
        this.setCreateBy(SecurityUtils.getUsername());
        this.setCreateTime(new Date());
        this.setUpdateBy(SecurityUtils.getUsername());
        this.setUpdateTime(new Date());
    }

    /**
     * 更新前设置通用值
     */
    public void preUpdate() {
        this.setUpdateBy(SecurityUtils.getUsername());
        this.setUpdateTime(new Date());
    }

}
