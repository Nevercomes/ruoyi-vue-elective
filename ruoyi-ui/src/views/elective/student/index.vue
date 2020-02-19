<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="deptName" placeholder="请输入班级名称" clearable size="small" prefix-icon="el-icon-search" style="margin-bottom: 20px" />
        </div>
        <div class="head-container">
          <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false" :filter-node-method="filterNode"
            ref="tree" default-expand-all @node-click="handleNodeClick" />
        </div>
      </el-col>
      <!--学生数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="学生姓名" prop="name">
            <el-input v-model="queryParams.name" placeholder="请输入学生姓名" clearable size="small" style="width: 240px"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="学生状态" clearable size="small" style="width: 240px">
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
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:user:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
              v-hasPermi="['system:user:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
              v-hasPermi="['system:user:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport" v-hasPermi="['system:user:import']">导入</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:user:export']">导出</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="40" align="center" />
          <el-table-column label="编号" align="center" prop="userId" />
          <el-table-column label="学生姓名" align="center" prop="name">
            <!-- <template slot-scope="scope">
              <router-link :to="'/user/profile/' + scope.row.userId" class="link-type">
                {{scope.row.name}}
              </router-link>
            </template> -->
          </el-table-column>
          <el-table-column label="登录名称" align="center" prop="userName" :show-overflow-tooltip="true" />
          <el-table-column label="性别" align="center" prop="sex" :formatter="sexFormat"></el-table-column>
          <el-table-column label="班级" align="center" prop="className" :show-overflow-tooltip="true" />
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="查看" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <router-link :to="'/select/record/list/' + scope.row.id" class="link-type">
                <el-button size="mini" type="text" icon="el-icon-document" v-hasPermi="['elective:select:list']">课程</el-button>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-mobile" @click="handleSelect(scope.row)" v-hasPermi="['elective:select:add']">代选</el-button>
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:user:edit']">修改</el-button>
              <el-button v-if="scope.row.userId !== 1" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['system:user:remove']">删除</el-button>
              <el-button size="mini" type="text" icon="el-icon-key" @click="handleResetPwd(scope.row)" v-hasPermi="['system:user:resetPwd']">重置</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="学生姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入学生姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptOptions" placeholder="请选择班级" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录名称" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入登录名称" :disabled="form.id != undefined" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="登录密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入登录密码" type="password" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.sex" placeholder="请选择性别">
                <el-option v-for="dict in sexOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 学生导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess"
        :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的学生数据
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="select.title" :visible.sync="select.open" width="500px">
      <el-form ref="selectForm" :model="selectForm" :rules="selectRules" label-width="80px">
        <el-form-item label="姓名" prop="studentName">
          <el-input v-model="selectForm.studentName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="选课" prop="openId">
          <el-select v-model="selectForm.openId" placeholder="请选择选课">
            <el-option v-for="item in openList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程" prop="courseId" v-if="selectForm.openId">
          <el-select v-model="selectForm.courseId" placeholder="请选择课程">
            <el-option v-for="item in canSelelctList" :key="item.courseId" :label="item.courseName" :value="item.courseId"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSelectForm">确 定</el-button>
        <el-button @click="cancelSelect">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listStudent,
    getStudent,
    delStudent,
    addStudent,
    updateStudent,
    exportStudent,
    resetUserPwd,
    changeUserStatus,
    importTemplate
  } from "@/api/elective/student/student";
  import {
    getToken
  } from "@/utils/auth";
  import {
    treeselect
  } from "@/api/system/dept";
  import {
    listCanSelect,
    addSelect
  } from "@/api/elective/record/select"
  import {
    listStudentOpen
  } from "@/api/elective/open/open"
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "Student",
    components: {
      Treeselect
    },
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
        // 学生表格数据
        studentList: null,
        // 弹出层标题
        title: "",
        // 部门树选项
        deptOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 部门名称
        deptName: undefined,
        // 默认密码
        initPassword: undefined,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        statusOptions: [],
        // 性别状态字典
        sexOptions: [],
        // 开放选课列表
        openList: [],
        // 该学生的可选课程
        canSelelctList: [],
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 用户导入参数
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          updateSupport: 0,
          // 设置上传的请求头部
          headers: {
            Authorization: "Bearer " + getToken()
          },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/elective/student/importData"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          status: undefined,
          deptId: undefined
        },
        // 表单校验
        rules: {
          userName: [{
            required: true,
            message: "登录名称不能为空",
            trigger: "blur"
          }],
          name: [{
            required: true,
            message: "学生姓名不能为空",
            trigger: "blur"
          }],
          deptId: [{
            required: true,
            message: "班级不能为空",
            trigger: "blur"
          }],
          password: [{
            required: true,
            message: "登录密码不能为空",
            trigger: "blur"
          }]
        },
        // 代选表单
        selectForm: {},
        // 代选表单控制
        select: {
          title: '学生选课',
          open: false
        },
        selectRules: {
          studentName: {
            required: true,
            message: "学生不能为空"
          },
          openId: {
            required: true,
            message: "选课不能为空"
          },
          courseId: {
            required: true,
            message: "课程不能为空"
          }
        }
      };
    },
    watch: {
      // 根据名称筛选部门树
      deptName(val) {
        this.$refs.tree.filter(val);
      },
      'selectForm.openId': function(val, oldVal) {
        if (val) {
          listCanSelect(this.selectForm).then(response => {
            this.canSelelctList = response.rows
          })
        }
      }
    },
    created() {
      this.getList();
      this.getTreeselect();
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
      this.getDicts("sys_user_sex").then(response => {
        this.sexOptions = response.data;
      });
      this.getConfigKey("sys.user.initPassword").then(response => {
        this.initPassword = response.data;
      });
      listStudentOpen().then(response => {
        this.openList = response.rows
      })
    },
    methods: {
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listStudent(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.studentList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        treeselect().then(response => {
          this.deptOptions = response.data;
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.deptId = data.id;
        this.getList();
      },
      // 用户状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$confirm('确认要"' + text + '""' + row.userName + '"用户吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return changeUserStatus(row.userId, row.status);
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function() {
          row.status = row.status === "0" ? "1" : "0";
        });
      },
      // 性别字典翻译
      sexFormat(row, column) {
        return this.selectDictLabel(this.sexOptions, row.sex);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          userId: undefined,
          deptId: undefined,
          userName: undefined,
          name: undefined,
          password: undefined,
          sex: undefined,
          status: "0",
          remark: undefined
        };
        this.resetForm("form");
      },
      // 代选表单重置
      resetSelectForm() {
        this.selectForm = {
          openId: undefined,
          studentId: undefined,
          courseId: undefined,
          studentName: undefined
        };
        this.openId = undefined
        this.canSelelctList = []
        this.resetForm("selectForm")
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.page = 1;
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
        this.ids = selection.map(item => item.userId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.getTreeselect();
        this.open = true;
        this.title = "添加学生";
        this.form.password = this.initPassword;
        this.form.deptId = this.queryParams.deptId;
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.getTreeselect();
        const studentId = row.id || this.ids;
        getStudent(studentId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改学生";
          this.form.password = "";
        });
      },
      /** 重置密码按钮操作 */
      handleResetPwd(row) {
        this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(({
          value
        }) => {
          resetUserPwd(row.userId, value).then(response => {
            if (response.code === 200) {
              this.msgSuccess("修改成功，新密码是：" + value);
            } else {
              this.msgError(response.msg);
            }
          });
        }).catch(() => {});
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.userId != undefined) {
              updateStudent(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addStudent(this.form).then(response => {
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
        const userIds = row.userId || this.ids;
        this.$confirm('是否确认删除学生编号为"' + userIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delStudent(userIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有学生数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportStudent(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "学生导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        importTemplate().then(response => {
          this.download(response.msg);
        });
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert(response.msg, "导入结果", {
          dangerouslyUseHTMLString: true
        });
        this.getList();
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
      handleSelect(row) {
        this.resetSelectForm()
        this.selectForm.studentId = row.id
        this.selectForm.studentName = row.name
        this.select.open = true
      },
      submitSelectForm: function() {
        this.$refs["selectForm"].validate(valid => {
          if (valid) {
            addSelect(this.selectForm).then(response => {
              if (response.code === 200) {
                this.msgSuccess("代选成功");
                this.select.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        });
      },
      cancelSelect() {
        this.select.open = false
        this.resetSelectForm()
      }
    }
  };
</script>
