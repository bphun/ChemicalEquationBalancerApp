function formatUrl(path, params) {
    let url = path + "?";

    if (params) {
        for (const [key, value] of Object.entries(params)) {
            url += key + "=" + value + "&"
        }
    }

    return url;
}

export { formatUrl }