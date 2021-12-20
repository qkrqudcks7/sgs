<template>
  <div>
    <doughnut-chart
        ref="faultchart"
        v-if="loaded"
        :data="faultData"
        :height="100">
    </doughnut-chart>
    <a-table
        ref="table"
        class="usertable"
        :columns="columns"
        :data-source="data"
        rowKey="email"
        size="small"
        :rowClassName="rowClassName"
        :customRow="clickRow"
        bordered
    >
    </a-table>
    <a-modal ref="modal" v-model="visible" title="권한 변경" ok-text="변경하기" cancel-text="취소" @ok="modifyRole">
      <a-form-model
          ref="ruleForm"
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
      >
        <p>이메일: {{form.email}}</p>
        <p>이름: {{form.name}}</p>
        <p>권한: {{form.role}}</p>
        <a-select :default-value="form.role" style="width: 200px" @change="handleChange">
          <a-select-option value="ROLE_MASTER" disabled>
            ROLE_MASTER
          </a-select-option>
          <a-select-option value="ROLE_ADMIN">
            ROLE_ADMIN
          </a-select-option>
          <a-select-option value="ROLE_USER">
            ROLE_USER
          </a-select-option>
        </a-select>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
const columns = [
  {
    title: "이름",
    dataIndex: "name",
    ellipsis: true
  },
  {
    title: "이메일",
    dataIndex: "email",
    ellipsis: true
  },
  {
    title: "권한",
    dataIndex: "role",
    ellipsis: true
  },
]
import DoughnutChart from "@/components/chart/DoughnutChart";
import api from "@/api/api";
export default {
  name: "adminDashboard",
  components: {
    DoughnutChart
  },
  data () {
    return {
      columns,
      data: [],
      selectedRowIndex: null,
      faultData: {
        labels: ["관리자", "사용자", "Master"],
        datasets: [
          {
            borderWidth: 2,
            backgroundColor: ["#FF5500", "#F8B16D", "#2CB5F2"],
            data: []
          }
        ]
      },
      faultChartLegend: null,
      loaded: false,
      visible: false,
      form: '',
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
      changedRole: ''
    }
  },
  async mounted() {
    this.loaded = false
    await api.users.userList()
    .then(async res => {
      if (res.status===200) {
        this.data = res.data
        let u = 0
        let a = 0
        for (let i = 0;i<res.data.length;i++) {
          if (res.data[i].role ==="ROLE_ADMIN") {
            a+=1
          } else {
            u+=1
          }
        }
        this.faultData.datasets[0].data = [a,u,1]
        this.loaded = true
      }
    })

  },
  methods: {
    async getUserList () {
      this.loaded = false
      await api.users.userList()
          .then(async res => {
            if (res.status===200) {
              this.data = res.data
              let u = 0
              let a = 0
              for (let i = 0;i<res.data.length;i++) {
                if (res.data[i].role ==="ROLE_ADMIN") {
                  a+=1
                } else {
                  u+=1
                }
              }
              this.faultData.datasets[0].data = [a,u,1]
              this.loaded = true
            }
          })
    },
    rowClassName(record,index) {
      return index === this.selectedRowIndex ? "click-active-row" : ""
    },
    clickRow(record,index) {
      return {
        on: {
          click: () => {
            console.log(record.email)
            this.form = {
              email: record.email,
              name: record.name,
              role: record.role
            }
            console.log(this.form)
            this.visible = true
            this.selectedRowIndex = index
          }
        }
      }
    },
    handleChange(value) {
      this.changedRole = value
    },
    modifyRole() {
      api.users.changeRole({
        email: this.form.email,
        role: this.changedRole
      })
      .then(res => {
        if (res.status === 200) {
          alert(this.changedRole + "으로 변경되었습니다.")
          this.visible = false
          this.getUserList()
        } else {
          alert("오류가 생겼습니다.")
          this.visible = false
        }
      })
    }
  }
}
</script>

<style scoped>

</style>