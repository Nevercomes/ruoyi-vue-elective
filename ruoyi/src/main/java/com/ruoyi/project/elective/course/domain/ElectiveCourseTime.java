package com.ruoyi.project.elective.course.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sunss
 * @since 2020/4/9
 */
@Data
@NoArgsConstructor
public class ElectiveCourseTime {
    private Long id;
    private Long courseId;
    private Long weekId;
    private Long timeId;

    private String weekName;
    private String timeName;

    public ElectiveCourseTime(Long courseId) {
        this.courseId = courseId;
    }

}
