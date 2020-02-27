<template>
  <div class="app-container mobile-container" ref="MobileContainer">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="学年学期" prop="semesterId">
        <el-select v-model="queryParams.semesterId" placeholder="请选择学年学期" clearable size="small" @change="handleQuery">
          <el-option v-for="item in semesterOptions" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="年级" prop="gradeId">
        <el-select v-model="queryParams.gradeId" placeholder="请选择年级" clearable size="small" @change="handleQuery">
          <el-option v-for="grade in gradeOptions" :key="grade.deptId" :value="grade.deptId" :label="grade.deptName"></el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" :topPullText="''"
      :bottomPullText="''" :bottomDropText="'释放加载'" :auto-fill="false" ref="loadmore">
      <ul>
        <li v-for="item in dataList" class="nl-crad-li">
          <nl-card :header="item.name">
            <nl-card-item :label="'课程'">
              <router-link :to="{path: '/mobile/select/record/', query: {courseId: item.courseId}}" class="link-type">
                <span>{{item.courseName}}</span>
              </router-link>
            </nl-card-item>
            <nl-card-item :label="'上课教师'" :value="item.teacherName"></nl-card-item>
            <nl-card-item :label="'年级'">
              <router-link :to="{path: '/mobile/select/record/', query: {courseId: item.courseId, gradeId: item.gradeId}}"
                class="link-type">
                <span>{{item.gradeName}}</span>
              </router-link>
            </nl-card-item>
            <nl-card-item :label="'计划招生'" :value="item.initNum + ''"></nl-card-item>
            <nl-card-item :label="'选课人数'" :value="item.selectNum + ''"></nl-card-item>
          </nl-card>
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
    exportCourse,
    listStatistic,
    exportStatistic
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
    name: "MobileStatistic",
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
          semesterId: undefined,
          gradeId: undefined
        }
      };
    },
    mounted() {
      this.setHeight()
    },
    created() {
      this.getList();
      listSemester().then(response => {
        this.semesterOptions = response.data;
      });
      listGrade().then(response => {
        this.gradeOptions = response.data
      })
    },
    methods: {
      /** 查询课程列表 */
      getList() {
        this.loading = true;
        this.noMore = false;
        listStatistic(this.queryParams).then(response => {
          this.dataList = this.dataList.concat(response.rows);
          this.total = response.total;
          this.loading = false;
          this.calNoMore()
        });
      },
      loadTop() {
        this.getList()
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
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.dataList = []
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      getSummaries(param) {
        const {
          columns,
          data
        } = param;
        const sums = [];
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
<style>
  .nl-crad-li {
    padding: 5px 0;
  }

  .card-footer-item {
    float: right;
  }
</style>
