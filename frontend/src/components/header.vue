<template>
  <div>
    <a-page-header
        style="border: 1px solid rgb(235, 237, 240)"
        title="SGS-Parks"
        :sub-title="user.role === `ROLE_ADMIN` ? `관리자 페이지입니다` : `유저 페이지입니다`"
    >
      <template slot="extra">
        <a-button>
          {{user.name}}님 어서오세요
        </a-button>
        <a-button @click="showModal">
          내 정보
        </a-button>
        <a-button @click="logout">
          로그아웃
        </a-button>
      </template>
    </a-page-header>
    <a-modal v-model="visible" title="내 정보" @ok="handleOk">
      <p>이름: {{user.name}}</p>
      <p>이메일: {{user.email}}</p>
      <p>권한: {{user.role}}</p>
    </a-modal>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from "vuex";

export default {
  name: "header",
  data() {
    return {
      visible: false
    }
  },
  computed: {
    ...mapGetters("account",["user","authToken"])
  },
  methods: {
    ...mapMutations("account",["resetAccount"]),
    logout () {
      this.resetAccount()
      this.$router.go()
    },
    showModal() {
      this.visible = true;
    },
    handleOk(e) {
      console.log(e);
      this.visible = false;
    }
  }
}
</script>

<style scoped>
tr:last-child td {
  padding-bottom: 0;
}
</style>