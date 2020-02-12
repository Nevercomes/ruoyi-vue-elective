package com.ruoyi.project.elective.teacher.controller;

import java.util.List;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * 教师管理Controller
 *
 * @author Sunss
 * @date 2020-02-11
 */
@RestController
@RequestMapping("/elective/teacher")
public class ElectiveTeacherController extends BaseController {
    @Autowired
    private IElectiveTeacherService electiveTeacherService;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询教师管理列表
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectiveTeacher electiveTeacher) {
        startPage();
        List<ElectiveTeacher> list = electiveTeacherService.selectElectiveTeacherList(electiveTeacher);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('elective:teacher:import')")
    @Log(title = "教师管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ElectiveTeacher> util = new ExcelUtil<ElectiveTeacher>(ElectiveTeacher.class);
        List<ElectiveTeacher> teacherList = util.importExcel(file.getInputStream());
        String message = electiveTeacherService.importTeacher(teacherList, updateSupport, SecurityUtils.getUsername());
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<ElectiveTeacher> util = new ExcelUtil<ElectiveTeacher>(ElectiveTeacher.class);
        return util.importTemplateExcel("教师数据模板");
    }

    /**
     * 导出教师管理列表
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:export')")
    @Log(title = "教师管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveTeacher electiveTeacher) {
        List<ElectiveTeacher> list = electiveTeacherService.selectElectiveTeacherList(electiveTeacher);
        ExcelUtil<ElectiveTeacher> util = new ExcelUtil<ElectiveTeacher>(ElectiveTeacher.class);
        return util.exportExcel(list, "教师数据");
    }

    /**
     * 获取教师管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(electiveTeacherService.selectElectiveTeacherById(id));
    }

    /**
     * 新增教师管理
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:add')")
    @Log(title = "教师管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ElectiveTeacher electiveTeacher) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(electiveTeacher.getUserName()))) {
            return AjaxResult.error("新增教师'" + electiveTeacher.getUserName() + "'失败，登录账号已存在");
        }
        return toAjax(electiveTeacherService.insertElectiveTeacher(electiveTeacher));
    }

    /**
     * 修改教师管理
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:edit')")
    @Log(title = "教师管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveTeacher electiveTeacher) {
        return toAjax(electiveTeacherService.updateElectiveTeacher(electiveTeacher));
    }

    /**
     * 删除教师管理
     */
    @PreAuthorize("@ss.hasPermi('elective:teacher:remove')")
    @Log(title = "教师管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveTeacherService.deleteElectiveTeacherByIds(ids));
    }
}
