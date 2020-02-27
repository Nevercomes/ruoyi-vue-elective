<template>
  <div v-if="show" class="no-data">暂无数据</div>
</template>

<script>

  export default {
    name: "NlNodata",
    props: {
      show: {
        type: Boolean,
        default: false
      }
    }
  }

</script>

<style>
  .no-data {
    color: #909399;
    line-height: 60px;
    margin: 0 auto;
    text-align: center;
  }
</style>
