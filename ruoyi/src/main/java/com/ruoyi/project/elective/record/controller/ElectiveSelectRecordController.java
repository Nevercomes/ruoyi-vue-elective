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
import com.ruoyi.project.elective.record.domain.ElectiveSelectRecord;
import com.ruoyi.project.elective.record.service.IElectiveSelectRecordService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * selectController
 * 
 * @author Sunss
 * @date 2020-02-11
 */
@RestController
@RequestMapping("/elective/select")
public class ElectiveSelectRecordController extends BaseController
{
    @Autowired
    private IElectiveSelectRecordService electiveSelectRecordService;

    /**
     * 查询select列表
     */
    @PreAuthorize("@ss.hasPermi('elective:select:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveSelectRecord electiveSelectRecord)
    {
        startPage();
        List<ElectiveSelectRecord> list = electiveSelectRecordService.selectElectiveSelectRecordList(electiveSelectRecord);
        return getDataTable(list);
    }

    /**
     * 导出select列表
     */
    @PreAuthorize("@ss.hasPermi('elective:select:export')")
    @Log(title = "select", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveSelectRecord electiveSelectRecord)
    {
        List<ElectiveSelectRecord> list = electiveSelectRecordService.selectElectiveSelectRecordList(electiveSelectRecord);
        ExcelUtil<ElectiveSelectRecord> util = new ExcelUtil<ElectiveSelectRecord>(ElectiveSelectRecord.class);
        return util.exportExcel(list, "select");
    }

    /**
     * 获取select详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:select:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(electiveSelectRecordService.selectElectiveSelectRecordById(id));
    }

    /**
     * 新增select
     */
    @PreAuthorize("@ss.hasPermi('elective:select:add')")
    @Log(title = "select", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveSelectRecord electiveSelectRecord)
    {
        return toAjax(electiveSelectRecordService.insertElectiveSelectRecord(electiveSelectRecord));
    }

    /**
     * 修改select
     */
    @PreAuthorize("@ss.hasPermi('elective:select:edit')")
    @Log(title = "select", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveSelectRecord electiveSelectRecord)
    {
        return toAjax(electiveSelectRecordService.updateElectiveSelectRecord(electiveSelectRecord));
    }

    /**
     * 删除select
     */
    @PreAuthorize("@ss.hasPermi('elective:select:remove')")
    @Log(title = "select", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(electiveSelectRecordService.deleteElectiveSelectRecordByIds(ids));
    }
}
