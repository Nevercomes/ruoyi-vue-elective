<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="模板名称" prop="templateId">
        <el-select v-model="queryParams.templateId" size="small">
          <el-option v-for="item in templateOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="内容" prop="label">
        <el-input v-model="queryParams.label" placeholder="请输入内容" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['config:value:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['config:value:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['config:value:remove']">删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="valueList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="内容" align="center" prop="label" />
      <!-- <el-table-column label="排序" align="center" prop="sort" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['config:value:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['config:value:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改配置模板下内容的label与value对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模板编号" prop="templateId">
          <el-input v-model="form.templateId" :disabled="true" />
        </el-form-item>
        <el-form-item label="模板名" prop="templateName">
          <el-input :value="templateFormat(templateOptions, form.templateId)" :disabled="true" />
        </el-form-item>
        <el-form-item label="内容" prop="label">
          <el-input v-model="form.label" placeholder="请输入内容" />
        </el-form-item>
        <!-- <el-form-item label="显示排序" prop="sort">
          <el-input-number v-model="form.sort" controls-position="right" :min="0" />
        </el-form-item> -->
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
    listValue,
    getValue,
    delValue,
    addValue,
    updateValue,
    exportValue
  } from "@/api/elective/config/value";

  import {
    listTemplate
  } from "@/api/elective/config/template.js"

  export default {
    name: "Value",
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
        // 配置模板内容表格数据
        valueList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 模板列表
        templateOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          label: undefined,
          templateId: undefined,
          sort: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          templateId: [{
            required: true,
            message: "模板不能为空",
            trigger: ["blur", "change"]
          }],
          label: [{
            required: true,
            message: "内容不能为空",
            trigger: "blur"
          }],
          // sort: [{
          //   required: true,
          //   message: "显示排序不能为空",
          //   trigger: "change"
          // }]
        }
      };
    },
    created() {
      const templateId = this.$route.params && this.$route.params.templateId;
      this.queryParams.templateId = templateId ? Number(templateId) : undefined
      this.getTemplateList()
      this.getList();
    },
    methods: {
      /** 查询配置模板下内容的label与value列表 */
      getList() {
        this.loading = true;
        listValue(this.queryParams).then(response => {
          this.valueList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 配置模板列表
      getTemplateList() {
        listTemplate().then(response => {
          this.templateOptions = response.rows;
        })
      },
      templateFormat(list, id) {
        var actions = [];
        Object.keys(list).map((key) => {
          if (list[key].id == ('' + id)) {
            actions.push(list[key].name);
            return false;
          }
        })
        return actions.join('');
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
          label: undefined,
          templateId: undefined,
          sort: 0
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
        this.title = "添加配置模板内容";
        this.form.templateId = this.queryParams.templateId
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getValue(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改配置模板内容";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateValue(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addValue(this.form).then(response => {
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
        this.$confirm('是否确认删除配置模板内容的编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delValue(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有配置模板内容的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportValue(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
