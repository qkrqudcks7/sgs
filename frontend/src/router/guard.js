function loadGuard(router,store) {
    router.beforeEach((to,from,next) => {
        if ((from.path === "/" && to.path === "/login") || (from.path === "/" && to.path === "/")) {
            next()
        } else {
            const token = store.getters["account/authToken"]
            const user = store.getters["account/user"]

            if (to.path === "/main/dashboard") {
                console.log(user.role)
                if (user.role === "ROLE_ADMIN") {
                    alert("관리자 모드로 입장합니다")
                    next({name: "관리자모드"})
                }
            }

            if (to.params?.message === "resetStore") {
                if (to.params?.type === "logout") {
                    next({name: "로그인", params: {message: "logOut"}})
                } else if (to.params?.type === "sessionOut") {
                    next({name: "로그인", params: {message: "sessionOut"}})
                }
                clearStore();
            } else if (token === undefined) {
                next({name: "로그인", params: {message: "sessionOut"}})
                clearStore()
            } else {
                next()
            }
        }
        function clearStore() {
            store.commit("account/resetAccount")
        }
    })
}

export { loadGuard }