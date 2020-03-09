<template>
  <div class="label-item">
    <div class="laebl-item_label">{{label}}</div>
    <div class="label-item_value">{{value}}</div>
  </div>
</template>

<script>
  export default {
    name: "NlLabelItem",
    props: {
      label: {
      },
      value: {
      }
    }
  }
</script>

<style>

  .label-item {
    width: 100%;
    padding: 6px 0;
    display: flex;
    flex-direction: row;
  }

  .laebl-item_label {
    color: #515a6e;
    font-weight: bold;
    width: 56px;
    margin-right: 15px;
    text-align: right;
  }

  .label-item_value {
    width: calc(100% - 71px);
    /* flex-grow: 1; */
  }

  /* .card-item_laebl {
    color: #515a6e;
    font-weight: bold;
    min-width: 60px;
    text-align: justify;
    margin-right: 20;
    vertical-align: middle;
  } */

  /* .laebl-item_label:after {
    content: '';
    width: 100%;
    display: inline-block;
    overflow: hidden;
    height: 0;
  } */

</style>
