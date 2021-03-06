package com.ruoyi.project.elective.config.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.elective.config.domain.ElectiveConfigTemplate;
import com.ruoyi.project.elective.config.domain.ElectiveConfigValue;
import com.ruoyi.project.elective.config.mapper.ElectiveConfigTemplateMapper;
import com.ruoyi.project.elective.config.mapper.ElectiveConfigValueMapper;
import com.ruoyi.project.elective.config.service.IElectiveConfigTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配置模板Service业务层处理
 *
 * @author Sunss
 * @date 2020-02-08
 */
@Service
public class ElectiveConfigTemplateServiceImpl implements IElectiveConfigTemplateService {
    @Autowired
    private ElectiveConfigTemplateMapper electiveConfigTemplateMapper;
    @Autowired
    private ElectiveConfigValueMapper electiveConfigValueMapper;

    /**
     * 查询配置模板
     *
     * @param id 配置模板ID
     * @return 配置模板
     */
    @Override
    public ElectiveConfigTemplate selectElectiveConfigTemplateById(Long id) {
        return electiveConfigTemplateMapper.selectElectiveConfigTemplateById(id);
    }

    /**
     * 查询配置模板列表
     *
     * @param electiveConfigTemplate 配置模板
     * @return 配置模板
     */
    @Override
    public List<ElectiveConfigTemplate> selectElectiveConfigTemplateList(ElectiveConfigTemplate electiveConfigTemplate) {
        return electiveConfigTemplateMapper.selectElectiveConfigTemplateList(electiveConfigTemplate);
    }

    /**
     * 新增配置模板
     *
     * @param electiveConfigTemplate 配置模板
     * @return 结果
     */
    @Override
    public int insertElectiveConfigTemplate(ElectiveConfigTemplate electiveConfigTemplate) {
        electiveConfigTemplate.setCreateTime(DateUtils.getNowDate());
        return electiveConfigTemplateMapper.insertElectiveConfigTemplate(electiveConfigTemplate);
    }

    /**
     * 修改配置模板
     *
     * @param electiveConfigTemplate 配置模板
     * @return 结果
     */
    @Override
    public int updateElectiveConfigTemplate(ElectiveConfigTemplate electiveConfigTemplate) {
        electiveConfigTemplate.setUpdateTime(DateUtils.getNowDate());
        return electiveConfigTemplateMapper.updateElectiveConfigTemplate(electiveConfigTemplate);
    }

    /**
     * 批量删除配置模板
     *
     * @param ids 需要删除的配置模板ID
     * @return 结果
     */
    @Override
    public int deleteElectiveConfigTemplateByIds(Long[] ids) {
        return electiveConfigTemplateMapper.deleteElectiveConfigTemplateByIds(ids);
    }

    /**
     * 删除配置模板信息
     *
     * @param id 配置模板ID
     * @return 结果
     */
    @Override
    public int deleteElectiveConfigTemplateById(Long id) {
        return electiveConfigTemplateMapper.deleteElectiveConfigTemplateById(id);
    }

    /**
     * 根据类型获取学校使用中的模板
     * 若有多个status为1 则按照sort去最前面的一个
     * @param electiveConfigTemplate
     * @return
     */
    @Override
    public ElectiveConfigTemplate selectInUseTemplate(ElectiveConfigTemplate electiveConfigTemplate) {
        return electiveConfigTemplateMapper.selectInUseTemplate(electiveConfigTemplate);
    }
}
