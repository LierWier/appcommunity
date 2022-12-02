import { createStore } from 'vuex'
import ModuleUser from "@/store/user";

export default createStore({
  state: {
    loginDialogVisible: false
  },
  getters: {
  },
  mutations: {
    updateLoginDialogVisible(state, data) {
      state.loginDialogVisible = data
    },
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
  }
})
