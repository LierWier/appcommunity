import {AjaxUtils} from "@/assets/utils/ajaxUtils";

const ModuleUser = {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = ~~user.id  // 确保id都为number类型，防止以后在比较时出现bug
            state.username = user.username
            state.photo = user.photo
            state.is_login = user.is_login
        },
        updateToken(state, token) {
            state.token = token
        },
        logout(state) {
            state.id = ''
            state.username = ''
            state.photo = ''
            state.token = ''
            state.is_login = false
        },
    },
    actions: {
        login(context, data) {
            AjaxUtils.login(data);
        },

        getInfo(context, data) {
            AjaxUtils.getInfo(data);
        },

        logout(context) {
            localStorage.removeItem("jwt_token")
            context.commit("logout")
        },
    },
    modules: {
    }
}

export default ModuleUser;
