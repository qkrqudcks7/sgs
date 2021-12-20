import axios from "axios";
import store from "@/store/index";
import router from "@/router/index";

const BASE = `http://3.38.10.189:8000/auth-server`

const RESTCALL = axios.create({
    baseURL: BASE,
    timeout: 10000
})

RESTCALL.interceptors.request.use(
    function (config) {
        let authInfo = store.getters["account/authToken"]

        if (authInfo) {
            config.headers.authorization = authInfo
        }
        return config
    },
    function (error) {
        return Promise.reject(error)
    }
)

RESTCALL.interceptors.response.use(
    function (response) {
        return response
    },
    function (error) {
        if (error.response?.status === 403 && window.location.pathname !== "/login") {
            router.push({
                name: "root",
                params: { message: "resetStore", type: "sessionOut" }
            })
        }
        return Promise.reject(error);
    }
)

export default RESTCALL;