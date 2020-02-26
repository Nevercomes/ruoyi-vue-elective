<template>
  <div class="app-container mobile-container" ref="MobileContainer">
    <!-- <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="学年学期" prop="semesterId">
        <el-select v-model="queryParams.semesterId" placeholder="请选择学年学期" clearable size="small">
          <el-option v-for="item in semesterOptions" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>
	  <el-form-item label="课程" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入课程名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="教师" prop="teacherId" v-hasPermi="['sys:role:staff']">
        <el-select v-model="queryParams.teacherId" placeholder="请选择上课教师" clearable size="small">
          <el-option v-for="item in teacherList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="上课时间" prop="classTimeId">
        <el-select v-model="queryParams.classTimeId" placeholder="请选择上课时间" clearable size="small">
          <el-option v-for="item in classTimeOptions" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="年级" prop="gradeId">
        <el-select v-model="queryParams.gradeId" placeholder="请选择年级" clearable size="small">
          <el-option v-for="grade in gradeOptions" :key="grade.deptId" :value="grade.deptId" :label="grade.deptName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <router-link :to="'/mobile/course/form'">
          <el-button type="primary" icon="el-icon-plus" size="mini" v-hasPermi="['elective:course:add']">新增</el-button>
        </router-link>
      </el-col>
    </el-row>

    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" :topPullText="''"
      :bottomPullText="''" :bottomDropText="'释放加载'" :auto-fill="false" ref="loadmore">
      <ul>
        <li v-for="item in dataList" class="nl-crad-li">
          <router-link :to="{path: '/mobile/course/form', query: {courseId: item.id}}">
            <nl-card :header="item.name">
              <nl-card-item :label="'教师'" :value="item.teacherName"></nl-card-item>
              <nl-card-item :label="'学年学期'" :value="item.semester"></nl-card-item>
              <nl-card-item :label="'上课时间'" :value="item.classTime"></nl-card-item>
              <nl-card-item :label="'上课地点'" :value="item.classLocation"></nl-card-item>
              <nl-card-item :label="'计划招生'" :value="item.enrollPeo"></nl-card-item>
              <nl-card-item :label="'简介'" :value="item.intro"></nl-card-item>
              <nl-card-item :label="'目标'" :value="item.objective"></nl-card-item>
              <nl-card-item :label="'特别声明'" :value="item.specialNote"></nl-card-item>
              <nl-card-item :label="'状态'">
                <el-tag v-if="item.status == 0">{{statusFormat(item.status)}}</el-tag>
                <el-tag v-else-if="item.status == 1" type="success">{{statusFormat(item.status)}}</el-tag>
                <el-tag v-else-if="item.status == 2" type="danger">{{statusFormat(item.status)}}</el-tag>
              </nl-card-item>
              <!-- <router-link :to="'/mobile/select/student/course/' + item.id" class="link-type">
                <el-button  class="card-footer-item" size="mini" type="danger" icon="el-icon-delete" v-hasPermi="['elective:course:edit']">删除课程</el-button>
                <div class="clearfix"></div>
              </router-link> -->
            </nl-card>
          </router-link>
        </li>
      </ul>
    </mt-loadmore>
  </div>
</template>

<script>
  import {
    listCourse,
    getCourse,
    delCourse,
    addCourse,
    updateCourse,
    exportCourse
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
  import NlCard from "@/components/NaLo/nl-card"
  import NlCardItem from "@/components/NaLo/nl-card-item"

  export default {
    name: "MobileCourse",
    components: {
      NlCard,
      NlCardItem
    },
    data() {
      return {
        // 底部数据是否全部加载
        allLoaded: false,
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
        dataList: [],
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
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          status: undefined,
          teacherId: undefined,
          semesterId: undefined,
          classTimeId: undefined,
          gradeId: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [{
            required: true,
            message: "课程名不能为空",
            trigger: "blur"
          }],
          status: [{
            required: true,
            message: "课程状态不能为空",
            trigger: "blur"
          }],
          teacherId: [{
            required: true,
            message: "上课教师不能为空",
            trigger: "blur"
          }],
          semesterId: [{
            required: true,
            message: "学年学期不能为空",
            trigger: "blur"
          }]
        }
      };
    },
    created() {
      const teacherId = this.$route.params && this.$route.params.teacherId
      if (teacherId) this.queryParams.teacherId = Number(teacherId)
      this.getList();
      listSemester().then(response => {
        this.semesterOptions = response.data;
      });
      listClassTime().then(response => {
        this.classTimeOptions = response.data;
      });
      listTeacher().then(response => {
        this.teacherList = response.rows;
      })
      listGrade().then(response => {
        this.gradeOptions = response.data
      })
      this.getDicts("elective_course_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    mounted() {
      this.setHeight()
    },
    methods: {
      /** 查询课程列表 */
      getList() {
        this.loading = true;
        this.allLoaded = false
        listCourse(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.dataList = this.dataList.concat(response.rows)
          this.total = response.total;
          this.loading = false;
          this.calNoMore()
        });
      },
      // 课程状态字典翻译
      statusFormat(status) {
        return this.selectDictLabel(this.statusOptions, status);
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
      resetQuery() {
        this.resetForm("queryForm");
      },
      loadTop() {
        // this.getList()
        this.$refs.loadmore.onTopLoaded();
      },
      loadBottom() {
        this.calNoMore()
        if (this.allLoaded) return
        this.queryParams.pageNum = this.queryParams.pageNum + 1
        this.getList()
        this.$refs.loadmore.onBottomLoaded();
      },
      setHeight() {
        this.$refs.MobileContainer.style.height = (document.body.clientHeight - 86) + 'px'
      },
      calNoMore() {
        if (this.total == 0) {
          this.allLoaded = true
        } else {
          this.allLoaded = this.dataList.length >= this.total ? true : false
        }
      }
    }
  };
</script>
<style>
  .nl-crad-li {
    padding: 5px 0;
  }

  .card-footer-item {
    float: right;
  }
</style>
