const formatterUtils = {
    userStatusFmt(v) {
        if (v == 1) return "正常";
        if (v == 0) return "封禁";
        if (v == -1) return "注销";
    },

    isManagerFmt(v) {
        return v < 1 ? "否" : "是"
    }
}

export default formatterUtils;