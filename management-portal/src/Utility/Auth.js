import Cookies from "js-cookie"
const decodeJwt = require('jwt-decode');

const authTokenName = "token"

function writeAuthCookie(cookie) {
    const decodedJwt = decodeJwt(cookie)
    const now = new Date().getTime()

    // Convert seconds to milliseconds
    const expireTime = decodedJwt.exp * 1000

    // Convert seconds to days
    const timeToExpire = (expireTime - now) / 86400

    Cookies.set(authTokenName, cookie, { expires: timeToExpire })
}

function signout() {
    Cookies.remove(authTokenName)
}

function getAuthToken() {
    return Cookies.get(authTokenName)
}

function isLoggedIn() {
    return getJwtExpirationTime() >= new Date().getTime() / 10000
}

function getSubject() {
    try {
        const decodedJwt = decodeJwt(getAuthToken())

        return decodedJwt.sub
    } catch {
        return undefined;
    }
}

function getJwtIssueTime() {
    try {
        const decodedJwt = decodeJwt(getAuthToken())

        return decodedJwt.iat
    } catch {
        return undefined
    }
}

function getJwtExpirationTime() {
    try {
        const decodedJwt = decodeJwt(getAuthToken())

        return decodedJwt.exp
    } catch {
        return undefined
    }
}

function getUserAuthorities() {
    try {
        const decodedJwt = decodeJwt(getAuthToken())

        return decodedJwt.authorities
    } catch {
        return undefined
    }
}

function secondsToExpiration() {
    return getJwtExpirationTime() - getJwtIssueTime()
}

export default {
    isLoggedIn,
    writeAuthCookie,
    signout,
    getAuthToken,
    getSubject,
    getJwtIssueTime,
    getJwtExpirationTime,
    getUserAuthorities,
    secondsToExpiration
}