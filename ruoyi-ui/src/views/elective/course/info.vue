<template>
  <div class="app-container">
    <el-row :gutter="25">
      <el-col :span="8">
        <el-card class="custom-card el-card">
          <div slot="header" class="clearfix">
            <h3>课程信息</h3>
          </div>
          <el-row class="card-item">
            <el-col :span="12">
              <span class="item-label">课程</span>
              <span class="item-value">{{course.name}}</span>
            </el-col>
            <el-col :span="12"><span class="item-label">上课时间</span>
              <span class="item-value">{{course.classTime}}</span></el-col>
          </el-row>
          <el-row class="card-item">
            <el-col :span="12">
              <span class="item-label">教师</span>
              <span class="item-value">{{course.teacherName}}</span>
            </el-col>
            <el-col :span="12"><span class="item-label">上课地点</span>
              <span class="item-value">{{course.classLocation}}</span></el-col>
          </el-row>
          <el-divider></el-divider>
          <div class="card-body">
            <el-row class="card-item">
              <el-col :span="5"><span class="item-label">选课人数</span></el-col>
              <el-col :span="19">
                <el-row>
                  <el-col :span="8" class="align-center"><span class="item-label-nomargin">年级</span></el-col>
                  <el-col :span="8" class="align-center"><span class="item-label-nomargin">可选</span></el-col>
                  <el-col :span="8" class="align-center"><span class="item-label-nomargin">已选</span></el-col>
                </el-row>
                <el-row class="card-item" v-for="peo in course.peopleList" :key="peo.id">
                  <el-col :span="8" class="align-center"><span class="item-value">{{gradeFormat(peo.gradeId)}}</span></el-col>
                  <el-col :span="8" class="align-center"><span class="item-value">{{peo.initNum}}</span></el-col>
                  <el-col :span="8" class="align-center"><span class="item-value">{{peo.selectNum}}</span>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
            <el-row class="card-item">
              <el-col :span="5">
                <span class="item-label">课程简介</span>
              </el-col>
              <el-col :span="19">
                <span class="item-value">{{course.intro}}</span>
              </el-col>
            </el-row>
            <el-row class="card-item">
              <el-col :span="5">
                <span class="item-label">学习目标</span>
              </el-col>
              <el-col :span="19">
                <span class="item-value">{{course.objective}}</span>
              </el-col>
            </el-row>
            <el-row class="card-item">
              <el-col :span="5">
                <span class="item-label">特别说明</span>
              </el-col>
              <el-col :span="19">
                <span class="item-value">{{course.specialNote}}</span>
              </el-col>
            </el-row>
          </div>
          <el-divider></el-divider>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card class="custom-card ">
          <div slot="header" class="clearfix">
            <h3>教师信息</h3>
          </div>
          <el-row>
            <el-col :span="5">
              <el-image style="width: 120px; height: 120px; border-radius: 50%;" :src="getAvatarUrl(teacher.avatar)"
                :fit="'contain'"></el-image>
            </el-col>
            <el-col :span="18">
              <el-row class="card-item">
                <span class="item-label">教师</span>
                <span class="item-value">{{teacher.name}}</span>
              </el-row>
              <el-row class="card-item">
                <span class="item-label">学科</span>
                <span class="item-value">{{teacher.subject}}</span>
              </el-row>
              <el-row class="card-item">
                <span class="item-label">手机</span>
                <span class="item-value">{{teacher.phonenumber}}</span>
              </el-row>
              <el-row class="card-item">
                <span class="item-label">邮箱</span>
                <span class="item-value">{{teacher.email}}</span>
              </el-row>
            </el-col>
          </el-row>
          <el-divider></el-divider>
          <el-row class="card-item">
            <el-col :span="2"><span class="item-label">个人简介</span></el-col>
            <el-col :span="22"><span class="item-value">{{teacher.intro}}</span></el-col>
          </el-row>
          <el-row class="card-item">
            <el-col :span="2"><span class="item-label">教师专长</span></el-col>
            <el-col :span="22"><span class="item-value">{{teacher.specialty}}</span></el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
  import {
    getCourse
  } from "@/api/elective/course/course"
  import {
    getTeacher
  } from "@/api/elective/teacher/teacher"
  import {
    listGrade
  } from "@/api/elective/clazz/clazz"

  import defaultAvatar from "@/assets/image/profile.jpg"

  export default {
    name: "CourseInfo",
    data() {
      return {
        // 课程对象
        course: {},
        // 教师对象
        teacher: {},
        // 年级列表
        gradeOptions: [],
        // 默认简介
        dftIntro: '擅长唱、跳、Rap和打篮球等传统艺能，能够熟练在斗地主中以17牌被秒杀，精通各种鬼畜和吃桃子',
        // 默认专长
        dftSpecial: '唱、跳、Rap、打篮球'
      }
    },
    created() {
      const courseId = this.$route.params && this.$route.params.courseId
      let that = this
      getCourse(courseId).then(response => {
        this.course = response.data
        getTeacher(this.course.id).then(response => {
          that.teacher = response.data
          console.log(that.teacher)
        })
      })
      listGrade().then(response => {
        this.gradeOptions = response.data
      })
    },
    methods: {
      gradeFormat(gradeId) {
        for (let i in this.gradeOptions) {
          var g = this.gradeOptions[i]
          if (g.deptId == gradeId)
            return g.deptName
        }
      },
      getAvatarUrl(url) {
        if (url) return url
        else return defaultAvatar
      }
    }
  }
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
    margin-right: 14px;
  }

  .custom-card {
    font-size: 14px;
    color: #606266;
  }

  .card-item {
    padding: 7px 0;
  }

  .card-body {
    min-height: 220px;
  }

  .item-label {
    color: #515a6e;
    font-weight: bold;
    margin-right: 10px;
  }

  .item-label-nomargin {
    color: #515a6e;
    font-weight: bold;
  }

  .item-value {}

  .align-center {
    text-align: center;
  }

  .card-bottom {
    position: absolute;
    bottom: 0;
  }

  .card-footer {
    width: 100%;
  }

  .card-footer-button {
    float: right;
  }
</style>
