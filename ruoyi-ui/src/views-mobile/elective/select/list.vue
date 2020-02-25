<template>
  <div class="app-container mobile-container" ref="MobileContainer">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item>
        <el-checkbox v-model="queryParams.onlyCan" v-hasPermi="['sys:role:student']">仅显示可选课程</el-checkbox>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" :topPullText="''"
      :bottomPullText="''" :bottomDropText="'释放加载'" :auto-fill="false" ref="loadmore">
      <div class="clearfix">
        <el-card class="course-card el-card" v-for="(item, index) in dataList" :key="index">
          <el-row>
            <el-col :span="12">
              <el-image style="width: 120px; height: 120px; border-radius: 50%;" :src="getAvatarUrl(item.teacherAvatar)"
                :fit="'contain'"></el-image>
              <!-- <img :src="item.teacherAvatar" style="width: 120px; height: 120px; border-radius: 50%;" /> -->
            </el-col>
            <el-col :span="12">
              <el-row class="card-item">
                <el-col :span="12"><span class="item-label">课程</span></el-col>
                <el-col :span="12"><span class="item-value">{{item.name}}</span></el-col>
              </el-row>
              <el-row class="card-item">
                <el-col :span="12"><span class="item-label">教师</span></el-col>
                <el-col :span="12"><span class="item-value">{{item.teacherName}}</span></el-col>
              </el-row>
              <el-row class="card-item">
                <el-col :span="12"><span class="item-label">上课时间</span></el-col>
                <el-col :span="12"><span class="item-value">{{item.classTime}}</span></el-col>
              </el-row>
              <el-row class="card-item">
                <el-col :span="12"><span class="item-label">上课地点</span></el-col>
                <el-col :span="12"><span class="item-value">{{item.classLocation}}</span></el-col>
              </el-row>
            </el-col>
          </el-row>
          <el-divider></el-divider>
          <div class="card-body">
            <el-row class="card-item">
              <el-col :span="6"><span class="item-label">选课人数</span></el-col>
              <el-col :span="18">
                <el-row>
                  <el-col :span="8" class="align-center"><span class="item-label-nomargin">年级</span></el-col>
                  <el-col :span="8" class="align-center"><span class="item-label-nomargin">可选</span></el-col>
                  <el-col :span="8" class="align-center"><span class="item-label-nomargin">剩余</span></el-col>
                </el-row>
                <el-row class="card-item" v-for="peo in item.peopleList" :key="peo.id">
                  <el-col :span="8" class="align-center" :class="peo.initNum == peo.selectNum ? 'forbid-select' : ''"><span
                      class="item-value">{{gradeFormat(peo.gradeId)}}</span></el-col>
                  <el-col :span="8" class="align-center" :class="peo.initNum == peo.selectNum ? 'forbid-select' : ''"><span
                      class="item-value">{{peo.initNum}}</span></el-col>
                  <el-col :span="8" class="align-center" :class="peo.initNum == peo.selectNum ? 'forbid-select' : ''"><span
                      class="item-value">{{peo.initNum - peo.selectNum}}</span>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
            <el-row class="card-item">
              <el-col :span="6">
                <span class="item-label">课程简介</span>
              </el-col>
              <el-col :span="18">
                <span class="item-value">{{item.intro}}</span>
              </el-col>
            </el-row>
            <el-row class="card-item">
              <el-col :span="6">
                <span class="item-label">学习目标</span>
              </el-col>
              <el-col :span="18">
                <span class="item-value">{{item.objective}}</span>
              </el-col>
            </el-row>
            <el-row class="card-item">
              <el-col :span="6">
                <span class="item-label">特别说明</span>
              </el-col>
              <el-col :span="18">
                <span class="item-value">{{item.specialNote}}</span>
              </el-col>
            </el-row>
          </div>
          <el-divider></el-divider>
          <el-row class="card-footer" justify="end">
            <el-button class="card-footer-button" type="primary" @click="selectCourse(item)" v-hasPermi="['elective:select:course:add']">我要选课</el-button>
          </el-row>
        </el-card>
      </div>
    </mt-loadmore>
  </div>
</template>

<script>
  import {
    listForSelect
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
    addSelect
  } from "@/api/elective/record/select"

  import defaultAvatar from "@/assets/image/profile.jpg"

  export default {
    name: "MobileSelectCourse",
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
        // 开放选课Id
        openId: undefined,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          status: undefined,
          teacherId: undefined,
          semesterId: undefined,
          classTimeId: undefined,
          gradeId: undefined,
          openId: undefined,
          onlyCan: false
        },
        // 表单参数
        form: {},
        // 选课选项
        select: {
          oepnId: undefined,
          courseId: undefined
        },
      };
    },
    mounted() {
      this.setHeight()
    },
    created() {
      this.openId = this.$route.params && this.$route.params.openId;
      this.queryParams.openId = this.openId
      this.select.openId = this.openId
      this.getList();
      // listSemester().then(response => {
      //   this.semesterOptions = response.data;
      // });
      // listClassTime().then(response => {
      //   this.classTimeOptions = response.data;
      // });
      // listTeacher().then(response => {
      //   this.teacherList = response.rows;
      // })
      listGrade().then(response => {
        this.gradeOptions = response.data
      })
      // this.getDicts("elective_course_status").then(response => {
      //   this.statusOptions = response.data;
      // });
    },
    methods: {
      /** 查询课程列表 */
      getList() {
        this.loading = true;
        listForSelect(this.queryParams).then(response => {
          this.dataList = this.dataList.concat(response.rows);
          this.total = response.total;
          this.loading = false;
          this.calNoMore()
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.dataList = []
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      selectCourse(row) {
        let text = row.specialNote || '无'
        let that = this
        let hint = "亲爱的同学，感谢你选择本课！请你再次确认你的身体条件等是否符合本课要求等信息。若你一旦选择，本学期内将无法作任何调整。"
        this.$confirm(hint, "温馨提示", {
          confirmButtonText: '确定选课',
          cancelButtonText: '不了，再看看',
          type: 'warning'
        }).then(function() {
          that.select.courseId = row.id
          addSelect(that.select).then(response => {
            if (response.code === 200) {
              that.msgSuccess("选课成功");
              that.getList();
            } else {
              that.msgError(response.msg);
            }
          })
        })
      },
      gradeFormat(gradeId) {
        for (let i in this.gradeOptions) {
          var g = this.gradeOptions[i]
          if (g.deptId == gradeId)
            return g.deptName
        }
      },
      getAvatarUrl(url) {
        if (url) return url
        else return defaultAvatar
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
      }
    }
  };
</script>
<style>
  .people-config-minus {
    color: red;
    font-size: 20px;
    cursor: pointer;
  }

  .people-config-plus {
    color: #5cb6ff;
    font-size: 20px;
    cursor: pointer;
  }

  .peo-el-row {
    margin-bottom: 20px;
  }

  .peo-el-row>.peo-el-col {
    margin-right: 14px;
  }

  .course-card {
    width: 100%;
    min-height: 520px;
    margin-right: 3%;
    margin-bottom: 30px;
    float: left;
    font-size: 14px;
    color: #606266;
  }

  .card-item {
    padding: 7px 0;
  }

  .card-body {
    min-height: 220px;
  }

  .item-label {
    color: #515a6e;
    font-weight: bold;
    margin-right: 10px;
  }

  .item-label-nomargin {
    color: #515a6e;
    font-weight: bold;
  }

  .item-value {}

  .align-center {
    text-align: center;
  }

  .card-bottom {
    position: absolute;
    bottom: 0;
  }

  .card-footer {
    width: 100%;
  }

  .card-footer-button {
    float: right;
  }

  .forbid-select {
    opacity: 0.5;
  }
</style>
