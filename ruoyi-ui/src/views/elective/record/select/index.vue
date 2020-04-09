<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="选课" prop="openId">
        <el-select v-model="queryParams.openId" placeholder="请选择选课" clearable size="small">
          <el-option v-for="item in openOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="年级" prop="gradeId" v-hasPermi="['sys:role:staff', 'sys:role:teacher']">
        <el-select v-model="queryParams.gradeId" placeholder="请选择年级" clearable size="small">
          <el-option v-for="item in gradeOptions" :key="item.deptId" :label="item.deptName" :value="item.deptId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="班级" prop="clazzId" v-hasPermi="['sys:role:staff', 'sys:role:teacher']">
        <el-select v-model="queryParams.clazzId" placeholder="请选择班级" clearable size="small">
          <el-option v-for="item in clazzOptions" :key="item.deptId" :label="item.deptName" :value="item.deptId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="学生" prop="studentName" v-hasPermi="['sys:role:staff']">
        <el-input v-model="queryParams.studentName" placeholder="请输入学生姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="课程" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['elective:select:edit']">修改</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['elective:select:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['elective:select:export']">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="selectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="学生" align="center" prop="studentName" />
      <el-table-column label="班级" align="center" prop="className" />
      <el-table-column label="选课" align="center" prop="openName" :show-overflow-tooltip="true" />
      <el-table-column label="课程" align="center" prop="courseName" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <router-link :to="'/course/info/' + scope.row.courseId" class="link-type">
            <span>{{scope.row.courseName}}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="手机" align="center" prop="studentPhone" />
      <el-table-column label="性别" align="center" prop="studentSex" :formatter="sexFormat" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-hasPermi="['sys:role:staff']">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['elective:select:edit']">修改</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['elective:select:remove']">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改select对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学生" prop="studentId">
          <el-select v-model="form.studentId" placeholder="请选择学生" :disabled="true">
            <el-option v-for="item in studentList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程">
            <el-option v-for="item in canSelectList" :key="item.courseId" :label="item.courseName" :value="item.courseId"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listSelect,
    getSelect,
    delSelect,
    addSelect,
    updateSelect,
    exportSelect,
    listCanSelect
  } from "@/api/elective/record/select";
  import {
    listClazz,
    listGrade
  } from "@/api/elective/clazz/clazz"
  import {
    listStudent
  } from "@/api/elective/student/student"
  import {
    listOpen
  } from "@/api/elective/open/open"

  export default {
    name: "SelectRecord",
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
        // select表格数据
        selectList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 日期范围
        dateRange: [],
        // 学生List
        studentList: [],
        // 特定学生可选的课程
        canSelectList: [],
        // 班级列表
        clazzOptions: [],
        // 年级列表
        gradeOptions: [],
        // 性别字典
        sexOptions: [],
        // 选课列表
        openOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          studentName: undefined,
          courseName: undefined,
          clazzId: undefined,
          studentId: undefined,
          openId: undefined,
          courseId: undefined,
          gradeId: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          studentId: [{
            required: true,
            message: "学生不能为空",
            trigger: "blur"
          }],
          courseId: [{
            required: true,
            message: "课程不能为空",
            trigger: "blur"
          }]
        }
      };
    },
    beforeRouteEnter(to, from_, next) {
      if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        const studentId = from_.params && from_.params.studentId
        const courseId = from_.query && from_.query.courseId
        const gradeId = from_.query && from_.query.gradeId
        next({
          path: '/mobile/select/record',
          query: {
            studentId: studentId,
            courseId: courseId,
            gradeId: gradeId
          }
        })
      } else next()
    },
    created() {
      this.queryParams.studentId = this.$route.params && this.$route.params.studentId;
      this.queryParams.courseId = this.$route.query && this.$route.query.courseId
      this.queryParams.gradeId = this.$route.query && this.$route.query.gradeId
      this.getList();
      listClazz().then(response => {
        this.clazzOptions = response.data
      })
      listGrade().then(response => {
        this.gradeOptions = response.data
      })
      listStudent().then(response => {
        this.studentList = response.rows
      })
      listOpen().then(response => {
        this.openOptions = response.rows
      })
      this.getDicts("sys_user_sex").then(response => {
        this.sexOptions = response.data;
      });
    },
    methods: {
      /** 查询select列表 */
      getList() {
        this.loading = true;
        listSelect(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.selectList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
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
          studentId: undefined,
          courseId: undefined,
          classTimeId: undefined,
          classLocation: undefined,

        };
        this.resetForm("form");
      },
      // 重置课程详情表单
      resetCourseForm() {
        this.courseForm = {
          id: undefined,
          name: undefined,
          teacherName: undefined,
          semesterId: undefined
        }
        this.resetForm("courseForm")
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      sexFormat(row, column) {
        return this.selectDictLabel(this.sexOptions, row.studentSex);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加select";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        console.log(row)
        this.reset();
        const id = row.id || this.ids
        listCanSelect({
          studentId: row.studentId,
          openId: row.openId
        }).then(response => {
          this.canSelectList = response.rows
        });
        getSelect(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改选课";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateSelect(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addSelect(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除select编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSelect(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有select数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSelect(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
