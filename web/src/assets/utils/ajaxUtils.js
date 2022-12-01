import $ from 'jquery';

const ip = `127.0.0.7`;
const port = 3000;
const cutUrl = `https://${ip}:${port}`
const loginUrl = cutUrl + `/login`;
const registerUrl = cutUrl + `/register`;

export const AjaxUtils = {
    login(data) {
        $.post(loginUrl, data, (resp) => {
            console.log(resp);
            return resp;
        })
    },

    register(data) {
        $.post(registerUrl, data, (resp) => {
            console.log(resp);
            return resp;
        })
    }
}