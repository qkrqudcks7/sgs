<template>
  <div>
    일반 유저 모드입니다. 꾸밀 시간이 없네요.
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "main",
  computed: {
    ...mapGetters("account",["user","authToken"])
  },
  mounted() {
    console.log(this.user)
    console.log(this.authToken)
  }
}
</script>

<style scoped>

</style>