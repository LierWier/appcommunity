import $ from 'jquery';
import store from "@/store";

const ip = `127.0.0.1`;
const port = 3001;
const cutUrl = `http://${ip}:${port}/api`

// user
const getTokenUrl = cutUrl + `/user/gettoken`;
const registerUrl = cutUrl + `/user/register`;
const getInfoUrl = cutUrl + `/user/getinfo`;
const getUserListUrl = cutUrl + `/user/getuserlist`;
const updateUserStatusUrl = cutUrl + `/user/updateStatus`;
const addUserUrl = cutUrl + `/user/add`;

// app
const getAppListUrl = cutUrl + `/app/getapplist`;
const getAppInfoUrl = cutUrl + `/app/getinfo`;
const getAppCategoryUrl = cutUrl + `/app/getappcategory`;
const updateAppStatusUrl = cutUrl + `/app/updatestatusbylist`;
const addAppUrl = cutUrl + `/app/add`;
const updateAppUrl = cutUrl + `/app/update`;
const deleteAppByListUrl = cutUrl + `/app/deletebylist`;

// appEvl
const appEvlShortUrl = cutUrl + `/appevl`;
const getAppEvlListUrl = appEvlShortUrl + `/getlist`;
const getAppEvlByLoginUserUrl = appEvlShortUrl + `/getbyloginuser`
const postAppEvlUrl = appEvlShortUrl + `/post`
const deleteAppEvlUrl = appEvlShortUrl + `/delete`
const likeAppEvlUrl = appEvlShortUrl + `/like`

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

    async getAppInfo(data) {
        return await $.get(getAppInfoUrl, data)
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

    async updateAppStatus(data) {
        return await $.post(updateAppStatusUrl, data)
    },

    async addApp(data) {
        return await $.post(addAppUrl, data)
    },

    async updateApp(data) {
        return await $.post(updateAppUrl, data)
    },

    async deleteAppByList(data) {
        return await $.post(deleteAppByListUrl, data)
    },

    async getAppEvlList(data) {
        return await $.ajax({
            url: getAppEvlListUrl,
            data: data,
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            }
        })
    },

    async getAppEvlByLoginUser(data) {
        return await $.ajax({
            url: getAppEvlByLoginUserUrl,
            data: data,
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            }
        })
    },

    async postAppEvl(data) {
        return await $.post(postAppEvlUrl, data);
    },

    async deleteAppEvl(data) {
        return await $.ajax({
            url: deleteAppEvlUrl,
            data: data,
            type: "post",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            }
        })
    },

    async likeAppEvl(data) {
        return await $.ajax({
            url: likeAppEvlUrl,
            data: data,
            type: "post",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            }
        })
    }

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