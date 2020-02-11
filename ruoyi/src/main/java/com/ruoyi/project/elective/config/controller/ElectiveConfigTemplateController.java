package com.ruoyi.project.elective.config.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.elective.config.domain.ElectiveConfigTemplate;
import com.ruoyi.project.elective.config.service.IElectiveConfigTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置模板Controller
 *
 * @author Sunss
 * @date 2020-02-08
 */
@RestController
@RequestMapping("/elective/config/template")
public class ElectiveConfigTemplateController extends BaseController {
    @Autowired
    private IElectiveConfigTemplateService electiveConfigTemplateService;

    /**
     * 查询配置模板列表
     */
    @PreAuthorize("@ss.hasPermi('config:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveConfigTemplate electiveConfigTemplate) {
        startPage();
        List<ElectiveConfigTemplate> list = electiveConfigTemplateService.selectElectiveConfigTemplateList(electiveConfigTemplate);
        return getDataTable(list);
    }

    /**
     * 获取配置模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(electiveConfigTemplateService.selectElectiveConfigTemplateById(id));
    }

    /**
     * 新增配置模板
     */
    @PreAuthorize("@ss.hasPermi('config:template:add')")
    @Log(title = "配置模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveConfigTemplate electiveConfigTemplate) {
        return toAjax(electiveConfigTemplateService.insertElectiveConfigTemplate(electiveConfigTemplate));
    }

    /**
     * 修改配置模板
     */
    @PreAuthorize("@ss.hasPermi('config:template:edit')")
    @Log(title = "配置模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveConfigTemplate electiveConfigTemplate) {
        return toAjax(electiveConfigTemplateService.updateElectiveConfigTemplate(electiveConfigTemplate));
    }

    /**
     * 修改配置模板状态
     */
    @PreAuthorize("@ss.hasPermi('config:template:edit')")
    @Log(title = "配置模板", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult changeStatus(@RequestBody ElectiveConfigTemplate electiveConfigTemplate) {
        return toAjax(electiveConfigTemplateService.updateElectiveConfigTemplate(electiveConfigTemplate));
    }

    /**
     * 删除配置模板
     */
    @PreAuthorize("@ss.hasPermi('config:template:remove')")
    @Log(title = "配置模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveConfigTemplateService.deleteElectiveConfigTemplateByIds(ids));
    }

}
