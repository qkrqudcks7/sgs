import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/login";
import BlankLayout from "../layout/blankLayout";
import MainLayout from "../layout/MainLayout";

Vue.use(VueRouter)

const routes = [
    {
        path: "/login",
        name: "로그인",
        component: Login
    },
    {
        path: "/",
        name: "root",
        component: BlankLayout,
        children: [
            {
                path: "main",
                name: "메인",
                component: MainLayout,
                children: [
                    {
                        path: "dashboard",
                        name: "대시보드",
                        component: () => import("@/views/dashboard.vue")
                    },
                    {
                        path: "admin-dashboard",
                        name: "관리자모드",
                        component: () => import("@/views/adminDashboard.vue")
                    }
                ]
            }
        ]
    }
]

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes
})

export default router