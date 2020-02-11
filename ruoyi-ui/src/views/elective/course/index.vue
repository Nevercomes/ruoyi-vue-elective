<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="课程名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入课程名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
        <el-form-item label="课程状态 字典值 0 申请中 1 申请通过 2 退回" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择课程状态 字典值 0 申请中 1 申请通过 2 退回" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="上课老师" prop="teacherId">
        <el-input
          v-model="queryParams.teacherId"
          placeholder="请输入上课老师"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
        <el-form-item label="对应的开课时间 字典值" prop="semester">
        <el-select v-model="queryParams.semester" placeholder="请选择对应的开课时间 字典值" clearable size="small">
          <el-option
            v-for="dict in semesterOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
        <el-form-item label="上课时间 字典值" prop="classTime">
        <el-select v-model="queryParams.classTime" placeholder="请选择上课时间 字典值" clearable size="small">
          <el-option
            v-for="dict in classTimeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['elective:course:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['elective:course:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['elective:course:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['elective:course:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="课程名" align="center" prop="name" />
      <el-table-column label="课程状态 字典值 0 申请中 1 申请通过 2 退回" align="center" prop="status" />
      <el-table-column label="上课老师" align="center" prop="teacherId" />
      <el-table-column label="对应的开课时间 字典值" align="center" prop="semester" :formatter="semesterFormat" />
      <el-table-column label="课程简介" align="center" prop="intro" />
      <el-table-column label="目标" align="center" prop="objective" />
      <el-table-column label="特别声明" align="center" prop="specialNote" />
      <el-table-column label="上课时间 字典值" align="center" prop="classTime" :formatter="classTimeFormat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['elective:course:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['elective:course:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改course对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程名" prop="name">
          <el-input v-model="form.name" placeholder="请输入课程名" />
        </el-form-item>
        <el-form-item label="课程状态 字典值 0 申请中 1 申请通过 2 退回">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上课老师" prop="teacherId">
          <el-input v-model="form.teacherId" placeholder="请输入上课老师" />
        </el-form-item>
        <el-form-item label="对应的开课时间 字典值">
          <el-select v-model="form.semester" placeholder="请选择对应的开课时间 字典值">
            <el-option
              v-for="dict in semesterOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程简介" prop="intro">
          <el-input v-model="form.intro" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="目标" prop="objective">
          <el-input v-model="form.objective" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="特别声明" prop="specialNote">
          <el-input v-model="form.specialNote" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="上课时间 字典值">
          <el-select v-model="form.classTime" placeholder="请选择上课时间 字典值">
            <el-option
              v-for="dict in classTimeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
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
import { listCourse, getCourse, delCourse, addCourse, updateCourse, exportCourse } from "@/api/elective/course/course";

export default {
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
      // course表格数据
      courseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 对应的开课时间 字典值字典
      semesterOptions: [],
      // 上课时间 字典值字典
      classTimeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        status: undefined,
        teacherId: undefined,
        semester: undefined,
        intro: undefined,
        objective: undefined,
        specialNote: undefined,
        classTime: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "课程名不能为空", trigger: "blur" }
        ],        status: [
          { required: true, message: "课程状态 字典值 0 申请中 1 申请通过 2 退回不能为空", trigger: "blur" }
        ],        teacherId: [
          { required: true, message: "上课老师不能为空", trigger: "blur" }
        ],        semester: [
          { required: true, message: "对应的开课时间 字典值不能为空", trigger: "blur" }
        ],      }
    };
  },
  created() {
    this.getList();
    this.getDicts("elective_config_template_type").then(response => {
      this.semesterOptions = response.data;
    });
    this.getDicts("elective_config_template_type").then(response => {
      this.classTimeOptions = response.data;
    });
  },
  methods: {
    /** 查询course列表 */
    getList() {
      this.loading = true;
      listCourse(this.queryParams).then(response => {
        this.courseList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 对应的开课时间 字典值字典翻译
    semesterFormat(row, column) {
      return this.selectDictLabel(this.semesterOptions, row.semester);
    },
    // 上课时间 字典值字典翻译
    classTimeFormat(row, column) {
      return this.selectDictLabel(this.classTimeOptions, row.classTime);
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
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined,
        name: undefined,
        status: "0",
        teacherId: undefined,
        semester: undefined,
        intro: undefined,
        objective: undefined,
        specialNote: undefined,
        classTime: undefined
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
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加course";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCourse(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改course";
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
      this.$confirm('是否确认删除course编号为"' + ids + '"的数据项?', "警告", {
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
      this.$confirm('是否确认导出所有course数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportCourse(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>