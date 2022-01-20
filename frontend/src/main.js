import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css';
import store from './store'
import { loadGuard } from "./router/guard"
import axios from 'axios'
import moment from 'moment'

Vue.prototype.$moment = moment
Vue.prototype.$axios = axios
loadGuard(router,store)
Vue.use(Antd)
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
