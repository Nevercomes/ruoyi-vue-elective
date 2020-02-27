<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24" class="mb-15">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user" :width="avatarWidth" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />手机号码
                <div class="pull-right">{{ user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item" v-hasPermi="['sys:role:student']">
                <svg-icon icon-class="tree" />所在班级
                <div class="pull-right" v-if="user.dept">{{ user.dept.deptName }}</div>
                <!-- <div class="pull-right" v-if="user.dept">{{ user.dept.deptName }} / {{ postGroup }}</div> -->
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />所属角色
                <div class="pull-right">{{ roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
                <div class="pull-right">2018-08-23 09:11:56</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane v-if="isTeacher" label="教师资料" name="teacherinfo" v-hasPermi="['sys:role:teacher', 'sys:role:staff']">
              <teacherInfo :teacher="teacher" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import userAvatar from "./userAvatar";
  import userInfo from "@/views/system/user/profile/userInfo";
  import resetPwd from "@/views/system/user/profile/resetPwd";
  import teacherInfo from "@/views/system/user/profile/teacherInfo"

  import {
    getUserProfile,
    getTeacherByUser
  } from "@/api/system/user";

  export default {
    name: "Profile",
    components: {
      userAvatar,
      userInfo,
      resetPwd,
      teacherInfo
    },
    data() {
      return {
        user: {},
        teacher: {},
        roleGroup: {},
        postGroup: {},
        activeTab: "userinfo",
        isTeacher: false,
        avatarWidth: '360px'
      };
    },
    created() {
      const userId = this.$route.query && this.$route.query.userId
      this.getUser(userId);
      const clientWidth = document.body.clientWidth
      if (clientWidth - 20 < 360) {
        this.avatarWidth = (clientWidth - 20) + 'px'
      }
    },
    methods: {
      getUser(userId) {
        let that = this
        getUserProfile(userId).then(response => {
          this.user = response.data;
          this.roleGroup = response.roleGroup;
          this.postGroup = response.postGroup;
          if (this.roleGroup.indexOf('教师') != -1) {
            this.isTeacher = true
            getTeacherByUser(this.user.userId).then(res => {
              that.teacher = res.data
            })
          }
        });
      }
    }
  };
</script>
<style>
  .mb-15 {
    margin-bottom: 15px;
  }
</style>
