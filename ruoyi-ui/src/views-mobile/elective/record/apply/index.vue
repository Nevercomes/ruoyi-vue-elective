<template>
  <div class="app-container mobile-container" ref="MobileContainer">
    <!-- <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
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
    </el-form> -->

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['elective:apply:remove', 'sys:role:teacher']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['elective:apply:export', 'sys:role:teacher']">导出</el-button>
      </el-col>
    </el-row> -->

    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" :topPullText="''"
      :bottomPullText="''" :bottomDropText="'释放加载'" :auto-fill="false" ref="loadmore">
      <ul>
        <li v-for="item in dataList" class="nl-crad-li">
          <router-link :to="{path: '/mobile/course/form', query: {courseId: item.id}}">
            <nl-card :header="item.name">
              <nl-card-item :label="'教师'" :value="item.teacherName"></nl-card-item>
              <nl-card-item :label="'课程'" :value="item.courseName"></nl-card-item>
              <nl-card-item :label="'创建时间'" :value="parseTime(item.createTime)"></nl-card-item>
              <nl-card-item :label="'状态'">
                <el-tag v-if="item.status == 0">{{statusFormat(item.status)}}</el-tag>
                <el-tag v-else-if="item.status == 1" type="success">{{statusFormat(item.status)}}</el-tag>
                <el-tag v-else-if="item.status == 2" type="danger">{{statusFormat(item.status)}}</el-tag>
              </nl-card-item>
            </nl-card>
          </router-link>
        </li>
      </ul>
    </mt-loadmore>
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
    getCourse
  } from "@/api/elective/course/course"
  import {
    addCheck
  } from "@/api/elective/record/check"
  import NlCard from "@/components/NaLo/nl-card"
  import NlCardItem from "@/components/NaLo/nl-card-item"

  export default {
    name: "MobileApply",
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
        // 申请记录表格数据
        dataList: [],
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
        // 年级列表
        gradeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          teacherId: undefined,
          courseName: undefined,
          status: undefined
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
        }
      };
    },
    mounted() {
      this.setHeight()
    },
    created() {
      this.getList();
      this.getDicts("elective_apply_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 查询申请记录列表 */
      getList() {
        this.loading = true;
        this.allLoaded = false
        listApply(this.queryParams).then(response => {
          this.dataList = this.dataList.concat(response.rows);
          this.total = response.total;
          this.loading = false;
          this.calNoMore()
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
            }]
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
        const courseId = row.courseId
        this.form.applyId = row.id
        this.form.courseId = courseId
        getCourse(courseId).then(response => {
          this.form.course = response.data;
          this.open = true;
          this.title = "审核课程";
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
            if (result == '2') '退回';
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
  .nl-crad-li {
    padding: 5px 0;
  }
</style>
