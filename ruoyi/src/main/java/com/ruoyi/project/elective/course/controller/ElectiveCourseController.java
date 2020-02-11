package com.ruoyi.project.elective.course.controller;

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
import com.ruoyi.project.elective.course.domain.ElectiveCourse;
import com.ruoyi.project.elective.course.service.IElectiveCourseService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * courseController
 * 
 * @author Sunss
 * @date 2020-02-11
 */
@RestController
@RequestMapping("/elective/course")
public class ElectiveCourseController extends BaseController
{
    @Autowired
    private IElectiveCourseService electiveCourseService;

    /**
     * 查询course列表
     */
    @PreAuthorize("@ss.hasPermi('elective:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveCourse electiveCourse)
    {
        startPage();
        List<ElectiveCourse> list = electiveCourseService.selectElectiveCourseList(electiveCourse);
        return getDataTable(list);
    }

    /**
     * 导出course列表
     */
    @PreAuthorize("@ss.hasPermi('elective:course:export')")
    @Log(title = "course", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveCourse electiveCourse)
    {
        List<ElectiveCourse> list = electiveCourseService.selectElectiveCourseList(electiveCourse);
        ExcelUtil<ElectiveCourse> util = new ExcelUtil<ElectiveCourse>(ElectiveCourse.class);
        return util.exportExcel(list, "course");
    }

    /**
     * 获取course详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:course:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(electiveCourseService.selectElectiveCourseById(id));
    }

    /**
     * 新增course
     */
    @PreAuthorize("@ss.hasPermi('elective:course:add')")
    @Log(title = "course", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveCourse electiveCourse)
    {
        return toAjax(electiveCourseService.insertElectiveCourse(electiveCourse));
    }

    /**
     * 修改course
     */
    @PreAuthorize("@ss.hasPermi('elective:course:edit')")
    @Log(title = "course", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveCourse electiveCourse)
    {
        return toAjax(electiveCourseService.updateElectiveCourse(electiveCourse));
    }

    /**
     * 删除course
     */
    @PreAuthorize("@ss.hasPermi('elective:course:remove')")
    @Log(title = "course", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(electiveCourseService.deleteElectiveCourseByIds(ids));
    }
}
