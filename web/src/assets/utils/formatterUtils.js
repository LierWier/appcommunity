const formatterUtils = {
    userStatusFmt(v) {
        if (v == 1) return "正常";
        if (v == 0) return "封禁";
        if (v == -1) return "注销";
    },

    isManagerFmt(v) {
        return v < 1 ? "否" : "是";
    },

    appStatusFmt(v) {
        if (v == 1) return "正常";
        if (v == 0) return "下架";
        if (v == -1) return "已删除";
    },

    appDownloadsFmt(v) {
        if (v >= 1000000) return (v / 1000000).toFixed(2) + " M";
        if (v >= 1000) return (v / 1000).toFixed(1) + " K";
        return v;
    }
}

export default formatterUtils;