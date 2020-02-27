<template>
  <div class="app-container height-100">
    <div class="flex-col-center height-100">
      <el-image style="width: 80%; height: 50%; margin-top: 5%;" :src="logoUrl" :fit="'contain'"></el-image>
      <h2>新桥小学选课系统欢迎您</h2>
      <div style="color: #97a8be; margin-top: 20px;">点击左上角可展开关闭功能菜单</div> 
    </div>
  </div>
</template>

<script>
  import logo from "@/assets/logo/xinqiao_logo_high.jpg"

  export default {
    name: 'Index',
    data() {
      return {
        logoUrl: logo
      }
    },
    methods: {

    },
    // beforeRouteEnter(to, from, next) {
    //   if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
    //     next({
    //       path: '/mobile'
    //     })
    //   } else next()
    // }
  }
</script>

<style lang="scss" scoped>

  .height-100 {
    height: 100%;
  }

  .flex-col-center {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

</style>
