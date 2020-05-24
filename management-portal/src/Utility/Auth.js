import Cookies from "js-cookie"
const decodeJwt = require('jwt-decode');

const authTokenName = "token"

function writeAuthCookie(cookie) {
    let decodedJwt = decodeJwt(cookie)
    let now = new Date().getTime()
    let expireTime = decodedJwt.exp * 1000
    let timeToExpire = expireTime - now

    console.log({ now: now, exp: expireTime, tte: timeToExpire, expireDay: timeToExpire / 86400 })

    Cookies.set(authTokenName, cookie, { expires: timeToExpire / 86400 })
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