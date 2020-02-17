<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="课程名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入课程名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="上课教师" prop="teacherId">
        <el-select v-model="queryParams.teacherId" placeholder="请选择上课教师" clearable size="small">
          <el-option v-for="item in teacherList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="开课时间" prop="semesterId">
        <el-select v-model="queryParams.semesterId" placeholder="请选择开课时间" clearable size="small">
          <el-option v-for="item in semesterOptions" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="上课时间" prop="classTimeId">
        <el-select v-model="queryParams.classTimeId" placeholder="请选择上课时间" clearable size="small">
          <el-option v-for="item in classTimeOptions" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['elective:course:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['elective:course:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['elective:course:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['elective:course:export']">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="课程名" align="center" prop="name" />
      <el-table-column label="上课教师" align="center" prop="teacherName" />
      <el-table-column label="开课时间" align="center" prop="semester" :show-overflow-tooltip="true" />
      <el-table-column label="上课时间" align="center" prop="classTime" :show-overflow-tooltip="true" />
      <el-table-column label="上课地点" align="center" prop="classLocation" :show-overflow-tooltip="true" />
      <el-table-column label="招生人数" align="center" prop="enrollPeo" :show-overflow-tooltip="true" />
      <!-- TODO 用标签来表示状态 -->
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 0">{{statusFormat(scope.row.status)}}</el-tag>
          <el-tag v-else-if="scope.row.status == 1" type="success">{{statusFormat(scope.row.status)}}</el-tag>
          <el-tag v-else-if="scope.row.status == 2" type="danger">{{statusFormat(scope.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- TODO 课程审核  -->
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['elective:check:add']">审核</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['elective:course:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['elective:course:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改课程对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程名" prop="name">
              <el-input v-model="form.name" placeholder="请输入课程名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上课教师" prop="teacherId">
              <el-select v-model="form.teacherId" placeholder="请选择上课教师" :disabled="form.id != undefined">
                <el-option v-for="item in teacherList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开课时间" prop="semesterId">
              <el-select v-model="form.semesterId" placeholder="请选择开课时间">
                <el-option v-for="item in semesterOptions" :key="item.id" :label="item.label" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上课时间" prop="classTimeId">
              <el-select v-model="form.classTimeId" placeholder="请选择上课时间">
                <el-option v-for="item in classTimeOptions" :key="item.id" :label="item.label" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上课地点" prop="classLocation">
              <el-input v-model="form.classLocation" placeholder="请输入上课地点" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="招生人数">
              <el-row class="peo-el-row" v-for="(item, index) in form.peopleList" :key="index">
                <el-col class="peo-el-col" :span="10">
                  <el-form-item :prop="`peopleList.${index}.gradeId`" :rules="{required: true, message: '请选择招生年级', trigger: 'change'}">
                    <el-select v-model="item.gradeId">
                      <el-option v-for="grade in gradeOptions" :key="grade.deptId + index" :value="grade.deptId" :label="grade.deptName"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col class="peo-el-col" :span="10">
                  <el-form-item :prop="`peopleList.${index}.initNum`" :rules="{required: true, message: '请输入招生人数', trigger: 'blur'}">
                    <el-input v-model="item.initNum" placeholder="请输入招生人数" />
                  </el-form-item>
                </el-col>
                <el-col :span="2">
                  <i v-if="index != 0" class="el-icon-minus people-config-minus" @click.prevent="removePeople(item)"></i>
                  <i v-else class="el-icon-plus people-config-plus" @click.prevent="addPeople"></i>
                </el-col>
              </el-row>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程简介" prop="intro">
              <el-input v-model="form.intro" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="目标" prop="objective">
              <el-input v-model="form.objective" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="特别声明" prop="specialNote">
              <el-input v-model="form.specialNote" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
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

  export default {
    name: "Course",
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
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          status: undefined,
          teacherId: undefined,
          semesterId: undefined,
          classTimeId: undefined
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
            message: "开课时间不能为空",
            trigger: "blur"
          }]
        }
      };
    },
    created() {
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
        listCourse(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
        this.title = "添加课程";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getCourse(id).then(response => {
          this.form = response.data;
          if(this.form.peopleList == undefined || this.form.peopleList.length == 0)
            this.form.peopleList.push({})
          this.open = true;
          this.title = "修改课程";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateCourse(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addCourse(this.form).then(response => {
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
        this.$confirm('是否确认删除课程编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCourse(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有课程数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportCourse(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
      removePeople(people) {
        let index = this.form.peopleList.indexOf(people)
        if (index != -1) {
          this.form.peopleList.splice(index, 1)
        }
      },
      addPeople() {
        this.form.peopleList.push({
          gradeId: null,
          initNum: ''
        })
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
  .peo-el-row > .peo-el-col {
    margin-right: 10px;
  }

</style>
