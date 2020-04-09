<template>
  <div class="app-container mobile-container" ref="MobileContainer">
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="课程" prop="name">
            <el-input v-model="form.name" placeholder="请输入课程名" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="教师" prop="teacherId">
            <el-select v-model="form.teacherId" placeholder="请选择上课教师" :disabled="form.id != undefined">
              <el-option v-for="item in teacherList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="学年学期" prop="semesterId">
            <el-select v-model="form.semesterId" placeholder="请选择学年学期">
              <el-option v-for="item in semesterOptions" :key="item.id" :label="item.label" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="上课时间" prop="classTimeId">
            <el-select v-model="form.classTimeId" placeholder="请选择上课时间">
              <el-option v-for="item in classTimeOptions" :key="item.id" :label="item.label" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
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
                <el-form-item :prop="`peopleList.${index}.initNum`" :rules="{validator: checkPeople, trigger: 'blur'}">
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
        <el-col :span="24">
          <el-form-item label="声明显示" prop="noteTime" v-if="form.specialNote">
            <el-radio-group v-model="form.noteTime">
              <el-radio :label="0">不特别显示</el-radio>
              <el-radio :label="3">3秒</el-radio>
              <el-radio :label="5">5秒</el-radio>
              <el-radio :label="10">10秒</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="form-footer">
      <el-button v-if="isUpdate" type="danger" @click="handleDelete(item)">删 除</el-button>
      <el-button type="primary" @click="submitForm">保 存</el-button>
      <el-button v-if="form.status == 1" type="success" @click="handleReAdd(item)">续 开</el-button>
      <el-button v-if="form.status == 2" type="success" @click="handleReApply(item)">重 申</el-button>
    </div>
    <div class="clearfix"></div>
  </div>
</template>

<script>
  import {
    getCourse,
    delCourse,
    addCourse,
    updateCourse,
    reApply
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
    MessageBox
  } from "mint-ui"

  export default {
    name: "MobileCourseForm",
    data() {
      var checkPeople = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('招生人数不能为空'));
        }
        try {
          value = Number(value)
        } catch {
          callback(new Error('招生人数请输入数字值'));
        }
        if (!Number.isInteger(value)) {
          callback(new Error('招生人数请输入数字值'));
        } else {
          if (value <= 0) {
            callback(new Error('招生人数必须大于0'));
          } else {
            callback();
          }
        }
      };
      return {
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
        },
        isUpdate: false,
        checkPeople: checkPeople
      };
    },
    mounted() {
      this.setHeight()
    },
    created() {
      this.reset()
      this.form.id = this.$route.query && this.$route.query.courseId
      if (this.form.id) {
        this.isUpdate = true
        getCourse(this.form.id).then(response => {
          this.form = response.data
        })
      }
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
      // 课程状态字典翻译
      statusFormat(status) {
        return this.selectDictLabel(this.statusOptions, status);
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
          }],
          noteTime: 0
        };
        this.resetForm("form");
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加课程";
      },
      handleReAdd(row) {
        this.form.id = undefined
        this.form.status = undefined
        addCourse(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("续开成功");
          } else {
            this.msgError(response.msg);
          }
        });
      },
      handleReApply(row) {
        reApply(this.form).then(res => {
          if (res.code === 200) {
            this.msgSuccess("重新申请成功");
          } else {
            this.msgError(res.msg);
          }
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getCourse(id).then(response => {
          this.form = response.data;
          if (this.form.peopleList == undefined || this.form.peopleList.length == 0)
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
                  // this.open = false;
                  // this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addCourse(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  // this.open = false;
                  // this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete() {
        const ids = this.form.id
        if (!this.isMobile()) {
          this.$confirm('是否确认删除课程"' + this.form.name + '"的数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return delCourse();
          }).then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          }).catch(function() {});
        } else {
          MessageBox.confirm('是否确认删除课程"' + this.form.name + '"的数据项?', '警告').then(action => {
            return delCourse(ids);
          }).then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          }).catch(function() {});
        }
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
      },
      setHeight() {
        this.$refs.MobileContainer.style.height = (document.body.clientHeight - 86) + 'px'
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
    margin-right: 10px;
  }

  .form-footer {
    float: right;
  }
</style>
