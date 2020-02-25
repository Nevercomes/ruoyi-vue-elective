<template>
  <div class="app-container mobile-container" ref="MobileContainer">
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" :topPullText="''"
      :bottomPullText="''" :bottomDropText="'释放加载'" :auto-fill="false" ref="loadmore">
      <ul>
        <li v-for="item in dataList" class="nl-crad-li">
          <nl-card :header="item.name">
            <nl-card-item :label="'学年学期'" :value="item.semester"></nl-card-item>
            <nl-card-item :label="'开始时间'" :value="parseTime(item.openTime)"></nl-card-item>
            <nl-card-item :label="'结束时间'" :value="parseTime(item.closeTime)"></nl-card-item>
            <!-- <el-divider></el-divider> -->
            <router-link :to="'/mobile/select/student/course/' + item.id" class="link-type">
              <el-button class="card-footer-item" size="mini" type="primary" icon="el-icon-date" v-hasPermi="['elective:select:list']">进入选课</el-button>
              <div class="clearfix"></div>
            </router-link>
          </nl-card>
        </li>
      </ul>
    </mt-loadmore>
  </div>
</template>

<script>
  import {
    listStudentOpen
  } from "@/api/elective/open/open";
  import NlCard from "@/components/NaLo/nl-card"
  import NlCardItem from "@/components/NaLo/nl-card-item"

  export default {
    name: "MobileStudentOpen",
    components: {
      NlCard,
      NlCardItem
    },
    data() {
      return {
        // 底部数据是否全部加载
        allLoaded: false,
        // 遮罩层
        loading: true,
        // 分页数据的总条数
        total: 0,
        // 数据列表
        dataList: [],
        // 开课时间字典
        semesterOptions: [],
        // 状态字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          semesterId: undefined,
          status: undefined
        }
      };
    },
    created() {
      this.getList();
    },
    mounted() {
      this.setHeight()
    },
    methods: {
      /** 查询开放选课列表 */
      getList() {
        this.loading = true;
        this.noMore = false;
        listStudentOpen(this.queryParams).then(response => {
          this.dataList = this.dataList.concat(response.rows);
          this.total = response.total;
          this.loading = false;
          this.calNoMore()
        });
      },
      resetQuery() {
        this.resetForm("queryForm");
      },
      loadTop() {
        this.getList()
        this.$refs.loadmore.onTopLoaded();
      },
      loadBottom() {
        this.calNoMore()
        if (this.allLoaded) return
        this.queryParams.pageNum = this.queryParams.pageNum + 1
        this.getList()
        this.$refs.loadmore.onBottomLoaded();
      },
      setHeight() {
        this.$refs.MobileContainer.style.height = (document.body.clientHeight - 86) + 'px'
      },
      calNoMore() {
        if (this.total == 0) {
          this.allLoaded = true
        } else {
          this.allLoaded = this.dataList.length >= this.total ? true : false
        }
      }
    },

  };
</script>
<style>
  .nl-crad-li {
    padding: 5px 0;
  }

  .card-footer-item {
    float: right;
  }
</style>
