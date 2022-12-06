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

export const AjaxUtils = {
    login: async (data) => {
        let resp
        await $.post(getTokenUrl, data, (res) => {
            console.log(res)
            resp = res;
        })
        return resp
    },

    register: async (data) => {
        let resp
        await $.post(registerUrl, data, (res) => {
            resp = res
        })
        return resp
    },

    getInfo: async () => {
        let resp
        await $.ajax({
            url: getInfoUrl,
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            },
            success(res) {
                resp = res
            }
        })
        return resp
    },

    getUserList: async (data) => {
        let resp
        await $.ajax({
            url: getUserListUrl,
            data: data,
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            },
            success(res) {
                resp = res
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
                resp = res
            }
        })
        return resp;
    },
    addUser: async (data) => {
        let resp;
        await $.post(addUserUrl, data, (res) => {
            resp = res
        })
        return resp
    }
}