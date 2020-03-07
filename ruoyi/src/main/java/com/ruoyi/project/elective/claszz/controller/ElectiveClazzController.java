package com.ruoyi.project.elective.claszz.controller;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.elective.claszz.service.IElectiveClazzService;
import com.ruoyi.project.system.domain.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Sunss
 * @since 2020/2/13
 */
@RestController
@RequestMapping("/elective/clazz")
public class ElectiveClazzController extends BaseController {

    @Autowired
    private IElectiveClazzService electiveClazzService;

    /**
     * 获取班级列表
     */
    @PreAuthorize("@ss.hasPermi('elective:clazz:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = electiveClazzService.selectDeptList(dept);
        return AjaxResult.success(electiveClazzService.buildDeptTree(depts));
    }

    /**
     * 根据班级编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:clazz:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(electiveClazzService.selectDeptById(deptId));
    }

    /**
     * 获取班级下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept) {
        List<SysDept> depts = electiveClazzService.selectDeptList(dept);
        return AjaxResult.success(electiveClazzService.buildDeptTreeSelect(depts));
    }

//    /**
//     * 加载对应角色部门列表树
//     */
//    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
//    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
//        return AjaxResult.success(deptService.selectDeptListByRoleId(roleId));
//    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('elective:clazz:add')")
    @Log(title = "班级管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        String typeName = electiveClazzService.getTypeName(dept.getType());
//        if (UserConstants.NOT_UNIQUE.equals(electiveClazzService.checkDeptNameUnique(dept))) {
//            return AjaxResult.error(StringUtils.format("新增{}'{}'失败，{}名称已存在", typeName, dept.getDeptName(), typeName));
//        }
        if (dept.getParentId().equals(dept.getDeptId())) {
            return AjaxResult.error(StringUtils.format("修改{}'{}'失败，上级不能是自己", typeName, dept.getDeptName()));
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return toAjax(electiveClazzService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('elective:clazz:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        String typeName = electiveClazzService.getTypeName(dept.getType());
//        if (UserConstants.NOT_UNIQUE.equals(electiveClazzService.checkDeptNameUnique(dept))) {
//            return AjaxResult.error(StringUtils.format("修改{}'{}'失败，{}名称已存在", typeName, dept.getDeptName(), typeName));
//        } else
        if (dept.getParentId().equals(dept.getDeptId())) {
            return AjaxResult.error(StringUtils.format("修改{}'{}'失败，上级不能是自己", typeName, dept.getDeptName()));
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(electiveClazzService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('elective:clazz:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (electiveClazzService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级,不允许删除");
        }
        if (electiveClazzService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("存在用户,不允许删除");
        }
        return toAjax(electiveClazzService.deleteDeptById(deptId));
    }

    /**
     * 获取年级列表
     */
    @GetMapping("/grade")
    public AjaxResult listGrade() {
        return AjaxResult.success(electiveClazzService.getGradeList());
    }

    /**
     * 获取班级列表
     */
    @GetMapping("/clazz")
    public AjaxResult listClazz() {
        return AjaxResult.success(electiveClazzService.getClassList());
    }

}
