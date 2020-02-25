<template>
  <el-row class="card-item_box">
    <el-col :span="6" v-if="label"><span class="card-item_laebl">{{ label }}</span></el-col>
    <el-col :span="18" v-if="value"><span class="card-item_vlaue">{{ value }}</span></el-col>
    <slot></slot>
  </el-row>
</template>

<script>
  export default {
    name: "NlCardItem",
    props: {
      label: {
        type: String,
        default: ''
      },
      value: {
        type: String,
        default: ''
      }
    }
  }
</script>

<style>
  .card-item_box {
    width: 100%;
    padding: 6px 0;
    display: flex;
  }

  .card-item_laebl {
    color: #515a6e;
    font-weight: bold;
    min-width: 60px;
    text-align: justify;
    margin-right: 20;
    vertical-align: middle;
  }
  
  .card-item_laebl:after {
    content: '';
    width: 100%;
    display: inline-block;
    overflow: hidden;
    height: 0;
    
  }
</style>
