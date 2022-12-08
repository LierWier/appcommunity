import $ from 'jquery';
import store from "@/store";

const ip = `127.0.0.1`;
const port = 3001;
const cutUrl = `http://${ip}:${port}/api`

const getTokenUrl = cutUrl + `/user/gettoken`;
const registerUrl = cutUrl + `/user/register`;
const getInfoUrl = cutUrl + `/user/getinfo`;
const getUserListUrl = cutUrl + `/user/getuserlist`;
const updateUserStatusUrl = cutUrl + `/user/updateStatus`;
const addUserUrl = cutUrl + `/user/add`;

const getAppListUrl = cutUrl + `/app/getapplist`;
const getAppCategoryUrl = cutUrl + `/app/getappcategory`;
// const updateAppUrl = cutUrl + `/app/update`;
const updateAppStatusUrl = cutUrl + `/app/updatestatusbylist`;

export const AjaxUtils = {
    login: async (data) => {
        let resp;
        await $.post(getTokenUrl, data, (res) => {
            console.log(res)
            resp = res;
        })
        return resp;
    },

    register: async (data) => {
        let resp;
        await $.post(registerUrl, data, (res) => {
            resp = res;
        })
        return resp;
    },

    getInfo: async () => {
        let resp;
        await $.ajax({
            url: getInfoUrl,
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            },
            success(res) {
                resp = res;
            }
        })
        return resp;
    },

    getUserList: async (data) => {
        let resp;
        await $.ajax({
            url: getUserListUrl,
            data: data,
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            },
            success(res) {
                resp = res;
            }
        })
        return resp;
    },

    updateUserStatus: async (data) => {
        let resp;
        await $.ajax({
            url: updateUserStatusUrl,
            data: data,
            type: "post",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            },
            success(res) {
                resp = res;
            }
        })
        return resp;
    },

    addUser: async (data) => {
        let resp;
        await $.post(addUserUrl, data, (res) => {
            resp = res;
        })
        return resp;
    },

    getAppList: async (data) => {
        let resp;
        await $.get(getAppListUrl, data, (res) => {
            resp = res;
        })
        return resp;
    },

    getAppCategory: () => {
        return fetch(getAppCategoryUrl).then(res => { return res.json() })
    },

    // updateApp: (data) => {
    //
    // },

    async updateAppStatus(data) {
        return await $.post(updateAppStatusUrl, data)
    },

    // text() {
    //     const json = []
    //     for (let i = 100000; i < 100500; i++) {
    //         const data = {}
    //         data["username"] = i + ""
    //         data["password"] = i + ""
    //         const r = Math.floor(Math.random() * 3)
    //         if (r === 2) data["sex"] = "男"
    //         else if (r === 1) data["sex"] = "女"
    //         json.push(data)
    //     }
    //     console.log(JSON.stringify(json))
    // }
}