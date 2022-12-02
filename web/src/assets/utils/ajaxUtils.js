import $ from 'jquery';

const ip = `127.0.0.1`;
const port = 3001;
const cutUrl = `http://${ip}:${port}/api`
const getTokenUrl = cutUrl + `/user/gettoken`;
const registerUrl = cutUrl + `/user/register`;
const getInfoUrl = cutUrl + `/user/getinfo`;

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

    getInfo: async (data) => {
        let resp
        await $.ajax({
            url: getInfoUrl,
            type: "get",
            headers: {
                Authorization: "Bearer " + data
            },
            success(res) {
                resp = res
            }
        })
        return resp
    }
}