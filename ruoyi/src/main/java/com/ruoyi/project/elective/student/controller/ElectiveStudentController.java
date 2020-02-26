package com.ruoyi.project.elective.student.controller;

import java.util.List;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.elective.teacher.domain.ElectiveTeacher;
import com.ruoyi.project.system.service.ISysUserService;
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
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private ISysUserService userService;

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

    @PreAuthorize("@ss.hasPermi('elective:student:import')")
    @Log(title = "学生管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ElectiveStudent> util = new ExcelUtil<ElectiveStudent>(ElectiveStudent.class);
        List<ElectiveStudent> studentList = util.importExcel(file.getInputStream());
        String message = electiveStudentService.importStudent(studentList, updateSupport, SecurityUtils.getUsername());
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<ElectiveStudent> util = new ExcelUtil<>(ElectiveStudent.class);
        return util.importTemplateExcel("学生数据模板");
    }

    /**
     * 导出student列表
     */
    @PreAuthorize("@ss.hasPermi('elective:student:export')")
    @Log(title = "学生管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectiveStudent electiveStudent) {
        List<ElectiveStudent> list = electiveStudentService.selectExportList(electiveStudent);
        ExcelUtil<ElectiveStudent> util = new ExcelUtil<ElectiveStudent>(ElectiveStudent.class);
        return util.exportExcel(list, "学生数据");
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
    @Log(title = "学生管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectiveStudent electiveStudent) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(electiveStudent.getUserName()))) {
            return AjaxResult.error("新增学生'" + electiveStudent.getUserName() + "'失败，登录账号已存在");
        }
        return toAjax(electiveStudentService.insertElectiveStudent(electiveStudent));
    }

    /**
     * 修改student
     */
    @PreAuthorize("@ss.hasPermi('elective:student:edit')")
    @Log(title = "学生管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectiveStudent electiveStudent) {
        return toAjax(electiveStudentService.updateElectiveStudent(electiveStudent));
    }

    /**
     * 删除student
     */
    @PreAuthorize("@ss.hasPermi('elective:student:remove')")
    @Log(title = "学生管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electiveStudentService.deleteElectiveStudentByIds(ids));
    }
}
