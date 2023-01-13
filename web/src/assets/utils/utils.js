const utils = {
    formClearEmpty(form) {
        const res = {}
        for (const i in form) if (form[i] !== null && form[i].length !== 0) res[i] = form[i]
        return res;
    },

    deepClone(obj) {
        let newObj = null;
        if (typeof (obj) == 'object' && obj !== null) {
            newObj = obj instanceof Array ? [] : {};
            for (let i in obj) newObj[i] = utils.deepClone(obj[i])
        } else newObj = obj;
        return newObj;
    }
}

export default utils