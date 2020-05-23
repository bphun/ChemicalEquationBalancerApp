import Cookies from "js-cookie"
const decodeJwt = require('jwt-decode');

function writeAuthCookie(cookie) {
    Cookies.set("token", cookie)
}

function signout() {
    Cookies.remove("token")
}

function getAuthToken() {
    return Cookies.get("token")
}

function isLoggedIn() {
    return getJwtExpirationTime() >= new Date().getTime() / 10000
}

function getSubject() {
    try {
        let decodedJwt = decodeJwt(getAuthToken())

        return decodedJwt.sub
    } catch {
        return undefined;
    }
}

function getJwtIssueTime() {
    try {
        let decodedJwt = decodeJwt(getAuthToken())

        return decodedJwt.iat
    } catch {
        return undefined
    }
}

function getJwtExpirationTime() {
    try {
        let decodedJwt = decodeJwt(getAuthToken())

        return decodedJwt.exp
    } catch {
        return undefined
    }
}

function getUserAuthorities() {
    try {
        let decodedJwt = decodeJwt(getAuthToken())

        return decodedJwt.authorities
    } catch {
        return undefined
    }
}

function secondsToExpiration() {
    return getJwtExpirationTime() - getJwtIssueTime()
}

export default { isLoggedIn, writeAuthCookie, signout, getAuthToken, getSubject, getJwtIssueTime, getJwtExpirationTime, getUserAuthorities, secondsToExpiration }