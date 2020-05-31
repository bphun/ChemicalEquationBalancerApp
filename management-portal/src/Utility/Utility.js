function formatUrl(path, params) {
    let url = path + "?";

    if (params) {
        for (const [key, value] of Object.entries(params)) {
            url += key + "=" + value + "&"
        }
    }

    return url;
}

function netInterfaceIpAddress() {
    const os = require('os');
    const ifaces = os.networkInterfaces();
    var ipAddresses = {}

    Object.keys(ifaces).forEach(function (ifname) {
        var alias = 0;

        ifaces[ifname].forEach(function (iface) {
            if ('IPv4' !== iface.family || iface.internal !== false) {
                // skip over internal (i.e. 127.0.0.1) and non-ipv4 addresses
                return;
            }

            if (alias >= 1) {
                // this single interface has multiple ipv4 addresses
                ipAddresses[ifname] = [alias, iface.address]
            } else {
                // this interface has only one ipv4 adress
                ipAddresses[ifname] = iface.address
            }
            ++alias;
        });
    });
}

export { formatUrl, netInterfaceIpAddress }