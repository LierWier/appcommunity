const utils = {
    formClearEmpty(form) {
        const res = {}
        for (const i in form) if (form[i] !== null && form[i].length !== 0) res[i] = form[i]
        return res;
    }
}

export default utils