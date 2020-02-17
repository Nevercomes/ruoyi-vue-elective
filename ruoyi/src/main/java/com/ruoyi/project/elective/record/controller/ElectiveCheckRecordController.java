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
import com.ruoyi.project.elective.record.domain.ElectiveCheckRecord;
import com.ruoyi.project.elective.record.service.IElectiveCheckRecordService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * checkController
 *
 * @author Sunss
 * @date 2020-02-11
 */
@RestController
@RequestMapping("/elective/check")
public class ElectiveCheckRecordController extends BaseController {
    @Autowired
    private IElectiveCheckRecordService electiveCheckRecordService;

    /**
     * 查询check列表
     */
    @PreAuthorize("@ss.hasPermi('elective:check:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveCheckRecord electiveCheckRecord) {
        startPage();
        List<ElectiveCheckRecord> list = electiveCheckRecordService.selectElectiveCheckRecordList(electiveCheckRecord);
        return getDataTable(list);
    }

    /**
     * 导出check列表
     */
    @PreAuthorize("@ss.hasPermi('elective:check:export')")
    @Log(title = "申请审核", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveCheckRecord electiveCheckRecord) {
        List<ElectiveCheckRecord> list = electiveCheckRecordService.selectElectiveCheckRecordList(electiveCheckRecord);
        ExcelUtil<ElectiveCheckRecord> util = new ExcelUtil<ElectiveCheckRecord>(ElectiveCheckRecord.class);
        return util.exportExcel(list, "check");
    }

    /**
     * 获取check详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:check:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(electiveCheckRecordService.selectElectiveCheckRecordById(id));
    }

    /**
     * 新增check
     */
    @PreAuthorize("@ss.hasPermi('elective:check:add')")
    @Log(title = "申请审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveCheckRecord electiveCheckRecord) {
        return toAjax(electiveCheckRecordService.insertElectiveCheckRecord(electiveCheckRecord));
    }

    /**
     * 修改check
     */
    @PreAuthorize("@ss.hasPermi('elective:check:edit')")
    @Log(title = "申请审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveCheckRecord electiveCheckRecord) {
        return toAjax(electiveCheckRecordService.updateElectiveCheckRecord(electiveCheckRecord));
    }

    /**
     * 删除check
     */
    @PreAuthorize("@ss.hasPermi('elective:check:remove')")
    @Log(title = "申请审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveCheckRecordService.deleteElectiveCheckRecordByIds(ids));
    }
}
