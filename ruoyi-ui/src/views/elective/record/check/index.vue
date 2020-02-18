<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="审核结果" prop="result">
        <el-select v-model="queryParams.result" placeholder="请选择审核结果" clearable size="small">
          <el-option v-for="dict in resultOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['elective:check:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['elective:check:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['elective:check:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['elective:check:export']">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="checkList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="课程" align="center" prop="courseName" />
      <el-table-column label="申请教师" align="center" prop="teacherName" />
      <el-table-column label="审核人" align="center" prop="createBy" />
      <el-table-column label="审核结果" align="center" prop="result" :formatter="resultFormat">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.result == 1" type="success">{{resultFormat(scope.row.result)}}</el-tag>
          <el-tag v-else-if="scope.row.result == 2" type="danger">{{resultFormat(scope.row.result)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['elective:check:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['elective:check:remove']">删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改申请审核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程" prop="courseId">
          <el-input v-model="form.courseId" placeholder="请输入课程" />
        </el-form-item>
        <el-form-item label="申请记录" prop="applyId">
          <el-input v-model="form.applyId" placeholder="请输入申请记录" />
        </el-form-item>
        <el-form-item label="审核结果">
          <el-select v-model="form.result" placeholder="请选择审核结果">
            <el-option v-for="dict in resultOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"></el-option>
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
    listCheck,
    getCheck,
    delCheck,
    addCheck,
    updateCheck,
    exportCheck
  } from "@/api/elective/record/check";

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
        // 申请审核表格数据
        checkList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 审核结果字典
        resultOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          courseName: undefined,
          result: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          courseId: [{
            required: true,
            message: "课程不能为空",
            trigger: "blur"
          }],
          applyId: [{
            required: true,
            message: "申请记录不能为空",
            trigger: "blur"
          }],
          result: [{
            required: true,
            message: "审核结果不能为空",
            trigger: "blur"
          }]
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("elective_check_result").then(response => {
        this.resultOptions = response.data;
      });
    },
    methods: {
      /** 查询申请审核列表 */
      getList() {
        this.loading = true;
        listCheck(this.queryParams).then(response => {
          this.checkList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 审核结果字典翻译
      resultFormat(result) {
        return this.selectDictLabel(this.resultOptions, result);
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
          courseId: undefined,
          applyId: undefined,
          result: undefined
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
        this.title = "添加申请审核";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getCheck(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改申请审核";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateCheck(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addCheck(this.form).then(response => {
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
        this.$confirm('是否确认删除申请审核编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCheck(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有申请审核数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportCheck(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
