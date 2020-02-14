package com.ruoyi.project.elective.config.controller;

import java.util.List;

import com.ruoyi.common.constant.ElectiveDict;
import com.ruoyi.project.elective.config.domain.ElectiveConfigTemplate;
import com.ruoyi.project.elective.config.domain.ElectiveConfigValue;
import com.ruoyi.project.elective.config.service.IElectiveConfigTemplateService;
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
import com.ruoyi.project.elective.config.service.IElectiveConfigValueService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 模板内容Controller
 *
 * @author Sunss
 * @date 2020-02-08
 */
@RestController
@RequestMapping("/elective/config/value")
public class ElectiveConfigValueController extends BaseController {
    @Autowired
    private IElectiveConfigValueService electiveConfigValueService;

    @Autowired
    private IElectiveConfigTemplateService electiveConfigTemplateService;

    /**
     * 查询模板内容列表
     */
    @PreAuthorize("@ss.hasPermi('config:value:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveConfigValue electiveConfigValue) {
        startPage();
        List<ElectiveConfigValue> list = electiveConfigValueService.selectElectiveConfigValueList(electiveConfigValue);
        return getDataTable(list);
    }

    /**
     * 查询模板内容列表
     */
    @PreAuthorize("@ss.hasPermi('config:value:list')")
    @GetMapping("/list/semester")
    public AjaxResult listSemester(ElectiveConfigValue electiveConfigValue) {
        ElectiveConfigTemplate template = electiveConfigTemplateService.selectInUseTemplate(ElectiveDict.CONFIG_TEMPLATE_SEMESTER);
        electiveConfigValue.setTemplateId(template.getId());
        startPage();
        List<ElectiveConfigValue> list = electiveConfigValueService.selectElectiveConfigValueList(electiveConfigValue);
        return AjaxResult.success(list);
    }

    /**
     * 查询模板内容列表
     */
    @PreAuthorize("@ss.hasPermi('config:value:list')")
    @GetMapping("/list/classTime")
    public AjaxResult listClassTime(ElectiveConfigValue electiveConfigValue) {
        ElectiveConfigTemplate template = electiveConfigTemplateService.selectInUseTemplate(ElectiveDict.CONFIG_TEMPLATE_CLASSTIME);
        electiveConfigValue.setTemplateId(template.getId());
        startPage();
        List<ElectiveConfigValue> list = electiveConfigValueService.selectElectiveConfigValueList(electiveConfigValue);
        return AjaxResult.success(list);
    }

    /**
     * 导出模板内容列表
     */
    @PreAuthorize("@ss.hasPermi('config:value:export')")
    @Log(title = "模板内容", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveConfigValue electiveConfigValue) {
        List<ElectiveConfigValue> list = electiveConfigValueService.selectElectiveConfigValueList(electiveConfigValue);
        ExcelUtil<ElectiveConfigValue> util = new ExcelUtil<ElectiveConfigValue>(ElectiveConfigValue.class);
        return util.exportExcel(list, "value");
    }

    /**
     * 获取模板内容详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:value:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(electiveConfigValueService.selectElectiveConfigValueById(id));
    }

    /**
     * 新增模板内容
     */
    @PreAuthorize("@ss.hasPermi('config:value:add')")
    @Log(title = "模板内容", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveConfigValue electiveConfigValue) {
        return toAjax(electiveConfigValueService.insertElectiveConfigValue(electiveConfigValue));
    }

    /**
     * 修改模板内容
     */
    @PreAuthorize("@ss.hasPermi('config:value:edit')")
    @Log(title = "模板内容", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveConfigValue electiveConfigValue) {
        return toAjax(electiveConfigValueService.updateElectiveConfigValue(electiveConfigValue));
    }

    /**
     * 删除模板内容
     */
    @PreAuthorize("@ss.hasPermi('config:value:remove')")
    @Log(title = "模板内容", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveConfigValueService.deleteElectiveConfigValueByIds(ids));
    }
}
