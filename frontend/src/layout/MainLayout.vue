<template>
  <a-layout>
    <h-header></h-header>
    <router-view />
  </a-layout>
</template>

<script>
import HHeader from "@/components/header";
export default {
  name: "MainLayout",
  components: {
    HHeader
  }
}
</script>

<style scoped>
</style>