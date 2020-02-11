package com.ruoyi.project.elective.record.controller;

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
import com.ruoyi.project.elective.record.domain.ElectiveApplyRecord;
import com.ruoyi.project.elective.record.service.IElectiveApplyRecordService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * applyController
 *
 * @author Sunss
 * @date 2020-02-11
 */
@RestController
@RequestMapping("/elective/apply")
public class ElectiveApplyRecordController extends BaseController {
    @Autowired
    private IElectiveApplyRecordService electiveApplyRecordService;

    /**
     * 查询apply列表
     */
    @PreAuthorize("@ss.hasPermi('elective:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveApplyRecord electiveApplyRecord) {
        startPage();
        List<ElectiveApplyRecord> list = electiveApplyRecordService.selectElectiveApplyRecordList(electiveApplyRecord);
        return getDataTable(list);
    }

    /**
     * 导出apply列表
     */
    @PreAuthorize("@ss.hasPermi('elective:apply:export')")
    @Log(title = "apply", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveApplyRecord electiveApplyRecord) {
        List<ElectiveApplyRecord> list = electiveApplyRecordService.selectElectiveApplyRecordList(electiveApplyRecord);
        ExcelUtil<ElectiveApplyRecord> util = new ExcelUtil<ElectiveApplyRecord>(ElectiveApplyRecord.class);
        return util.exportExcel(list, "apply");
    }

    /**
     * 获取apply详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:apply:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(electiveApplyRecordService.selectElectiveApplyRecordById(id));
    }

    /**
     * 新增apply
     */
    @PreAuthorize("@ss.hasPermi('elective:apply:add')")
    @Log(title = "apply", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveApplyRecord electiveApplyRecord) {
        return toAjax(electiveApplyRecordService.insertElectiveApplyRecord(electiveApplyRecord));
    }

    /**
     * 修改apply
     */
    @PreAuthorize("@ss.hasPermi('elective:apply:edit')")
    @Log(title = "apply", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveApplyRecord electiveApplyRecord) {
        return toAjax(electiveApplyRecordService.updateElectiveApplyRecord(electiveApplyRecord));
    }

    /**
     * 删除apply
     */
    @PreAuthorize("@ss.hasPermi('elective:apply:remove')")
    @Log(title = "apply", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveApplyRecordService.deleteElectiveApplyRecordByIds(ids));
    }
}
