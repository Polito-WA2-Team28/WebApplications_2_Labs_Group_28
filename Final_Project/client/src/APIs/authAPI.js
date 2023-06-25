import { compositeHeader, jsonHeader } from './util.js';
const url = "http://localhost:3000/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function login(credentials) {
    const res = await fetch(url + "/auth/login",
        {
            method: "POST",
            headers: jsonHeader,
            body: JSON.stringify(credentials)
        })
        .catch((err) => { throw Error("Server error") })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data.accessToken;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function register(profile) {
    const res = await fetch(url + "/auth/register",
        {
            method: "POST",
            headers: jsonHeader,
            body: JSON.stringify(profile)
        })
        .catch((err) => { throw Error("Server error") })
    
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function editProfile(token, profile) {
    const res = await fetch(url + "/auth/register",
        {
            method: "PATCH",
            headers: compositeHeader(token),
            body: JSON.stringify(profile)
        })
        .catch((err) => { throw Error("Server error") })
    
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

const authAPI = { login, register, editProfile }

export default authAPI
