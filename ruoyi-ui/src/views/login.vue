<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <el-image style="width: 164px; height: 60px; margin: 0 auto; display: block;" :src="loginLogo"></el-image>
      <h3 class="title">卓越校园</h3>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <!-- <el-form-item prop="code">
        <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 63%" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" />
        </div>
      </el-form-item> -->
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button :loading="loading" size="medium" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
      <el-form-item>
        <h4 class="footer">快乐新桥 畅达天下</h4>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <el-row>Copyright © 2018-2019 <a href="http://haitun158.com" target="_blank">卓越软件</a> All Rights Reserved.</el-row>
      <el-row>Powered By 铸才网络科技</el-row>
    </div>
  </div>
</template>

<script>
  import {
    getCodeImg
  } from "@/api/login";
  import Cookies from "js-cookie";
  import {
    encrypt,
    decrypt
  } from '@/utils/jsencrypt'
  import loginLogo from '@/assets/logo/xinqiao_logo_high.jpg'

  export default {
    name: "Login",
    data() {
      return {
        codeUrl: "",
        cookiePassword: "",
        loginForm: {
          username: "xinqiao-admin",
          password: "adminxinqiao",
          rememberMe: false,
          code: "",
          uuid: ""
        },
        loginRules: {
          username: [{
            required: true,
            trigger: "blur",
            message: "用户名不能为空"
          }],
          password: [{
            required: true,
            trigger: "blur",
            message: "密码不能为空"
          }],
          // code: [{
          //   required: true,
          //   trigger: "change",
          //   message: "验证码不能为空"
          // }]
        },
        loading: false,
        redirect: undefined,
        loginLogo: loginLogo
      };
    },
    watch: {
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect;
        },
        immediate: true
      }
    },
    created() {
      this.getCode();
      this.getCookie();
    },
    methods: {
      getCode() {
        getCodeImg().then(res => {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        });
      },
      getCookie() {
        const username = Cookies.get("username");
        const password = Cookies.get("password");
        const rememberMe = Cookies.get('rememberMe')
        this.loginForm = {
          username: username === undefined ? this.loginForm.username : username,
          password: password === undefined ? this.loginForm.password : decrypt(password),
          rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
        };
      },
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true;
            if (this.loginForm.rememberMe) {
              Cookies.set("username", this.loginForm.username, {
                expires: 30
              });
              Cookies.set("password", encrypt(this.loginForm.password), {
                expires: 30
              });
              Cookies.set('rememberMe', this.loginForm.rememberMe, {
                expires: 30
              });
            } else {
              Cookies.remove("username");
              Cookies.remove("password");
              Cookies.remove('rememberMe');
            }
            this.$store
              .dispatch("Login", this.loginForm)
              .then(() => {
                this.loading = false;
                this.$router.push({
                  path: this.redirect || "/"
                });
              })
              .catch(() => {
                this.loading = false;
                this.getCode();
              });
          }
        });
      }
    }
  };
</script>

<style rel="stylesheet/scss" lang="scss">
  .login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-image: url("../assets/image/xinqiao-login-bg.jpg");
    background-size: cover;
  }

  .title {
    margin: 10px auto 20px auto;
    text-align: center;
    color: #707070;
  }

  .login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    padding: 25px 25px 5px 25px;

    .el-input {
      height: 38px;

      input {
        height: 38px;
      }
    }

    .input-icon {
      height: 39px;
      width: 14px;
      margin-left: 2px;
    }
  }

  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }

  .login-code {
    width: 33%;
    height: 38px;
    float: right;

    img {
      cursor: pointer;
      vertical-align: middle;
    }
  }

  .el-login-footer {
    height: 60px;
    line-height: 25px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
  }

  .el-login-footer a:hover {
    color: #1989fa;
    text-decoration: underline;
  }

  .footer {
    display: block;
    text-align: center;
    margin: 0px auto;
    font-size: 15px;
    opacity: 0.4;
  }

</style>
