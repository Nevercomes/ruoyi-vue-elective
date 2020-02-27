<template>
  <el-form ref="form" :model="teacher" :rules="rules" label-width="80px">
    <el-form-item label="学科" prop="subject">
      <el-input v-model="teacher.subject" />
    </el-form-item>
    <el-form-item label="简介" prop="intro">
      <el-input v-model="teacher.intro" type="textarea" />
    </el-form-item>
    <el-form-item label="专长" prop="specialty">
      <el-input v-model="teacher.specialty" type="textarea" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import {
    updateTeacherProfile
  } from "@/api/elective/teacher/teacher";

  export default {
    props: {
      teacher: {
        type: Object
      }
    },
    data() {
      return {
        rules: {}
      };
    },
    methods: {
      submit() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            updateTeacherProfile(this.teacher).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功"); 
              } else {
                this.msgError(response.msg);
              }
            });
          }
        });
      },
      close() {
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({
          path: "/index"
        });
      }
    }
  };
</script>
