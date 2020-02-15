package com.ruoyi.project.elective.student.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.Na;

/**
 * student对象 elective_student
 *
 * @author Sunss
 * @date 2020-02-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveStudent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 关联的用户
     */
    private Long userId;

    /**
     * name
     */
    @Excel(name = "姓名")
    private String name;

    /**
     * 登录名称
     */
    @Excel(name = "登录名称")
    private String userName;

    /**
     * 密码
     */
    @Excel(name = "登录密码", type = Excel.Type.IMPORT)
    private String password;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 状态
     */
    private String status;

    /**
     * 班级(部门)Id
     */
    private Long deptId;

    /**
     * 年级名
     */
    @Excel(name = "年级")
    private String gradeName;

    /**
     * 班级名
     */
    @Excel(name = "班级")
    private String className;




}
