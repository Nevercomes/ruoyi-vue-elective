package com.ruoyi.project.elective.student.controller;

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
import com.ruoyi.project.elective.student.domain.ElectiveStudent;
import com.ruoyi.project.elective.student.service.IElectiveStudentService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * studentController
 *
 * @author Sunss
 * @date 2020-02-11
 */
@RestController
@RequestMapping("/elective/student")
public class ElectiveStudentController extends BaseController {
    @Autowired
    private IElectiveStudentService electiveStudentService;

    /**
     * 查询student列表
     */
    @PreAuthorize("@ss.hasPermi('elective:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveStudent electiveStudent) {
        startPage();
        List<ElectiveStudent> list = electiveStudentService.selectElectiveStudentList(electiveStudent);
        return getDataTable(list);
    }

    /**
     * 导出student列表
     */
    @PreAuthorize("@ss.hasPermi('elective:student:export')")
    @Log(title = "student", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveStudent electiveStudent) {
        List<ElectiveStudent> list = electiveStudentService.selectElectiveStudentList(electiveStudent);
        ExcelUtil<ElectiveStudent> util = new ExcelUtil<ElectiveStudent>(ElectiveStudent.class);
        return util.exportExcel(list, "student");
    }

    /**
     * 获取student详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:student:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(electiveStudentService.selectElectiveStudentById(id));
    }

    /**
     * 新增student
     */
    @PreAuthorize("@ss.hasPermi('elective:student:add')")
    @Log(title = "student", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveStudent electiveStudent) {
        return toAjax(electiveStudentService.insertElectiveStudent(electiveStudent));
    }

    /**
     * 修改student
     */
    @PreAuthorize("@ss.hasPermi('elective:student:edit')")
    @Log(title = "student", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveStudent electiveStudent) {
        return toAjax(electiveStudentService.updateElectiveStudent(electiveStudent));
    }

    /**
     * 删除student
     */
    @PreAuthorize("@ss.hasPermi('elective:student:remove')")
    @Log(title = "student", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveStudentService.deleteElectiveStudentByIds(ids));
    }
}
