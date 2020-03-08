package com.ruoyi.project.elective.clazz.service.impl;

import com.ruoyi.common.constant.ElectiveDict;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.elective.clazz.service.IElectiveClazzService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sunss
 * @since 2020/2/13
 */
@Service
public class ElectiveClazzServiceImpl implements IElectiveClazzService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysDeptService sysDeptService;

    public static final String GRADE_CACHE_KEY = "gradeCache-";

    public static final String CLAZZ_CACHE_KEY = "clazzCache-";

    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return sysDeptService.selectDeptList(dept);
    }

    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        return sysDeptService.buildDeptTree(depts);
    }

    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        return sysDeptService.buildDeptTreeSelect(depts);
    }

    @Override
    public List<Integer> selectDeptListByRoleId(Long roleId) {
        return sysDeptService.selectDeptListByRoleId(roleId);
    }

    @Override
    public SysDept selectDeptById(Long deptId) {
        return sysDeptService.selectDeptById(deptId);
    }

    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return sysDeptService.hasChildByDeptId(deptId);
    }

    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return sysDeptService.checkDeptExistUser(deptId);
    }

    @Override
    public String checkDeptNameUnique(SysDept dept) {
        return sysDeptService.checkDeptNameUnique(dept);
    }

    @Override
    public int insertDept(SysDept dept) {
        int res = sysDeptService.insertDept(dept);
        refreshCache();
        return res;
    }

    @Override
    public int updateDept(SysDept dept) {
        int res = sysDeptService.updateDept(dept);
        if (needRefresh(dept.getDeptId()))
            refreshCache();
        return res;
    }

    @Override
    public int deleteDeptById(Long deptId) {
        int res = sysDeptService.deleteDeptById(deptId);
        if (needRefresh(deptId))
            refreshCache();
        return res;
    }

    @Override
    public List<SysDept> getGradeList() {
        SysDept query = new SysDept();
        query.setType(ElectiveDict.DEPT_TYPE_GRADE);
        List<SysDept> list = redisCache.getCacheList(GRADE_CACHE_KEY + SecurityUtils.getSchoolId());
        if (list == null || list.size() == 0) {
            list = sysDeptService.selectDeptList(query);
            redisCache.setCacheList(GRADE_CACHE_KEY + SecurityUtils.getSchoolId(), list);
        }
        return list;
    }

    @Override
    public List<SysDept> getClassList() {
        SysDept query = new SysDept();
        query.setType(ElectiveDict.DEPT_TYPE_CLASS);
        List<SysDept> list = redisCache.getCacheList(CLAZZ_CACHE_KEY + SecurityUtils.getSchoolId());
        if (list == null || list.size() == 0) {
            list = sysDeptService.selectDeptList(query);
            redisCache.setCacheList(CLAZZ_CACHE_KEY + SecurityUtils.getSchoolId(), list);
        }
        return list;
    }

    /**
     * 获取dept的操作类型
     *
     * @param type
     * @return
     */
    @Override
    public String getTypeName(String type) {
        if (ElectiveDict.DEPT_TYPE_CLASS.equals(type)) {
            return "班级";
        } else if (ElectiveDict.DEPT_TYPE_GRADE.equals(type)) {
            return "年级";
        } else {
            throw new CustomException("不支持的操作类别");
        }
    }

    private void refreshCache() {
        SysDept query = new SysDept();
        query.setType(ElectiveDict.DEPT_TYPE_GRADE);
        redisCache.deleteObject(GRADE_CACHE_KEY + SecurityUtils.getSchoolId());
        redisCache.deleteObject(CLAZZ_CACHE_KEY + SecurityUtils.getSchoolId());
        List<SysDept> gradeList = sysDeptService.selectDeptList(query);
        redisCache.setCacheList(GRADE_CACHE_KEY + SecurityUtils.getSchoolId(), gradeList);
        query.setType(ElectiveDict.DEPT_TYPE_CLASS);
        List<SysDept> classList = sysDeptService.selectDeptList(query);
        redisCache.setCacheList(CLAZZ_CACHE_KEY + SecurityUtils.getSchoolId(), classList);
    }

    private boolean needRefresh(Long deptId) {
        List<SysDept> gradeList = redisCache.getCacheList(GRADE_CACHE_KEY + SecurityUtils.getSchoolId());
        List<SysDept> clazzList = redisCache.getCacheList(CLAZZ_CACHE_KEY + SecurityUtils.getSchoolId());
        for (SysDept g : gradeList) {
            if (g.getDeptId().equals(deptId)) {
                return true;
            }
        }
        for (SysDept c : clazzList) {
            if (c.getDeptId().equals(deptId)) {
                return true;
            }
        }
        return false;
    }
}
