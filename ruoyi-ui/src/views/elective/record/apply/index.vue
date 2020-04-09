<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="学年学期" prop="semesterId">
        <el-select v-model="queryParams.semesterId" placeholder="请选择学年学期" clearable size="small">
          <el-option v-for="item in semesterOptions" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="课程" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入申请课程" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="教师" prop="teacherId" v-hasPermi="['sys:role:staff']">
        <el-select v-model="queryParams.teacherId" placeholder="请选择教师" clearable size="small">
          <el-option v-for="item in teacherList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择申请状态" clearable size="small">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['elective:apply:remove', 'sys:role:teacher']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['elective:apply:export', 'sys:role:teacher']">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="applyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="教师" align="center" prop="teacherName" />
      <el-table-column label="课程" align="center" prop="courseName" />
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 0">{{statusFormat(scope.row.status)}}</el-tag>
          <el-tag v-else-if="scope.row.status == 1" type="success">{{statusFormat(scope.row.status)}}</el-tag>
          <el-tag v-else-if="scope.row.status == 2" type="danger">{{statusFormat(scope.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit-outline" @click="handleCheck(scope.row)" v-hasPermi="['elective:check:add']">审核</el-button>
          <!-- <el-button v-if="scope.row.status == 2" size="mini" type="text" icon="el-icon-circle-plus-outline" @click="handleReApply(scope.row)"
            v-hasPermi="['elective:apply:add']">重申</el-button> -->
          <!-- <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['elective:apply:remove']">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改申请记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程" prop="course.name">
              <el-input v-model="form.course.name" placeholder="请输入课程名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教师" prop="course.teacherId">
              <el-select v-model="form.course.teacherId" placeholder="请选择上课教师" :disabled="true">
                <el-option v-for="item in teacherList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学年学期" prop="course.semesterId">
              <el-select v-model="form.course.semesterId" placeholder="请选择学年学期">
                <el-option v-for="item in semesterOptions" :key="item.id" :label="item.label" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上课地点" prop="classLocation">
              <el-input v-model="form.classLocation" placeholder="请输入上课地点" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="上课时间">
              <el-row class="peo-el-row" v-for="(item, index) in form.course.timeList" :key="index">
                <el-col class="peo-el-col" :span="10">
                  <el-form-item :prop="`course.timeList.${index}.weekId`" :rules="{required: true, message: '请选择上课星期', trigger: 'change'}">
                    <el-select v-model="item.weekId">
                      <el-option v-for="week in classWeekOptions" :key="week.id + index" :value="week.id" :label="week.label"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col class="peo-el-col" :span="10">
                  <el-form-item :prop="`course.timeList.${index}.timeId`" :rules="{required: true, message: '请选择上课时间', trigger: 'change'}">
                    <el-select v-model="item.timeId">
                      <el-option v-for="time in classTimeOptions" :key="time.id + index" :value="time.id" :label="time.label"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="2">
                  <i v-if="index != 0" class="el-icon-minus people-config-minus" @click.prevent="removeTime(item)"></i>
                  <i v-else class="el-icon-plus people-config-plus" @click.prevent="addTime"></i>
                </el-col>
              </el-row>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="招生人数">
              <el-row class="peo-el-row" v-for="(item, index) in form.course.peopleList" :key="index">
                <el-col class="peo-el-col" :span="10">
                  <el-form-item :prop="`course.peopleList.${index}.gradeId`" :rules="{required: true, message: '请选择招生年级', trigger: 'change'}">
                    <el-select v-model="item.gradeId">
                      <el-option v-for="grade in gradeOptions" :key="grade.deptId + index" :value="grade.deptId" :label="grade.deptName"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col class="peo-el-col" :span="10">
                  <el-form-item :prop="`course.peopleList.${index}.initNum`" :rules="{required: true, message: '请输入招生人数', trigger: 'blur'}">
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
              <el-input v-model="form.course.intro" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="目标" prop="objective">
              <el-input v-model="form.course.objective" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="特别声明" prop="specialNote">
              <el-input v-model="form.course.specialNote" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="dialogType == 'REAPPLY'" type="primary" @click="submitReApplyForm">确 定</el-button>
        <el-button v-if="dialogType == 'CHECK'" type="success" @click="submitCheckForm('1')">通 过</el-button>
        <el-button v-if="dialogType == 'CHECK'" type="danger" @click="submitCheckForm('2')">退 回</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listApply,
    getApply,
    delApply,
    addApply,
    updateApply,
    exportApply
  } from "@/api/elective/record/apply";
  import {
    listSemester,
    listClassTime,
    listInUse
  } from "@/api/elective/config/value"
  import {
    listTeacher
  } from "@/api/elective/teacher/teacher"
  import {
    listClazz,
    listGrade
  } from "@/api/elective/clazz/clazz"
  import {
    getCourse
  } from "@/api/elective/course/course"
  import {
    addCheck
  } from "@/api/elective/record/check"
  import {
    inTime
  } from '@/utils/semester.js'

  export default {
    name: "Apply",
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
        // 申请记录表格数据
        applyList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 申请状态字典
        statusOptions: [],
        // 教师列表
        teacherList: [],
        // 对应的开课时间 字典值字典
        semesterOptions: [],
        // 上课时间 字典值字典
        classTimeOptions: [],
        // 上课星期 字典值字典
        classWeekOptions: [],
        // 年级列表
        gradeOptions: [],
        // 异步加载的查询参数，学期
        querySemesterId: undefined,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          teacherId: undefined,
          courseName: undefined,
          status: undefined,
          semesterId: this.querySemesterId,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          "course.name": [{
            required: true,
            message: "课程名不能为空",
            trigger: "blur"
          }],
          "course.semesterId": [{
            required: true,
            message: "学年学期不能为空",
            trigger: "blur"
          }],
          "course.teacherId": [{
            required: true,
            message: "上课教师不能为空",
            trigger: "blur"
          }]
        },
        dialogType: 'CHECK'
      };
    },
    beforeRouteEnter(to, from, next) {
      if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        next({
          path: '/mobile/apply/record'
        })
      } else next()
    },
    created() {
      // 当存在两级对象的时候 需要先在data里面声明出来
      const teacherId = this.$route.params && this.$route.params.teacherId
      if (teacherId) this.queryParams.teacherId = Number(teacherId)
      this.reset()
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
      listTeacher().then(response => {
        this.teacherList = response.rows;
      })
      listSemester().then(response => {
        this.semesterOptions = response.data;
      });
      listClassTime().then(response => {
        this.classTimeOptions = response.data;
      });
      listInUse("2").then(response => {
        this.classWeekOptions = response.data;
      });
      listGrade().then(response => {
        this.gradeOptions = response.data
      })
      this.getDicts("elective_apply_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 查询申请记录列表 */
      getList() {
        this.loading = true;
        listApply(this.queryParams).then(response => {
          this.applyList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 申请状态字典翻译
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
          applyId: undefined,
          courseId: undefined,
          result: undefined,
          course: {
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
            }],
            timeList: [{
              weekId: null,
              timeId: null
            }],
          }
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
        this.title = "添加申请记录";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getApply(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改申请记录";
        });
      },
      /** 审核按钮操作 */
      handleCheck(row) {
        this.reset()
        this.dialogType = 'CHECK'
        const courseId = row.courseId
        this.form.applyId = row.id
        this.form.courseId = courseId
        getCourse(courseId).then(response => {
          this.form.course = response.data;
          if (this.form.course.peopleList == undefined || this.form.course.peopleList.length == 0)
            this.form.course.peopleList.push({})
          if (this.form.course.timeList == undefined || this.form.course.timeList.length == 0)
            this.form.course.timeList.push({})
          this.open = true;
          this.title = "审核课程";
        });
      },
      handleReApply(row) {
        this.reset()
        this.dialogType = 'REAPPLY'
        const courseId = row.courseId
        this.form.applyId = row.id
        this.form.courseId = courseId
        getCourse(courseId).then(response => {
          this.form.course = response.data;
          this.open = true;
          this.title = "重新申请课程";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateApply(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addApply(this.form).then(response => {
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
      submitCheckForm(result) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.result = result;
            let text = '通过';
            if (result == '2') text = '退回';
            addCheck(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess(text + "成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        });
      },
      submitReApplyForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            addApply(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("重新申请成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除申请记录编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delApply(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有申请记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportApply(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
      removePeople(people) {
        let index = this.form.course.peopleList.indexOf(people)
        if (index != -1) {
          this.form.course.peopleList.splice(index, 1)
        }
      },
      addPeople() {
        this.form.course.peopleList.push({
          gradeId: null,
          initNum: ''
        })
      },
      removeTime(time) {
        let index = this.form.course.timeList.indexOf(time)
        if (index != -1) {
          this.form.course.timeList.splice(index, 1)
        }
      },
      addTime() {
        this.form.course.timeList.push({
          weekId: null,
          timeId: null
        })
      },
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
    margin-right: 10px;
  }
</style>
