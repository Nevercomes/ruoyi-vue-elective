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
import com.ruoyi.project.elective.course.domain.ElectiveCoursePeople;
import com.ruoyi.project.elective.course.service.IElectiveCoursePeopleService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 选课人数Controller
 *
 * @author Sunss
 * @date 2020-02-14
 */
@RestController
@RequestMapping("/elective/people")
public class ElectiveCoursePeopleController extends BaseController {
    @Autowired
    private IElectiveCoursePeopleService electiveCoursePeopleService;

    /**
     * 查询选课人数列表
     */
    @PreAuthorize("@ss.hasPermi('elective:people:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveCoursePeople electiveCoursePeople) {
        startPage();
        List<ElectiveCoursePeople> list = electiveCoursePeopleService.selectElectiveCoursePeopleList(electiveCoursePeople);
        return getDataTable(list);
    }
    /**
     * 统计选课人数
     */
    @PreAuthorize("@ss.hasPermi('elective:people:statistic')")
    @GetMapping("/statistic")
    public TableDataInfo statistic(ElectiveCoursePeople electiveCoursePeople) {
        startPage();
        List<ElectiveCoursePeople> list = electiveCoursePeopleService.listStatistic(electiveCoursePeople);
        return getDataTable(list);
    }


    /**
     * 导出选课人数列表
     */
    @PreAuthorize("@ss.hasPermi('elective:people:export')")
    @Log(title = "选课人数", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveCoursePeople electiveCoursePeople) {
        List<ElectiveCoursePeople> list = electiveCoursePeopleService.listStatistic(electiveCoursePeople);
        ExcelUtil<ElectiveCoursePeople> util = new ExcelUtil<ElectiveCoursePeople>(ElectiveCoursePeople.class);
        return util.exportExcel(list, "选课统计");
    }

    /**
     * 获取选课人数详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:people:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(electiveCoursePeopleService.selectElectiveCoursePeopleById(id));
    }

    /**
     * 新增选课人数
     */
    @PreAuthorize("@ss.hasPermi('elective:people:add')")
    @Log(title = "选课人数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveCoursePeople electiveCoursePeople) {
        return toAjax(electiveCoursePeopleService.insertElectiveCoursePeople(electiveCoursePeople));
    }

    /**
     * 修改选课人数
     */
    @PreAuthorize("@ss.hasPermi('elective:people:edit')")
    @Log(title = "选课人数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveCoursePeople electiveCoursePeople) {
        return toAjax(electiveCoursePeopleService.updateElectiveCoursePeople(electiveCoursePeople));
    }

    /**
     * 删除选课人数
     */
    @PreAuthorize("@ss.hasPermi('elective:people:remove')")
    @Log(title = "选课人数", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveCoursePeopleService.deleteElectiveCoursePeopleByIds(ids));
    }
}
