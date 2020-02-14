package com.ruoyi.project.elective.claszz.service.impl;

import com.ruoyi.common.constant.ElectiveDict;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.project.elective.claszz.service.IElectiveClazzService;
import org.springframework.stereotype.Service;

/**
 * @author Sunss
 * @since 2020/2/13
 */
@Service
public class ElectiveClazzServiceImpl implements IElectiveClazzService {
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
}
