<template>
  <el-card class="box-card box-card_font el-card">
    <div v-if="header" slot="header" class="clearfix header">
      <span>{{header}}</span>
    </div>
    <slot></slot>
  </el-card>
</template>

<script>
  export default {
    name: 'NlCard',
    props: {
       header: {
         type: String
       }
    },
    computed: {

    },
    methods: {

    }
  }
</script>

<style>
  .box-card {
    width: 100%;
  }

  .box-card_font {
    font-size: 14px;
    color: #606266;
  }
  
  .header {
    font-weight: bold;
  }
  
</style>
