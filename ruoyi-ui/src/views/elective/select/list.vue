<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="课程" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入课程名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="上课教师" prop="teacherId">
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
      <el-form-item>
        <el-checkbox v-model="queryParams.onlyCan" v-hasPermi="['sys:role:student']">仅显示可选课程</el-checkbox>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="clearfix">
      <el-card class="course-card el-card" v-for="(item, index) in courseList" :key="index">
        <el-row>
          <el-col :span="12">
            <el-image style="width: 120px; height: 120px; border-radius: 50%;" :src="getAvatarUrl(item.teacherAvatar)"
              :fit="'contain'"></el-image>
            <!-- <img :src="item.teacherAvatar" style="width: 120px; height: 120px; border-radius: 50%;" /> -->
          </el-col>
          <el-col :span="12">
            <el-row class="card-item">
              <span class="item-label">课程</span>
              <span class="item-value">{{item.name}}</span>
            </el-row>
            <el-row class="card-item">
              <span class="item-label">教师</span>
              <span class="item-value">{{item.teacherName}}</span>
            </el-row>
            <el-row class="card-item">
              <span class="item-label">上课时间</span>
              <span class="item-value">{{item.courseTime}}</span>
            </el-row>
            <el-row class="card-item">
              <span class="item-label">上课地点</span>
              <span class="item-value">{{item.classLocation}}</span>
            </el-row>
          </el-col>
        </el-row>
        <el-divider></el-divider>
        <div class="card-body">
          <el-row class="card-item">
            <el-col class="laebl-item_label"><span class="item-label-nomargin">选课信息</span></el-col>
            <el-col class="label-item_value">
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
            <!-- <el-col :span="5">
              <span class="item-label">课程简介</span>
            </el-col>
            <el-col :span="19">
              <span class="item-value">{{item.intro}}</span>
            </el-col> -->
            <nl-label-item :label="'课程简介'" :value="item.intro"></nl-label-item>
          </el-row>
          <el-row class="card-item">
            <!-- <el-col :span="5">
              <span class="item-label">学习目标</span>
            </el-col>
            <el-col :span="19">
              <span class="item-value">{{item.objective}}</span>
            </el-col> -->
            <nl-label-item :label="'学习目标'" :value="item.objective"></nl-label-item>
          </el-row>
          <el-row class="card-item">
            <!-- <el-col :span="5">
              <span class="item-label">特别说明</span>
            </el-col>
            <el-col :span="19">
              <span class="item-value">{{item.specialNote}}</span>
            </el-col> -->
            <nl-label-item :label="'特别说明'" :value="item.specialNote"></nl-label-item>
          </el-row>
        </div>
        <el-divider></el-divider>
        <el-row class="card-footer" justify="end">
          <el-button v-if="item.canSelect" class="card-footer-button" type="primary" @click="handleSelectCourse(item)"
            v-hasPermi="['elective:select:course:add']">我要选课</el-button>
          <el-button v-else :disabled="true" class="card-footer-button" type="primary" v-hasPermi="['elective:select:course:add']">年级不符</el-button>
        </el-row>
      </el-card>
    </div>

    <!-- <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="课程名" align="center" prop="name" />
      <el-table-column label="上课教师" align="center" prop="teacherName" />
      <el-table-column label="开课时间" align="center" prop="semester" :show-overflow-tooltip="true" />
      <el-table-column label="上课时间" align="center" prop="classTime" :show-overflow-tooltip="true" />
      <el-table-column label="上课地点" align="center" prop="classLocation" :show-overflow-tooltip="true" />
      <el-table-column label="招生人数" align="center" prop="enrollPeo" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 0">{{statusFormat(scope.row.status)}}</el-tag>
          <el-tag v-else-if="scope.row.status == 1" type="success">{{statusFormat(scope.row.status)}}</el-tag>
          <el-tag v-else-if="scope.row.status == 2" type="danger">{{statusFormat(scope.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['elective:course:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['elective:course:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table> -->

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="450px">
      <span>{{course.specialNote}}</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="!countZero" @click="selectCourse">{{countdown}}</el-button>
      </span>
    </el-dialog>

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
  import NlLabelItem from '@/components/NaLo/nl-label-item.vue'

  export default {
    name: "StudentSelect",
    components: {
      NlLabelItem
    },
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
        // 当前选中的课程
        course: {},
        // 选课选项
        select: {
          oepnId: undefined,
          courseId: undefined
        },
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
        },
        countdown: '确定',
        countZero: true
      };
    },
    beforeRouteEnter(to, from, next) {
      if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        next({
          path: '/mobile/select/student/course'
        })
      } else next()
    },
    created() {
      this.openId = this.$route.params && this.$route.params.openId;
      this.queryParams.openId = this.openId
      this.select.openId = this.openId
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
    methods: {
      /** 查询课程列表 */
      getList() {
        this.loading = true;
        listForSelect(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.courseList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 课程状态字典翻译
      statusFormat(status) {
        return this.selectDictLabel(this.statusOptions, status);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          remark: undefined,
          name: undefined,
          status: undefined,
          teacherId: undefined,
          semesterId: undefined,
          intro: undefined,
          objective: undefined,
          specialNote: undefined,
          classTimeId: undefined,
          classLocation: undefined,
          noteTime: undefined,
          peopleList: [{
            gradeId: null,
            initNum: ''
          }]
        };
        this.resetForm("form");
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
      handleSelectCourse(row) {
        // 弹出特别说明的限制关闭时间的弹窗(dialog模拟)
        this.course = row
        if (row.specialNote && row.noteTime && row.noteTime > 0) {
          this.open = true
          this.title = '特别说明'
          this.countZero = false
          let time = row.noteTime
          let timer = setInterval(() => {
            time = time - 1
            this.countdown = time + 's确定'
            if (time == 0) {
              this.countdown = '确定'
              this.countZero = true
              clearInterval(timer)
            }
          }, 1000)
        } else {
          this.selectCourse()
        }
      },
      selectCourse() {
        this.open = false
        let that = this
        let hint = "亲爱的同学，感谢你选择本课！请你再次确认你的身体条件等是否符合本课要求等信息。若你一旦选择，本学期内将无法作任何调整。"
        this.$confirm(hint, "温馨提示", {
          confirmButtonText: '是的，我要学习本课',
          cancelButtonText: '不了，我还没想好',
          type: 'warning'
        }).then(function() {
          that.select.courseId = that.course.id
          addSelect(that.select).then(response => {
            if (response.code === 200) {
              that.msgSuccess("恭喜你，已完成选课！");
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
        if (url) return process.env.VUE_APP_BASE_API + url
        else return defaultAvatar
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
    width: 30%;
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

  .laebl-item_label {
    color: #515a6e;
    font-weight: bold;
    width: 56px;
    margin-right: 15px;
    text-align: right;
  }

  .label-item_value {
    width: calc(100% - 71px);
    word-wrap: break-word;
    /* flex-grow: 1; */
  }

</style>
