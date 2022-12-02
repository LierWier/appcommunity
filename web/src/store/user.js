const ModuleUser = {
    state: {
        user: {},
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.user = user
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
