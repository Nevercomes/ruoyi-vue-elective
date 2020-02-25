<template>
  <div class="app-container mobile-container" ref="MobileContainer">
    <!-- <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->

    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" :topPullText="''"
      :bottomPullText="''" :bottomDropText="'释放加载'" :auto-fill="false" ref="loadmore">
      <ul>
        <li v-for="item in dataList" class="nl-crad-li">
          <nl-card>
            <nl-card-item :label="'学生'" :value="item.studentName"></nl-card-item>
            <nl-card-item :label="'班级'" :value="item.className"></nl-card-item>
            <nl-card-item :label="'选课'" :value="item.openName"></nl-card-item>
            <nl-card-item>
              <el-row class="card-item_box">
                <el-col :span="6"><span class="card-item_laebl">课程</span></el-col>
                <el-col :span="18">
                  <router-link :to="'/course/info/' + item.courseId" class="link-type">
                    <span class="card-item_vlaue">{{ item.courseName }}</span>
                  </router-link>
                </el-col>
              </el-row>
            </nl-card-item>
            <nl-card-item :label="'创建时间'" :value="parseTime(item.createTime)"></nl-card-item>
          </nl-card>
        </li>
      </ul>
    </mt-loadmore>
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
  import NlCard from "@/components/NaLo/nl-card"
  import NlCardItem from "@/components/NaLo/nl-card-item"

  export default {
    name: "MobileSelectRecord",
    components: {
      NlCard,
      NlCardItem
    },
    data() {
      return {
        // 是否加载完数据
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
        // select表格数据
        dataList: [],
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
    mounted() {
      this.setHeight()
    },
    created() {
      this.queryParams.studentId = this.$route.query && this.$route.query.studentId;
      this.queryParams.courseId = this.$route.query && this.$route.query.courseId
      this.queryParams.gradeId = this.$route.query && this.$route.query.gradeId
      this.getList();
      // listClazz().then(response => {
      //   this.clazzOptions = response.data
      // })
      // listGrade().then(response => {
      //   this.gradeOptions = response.data
      // })
      // listStudent().then(response => {
      //   this.studentList = response.rows
      // })
      // listOpen().then(response => {
      //   this.openOptions = response.rows
      // })
    },
    methods: {
      /** 查询select列表 */
      getList() {
        this.loading = true;
        this.allLoaded = false
        listSelect(this.queryParams).then(response => {
          this.dataList = this.dataList.concat(response.rows);
          this.total = response.total;
          this.loading = false;
          this.calNoMore()
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
        this.dataList = []
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
        this.title = "添加select";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        listCanSelect(row.studentId).then(response => {
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
<style scoped>
  .nl-crad-li {
    padding: 5px 0;
  }

  .card-item_box {
    width: 100%;
    padding: 6px 0;
    display: flex;
  }

  .card-item_laebl {
    color: #515a6e;
    font-weight: bold;
    min-width: 60px;
    text-align: justify;
    margin-right: 20;
    vertical-align: middle;
  }

  .card-item_laebl:after {
    content: '';
    width: 100%;
    display: inline-block;
    overflow: hidden;
    height: 0;
  }

</style>
