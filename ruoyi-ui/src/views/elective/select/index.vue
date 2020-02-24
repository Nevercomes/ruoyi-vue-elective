<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
    </el-form>

    <el-table v-loading="loading" :data="openList">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="学年学期" align="center" prop="semester" />
      <el-table-column label="开始时间" align="center" prop="openTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.openTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关闭时间" align="center" prop="closeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.closeTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <!-- <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleEnter(scope.row)" v-hasPermi="['elective:select:list']">进入选课</el-button>
        </template> -->
        <template slot-scope="scope">
          <router-link :to="'/select/course/list/' + scope.row.id" class="link-type">
            <el-button size="mini" type="text" icon="el-icon-date" v-hasPermi="['elective:select:list']">进入选课</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
  import {
    listOpen,
    getOpen,
    delOpen,
    addOpen,
    updateOpen,
    exportOpen,
    changeOpenStatus,
    listStudentOpen
  } from "@/api/elective/open/open";
  import {
    listSemester,
    listClassTime
  } from "@/api/elective/config/value"

  export default {
    name: "StudentOpen",
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
        // 开放选课表格数据
        openList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 开课时间字典
        semesterOptions: [],
        // 状态字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          semesterId: undefined,
          status: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [{
            required: true,
            message: "名称不能为空",
            trigger: "blur"
          }],
          semesterId: [{
            required: true,
            message: "学年学期不能为空",
            trigger: ["change", "blur"]
          }],
          openTime: [{
            required: true,
            message: "开始时间不能为空",
            trigger: "blur"
          }],
          closeTime: [{
            required: true,
            message: "关闭时间不能为空",
            trigger: "blur"
          }]
        }
      };
    },
    created() {
      this.getList();
    },
    beforeRouteEnter(to, from, next) {
      if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        next({
          path: '/mobile/select/student/open'
        })
      } else next()
    },
    methods: {
      /** 查询开放选课列表 */
      getList() {
        this.loading = true;
        listStudentOpen(this.queryParams).then(response => {
          this.openList = response.rows;
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
          remark: undefined,
          name: undefined,
          semesterId: undefined,
          openTime: undefined,
          closeTime: undefined,
          status: "0"
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
        this.title = "添加开放选课";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getOpen(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改开放选课";
        });
      },
      // 开放选课状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "关闭" : "开启";
        this.$confirm('确认要"' + text + '""' + row.name + '"吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return changeOpenStatus(row.id, row.status);
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function() {
          row.status = row.status === "0" ? "1" : "0";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateOpen(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addOpen(this.form).then(response => {
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
        this.$confirm('是否确认删除开放选课编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delOpen(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      }
    }
  };
</script>
