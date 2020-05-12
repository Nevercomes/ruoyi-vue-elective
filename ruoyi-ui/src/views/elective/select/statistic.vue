<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="学年学期" prop="semesterId">
        <el-select v-model="queryParams.semesterId" placeholder="请选择学年学期" clearable size="small">
          <el-option v-for="item in semesterOptions" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="年级" prop="gradeId">
        <el-select v-model="queryParams.gradeId" placeholder="请选择年级" clearable size="small">
          <el-option v-for="grade in gradeOptions" :key="grade.deptId" :value="grade.deptId" :label="grade.deptName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课程" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择课程" clearable size="small">
          <el-option v-for="item in courseOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['elective:course:export']">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="courseList" show-summary :summary-method="getSummaries">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="课程" align="center" prop="courseId">
        <template slot-scope="scope">
          <router-link :to="{path: '/select/record/course/student/', query: {courseId: scope.row.courseId}}" class="link-type">
            <span>{{scope.row.courseName}}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="上课教师" align="center" prop="teacherName" />
      <el-table-column label="年级" align="center" prop="gradeName">
        <template slot-scope="scope">
          <router-link :to="{path: '/select/record/course/student/', query: {courseId: scope.row.courseId, gradeId: scope.row.gradeId}}"
            class="link-type">
            <!-- <el-button size="mini" type="text" icon="el-icon-view">学生</el-button> -->
            <span>{{scope.row.gradeName}}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="计划招生" align="center" prop="initNum" />
      <el-table-column label="选课人数" align="center" prop="selectNum" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

  </div>
</template>

<script>
  import {
    listCourse,
    getCourse,
    delCourse,
    addCourse,
    updateCourse,
    exportCourse,
    listStatistic,
    exportStatistic,
    listCoursePlainList
  } from "@/api/elective/course/course";
  import {
    listSemester,
    listClassTime
  } from "@/api/elective/config/value"
  import {
    listTeacher
  } from "@/api/elective/teacher/teacher"
  import {
    listClazz,
    listGrade
  } from "@/api/elective/clazz/clazz"
  import {
    inTime
  } from '@/utils/semester.js'

  export default {
    name: "Statistic",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 课程表格数据
        courseList: [],
        // 用于搜索的课程
        courseOptions: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 日期范围
        dateRange: [],
        // 课程状态字典值
        statusOptions: [],
        // 对应的开课时间 字典值字典
        semesterOptions: [],
        // 上课时间 字典值字典
        classTimeOptions: [],
        // 年级列表
        gradeOptions: [],
        // 教师列表
        teacherList: [],
        // 查询参数
        queryParams: {
          semesterId: undefined,
          gradeId: undefined,
          courseId: undefined
        }
      };
    },
    beforeRouteEnter(to, from, next) {
      if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        next({
          path: '/mobile/select/statistic'
        })
      } else next()
    },
    created() {
      listSemester().then(response => {
        this.semesterOptions = response.data;
        // 按名字处理学年学期（就很离谱）
        // forEach无法通过break终止循环
        for(let i in this.semesterOptions) {
          const s = this.semesterOptions[i]
          if(inTime(s.label)) {
            this.querySemesterId = s.id
            this.queryParams.semesterId = s.id
            break;
          }
        }
        this.getList();
      }).catch(() => {
        this.getList()
      });
      listGrade().then(response => {
        this.gradeOptions = response.data
      })
      listCoursePlainList().then(response => {
        this.courseOptions = response.rows
      })
    },
    methods: {
      /** 查询课程列表 */
      getList() {
        this.loading = true;
        listStatistic(this.queryParams).then(response => {
          this.courseList = response.rows;
          this.loading = false;
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有统计数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportStatistic(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
      getSummaries(param) {
        const {
          columns,
          data
        } = param;
        const sums = [];
        console.log(columns)
        console.log(data)
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计';
            return;
          }
          if (index === 1) {
            let idNum = 0
            let idSet = {}
            data.map(item => item[column.property]).forEach((id, index) => {
              if(!idSet[id]){
                idNum++
                idSet[id] = true
              }
            })
            sums[index] = idNum
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
          } else {
            sums[index] = '';
          }
        });
        return sums;
      }
    }
  };
</script>
