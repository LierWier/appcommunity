const ModuleUser = {
    state: {
        info: {},
        is_login: false,
        token: "",
    },
    getters: {
    },
    mutations: {
        updateInfo(state, user) {
            state.info = user
        },
        updateToken(state, token) {
            state.token = token
            localStorage.setItem("jwt_token", token)
        },
        updateLogin(state, isLogin) {
            state.is_login = isLogin
        },
        logout(state) {
            state.user = {}
            state.is_login = false
            localStorage.removeItem("jwt_token")
        },
    },
    actions: {
    },
    modules: {
    }
}

export default ModuleUser;
