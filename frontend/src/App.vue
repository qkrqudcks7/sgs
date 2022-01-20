<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { mapState, mapGetters } from "vuex";

export default {
  name: 'App',
  mounted() {
    if (this.user !== undefined && this.authToken !== undefined) {
      this.$router.replace("/main/dashboard")
    }
    else {
      this.$router.replace("/login");
    }
  },
  computed: {
    ...mapState("account",["user","authToken"]),
    ...mapGetters("account",["user","authToken"])
  }
}
</script>

<style>

</style>
