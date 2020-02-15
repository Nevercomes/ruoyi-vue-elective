package com.ruoyi.project.elective.open.controller;

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
import com.ruoyi.project.elective.open.domain.ElectiveOpenSelect;
import com.ruoyi.project.elective.open.service.IElectiveOpenSelectService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 开放选课Controller
 *
 * @author Sunss
 * @date 2020-02-15
 */
@RestController
@RequestMapping("/elective/open")
public class ElectiveOpenSelectController extends BaseController {
    @Autowired
    private IElectiveOpenSelectService electiveOpenSelectService;

    /**
     * 查询开放选课列表
     */
    @PreAuthorize("@ss.hasPermi('elective:open:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveOpenSelect electiveOpenSelect) {
        startPage();
        List<ElectiveOpenSelect> list = electiveOpenSelectService.selectElectiveOpenSelectList(electiveOpenSelect);
        return getDataTable(list);
    }

    /**
     * 导出开放选课列表
     */
    @PreAuthorize("@ss.hasPermi('elective:open:export')")
    @Log(title = "开放选课", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveOpenSelect electiveOpenSelect) {
        List<ElectiveOpenSelect> list = electiveOpenSelectService.selectElectiveOpenSelectList(electiveOpenSelect);
        ExcelUtil<ElectiveOpenSelect> util = new ExcelUtil<ElectiveOpenSelect>(ElectiveOpenSelect.class);
        return util.exportExcel(list, "open");
    }

    /**
     * 获取开放选课详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:open:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(electiveOpenSelectService.selectElectiveOpenSelectById(id));
    }

    /**
     * 新增开放选课
     */
    @PreAuthorize("@ss.hasPermi('elective:open:add')")
    @Log(title = "开放选课", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveOpenSelect electiveOpenSelect) {
        return toAjax(electiveOpenSelectService.insertElectiveOpenSelect(electiveOpenSelect));
    }

    /**
     * 修改开放选课
     */
    @PreAuthorize("@ss.hasPermi('elective:open:edit')")
    @Log(title = "开放选课", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveOpenSelect electiveOpenSelect) {
        return toAjax(electiveOpenSelectService.updateElectiveOpenSelect(electiveOpenSelect));
    }

    /**
     * 删除开放选课
     */
    @PreAuthorize("@ss.hasPermi('elective:open:remove')")
    @Log(title = "开放选课", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveOpenSelectService.deleteElectiveOpenSelectByIds(ids));
    }

    /**
     * 更改开放选课状态
     */
    @PreAuthorize("@ss.hasPermi('elective:open:edit')")
    @Log(title = "开放选课", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult changeStatus(@RequestBody ElectiveOpenSelect electiveOpenSelect) {
        return toAjax(electiveOpenSelectService.updateElectiveOpenSelect(electiveOpenSelect));
    }
}
