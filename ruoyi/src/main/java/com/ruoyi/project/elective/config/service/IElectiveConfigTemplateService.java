package com.ruoyi.project.elective.config.service;

import com.ruoyi.project.elective.config.domain.ElectiveConfigTemplate;

import java.util.List;

/**
 * 配置模板Service接口
 *
 * @author Sunss
 * @date 2020-02-08
 */
public interface IElectiveConfigTemplateService {
    /**
     * 查询配置模板
     *
     * @param id 配置模板ID
     * @return 配置模板
     */
    public ElectiveConfigTemplate selectElectiveConfigTemplateById(Long id);

    /**
     * 查询配置模板列表
     *
     * @param electiveConfigTemplate 配置模板
     * @return 配置模板集合
     */
    public List<ElectiveConfigTemplate> selectElectiveConfigTemplateList(ElectiveConfigTemplate electiveConfigTemplate);

    /**
     * 新增配置模板
     *
     * @param electiveConfigTemplate 配置模板
     * @return 结果
     */
    public int insertElectiveConfigTemplate(ElectiveConfigTemplate electiveConfigTemplate);

    /**
     * 修改配置模板
     *
     * @param electiveConfigTemplate 配置模板
     * @return 结果
     */
    public int updateElectiveConfigTemplate(ElectiveConfigTemplate electiveConfigTemplate);

    /**
     * 批量删除配置模板
     *
     * @param ids 需要删除的配置模板ID
     * @return 结果
     */
    public int deleteElectiveConfigTemplateByIds(Long[] ids);

    /**
     * 删除配置模板信息
     *
     * @param id 配置模板ID
     * @return 结果
     */
    public int deleteElectiveConfigTemplateById(Long id);

    /**
     * 根据类型获取学校使用中的模板
     * 若有多个status为1 则按照sort去最前面的一个
     * @param type
     * @return
     */
    public ElectiveConfigTemplate selectInUseTemplate(String type);
}
