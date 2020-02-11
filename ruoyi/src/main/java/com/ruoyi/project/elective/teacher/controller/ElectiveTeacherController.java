package com.ruoyi.project.elective.teacher.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.elective.teacher.domain.ElectiveTeacher;
import com.ruoyi.project.elective.teacher.service.IElectiveTeacherService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 教师管理Controller
 * 
 * @author Sunss
 * @date 2020-02-11
 */
@RestController
@RequestMapping("/teacher/teacher")
public class ElectiveTeacherController extends BaseController
{
    @Autowired
    private IElectiveTeacherService electiveTeacherService;

    /**
     * 查询教师管理列表
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveTeacher electiveTeacher)
    {
        startPage();
        List<ElectiveTeacher> list = electiveTeacherService.selectElectiveTeacherList(electiveTeacher);
        return getDataTable(list);
    }

    /**
     * 导出教师管理列表
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:export')")
    @Log(title = "教师管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveTeacher electiveTeacher)
    {
        List<ElectiveTeacher> list = electiveTeacherService.selectElectiveTeacherList(electiveTeacher);
        ExcelUtil<ElectiveTeacher> util = new ExcelUtil<ElectiveTeacher>(ElectiveTeacher.class);
        return util.exportExcel(list, "teacher");
    }

    /**
     * 获取教师管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(electiveTeacherService.selectElectiveTeacherById(id));
    }

    /**
     * 新增教师管理
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:add')")
    @Log(title = "教师管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveTeacher electiveTeacher)
    {
        return toAjax(electiveTeacherService.insertElectiveTeacher(electiveTeacher));
    }

    /**
     * 修改教师管理
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:edit')")
    @Log(title = "教师管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveTeacher electiveTeacher)
    {
        return toAjax(electiveTeacherService.updateElectiveTeacher(electiveTeacher));
    }

    /**
     * 删除教师管理
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:remove')")
    @Log(title = "教师管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(electiveTeacherService.deleteElectiveTeacherByIds(ids));
    }
}
