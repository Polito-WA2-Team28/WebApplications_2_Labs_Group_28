const url = "http://localhost:3001/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function login(credentials) {
    const res = await fetch(url + "/auth/login",
        {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credentials)
        })
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
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(profile)
        })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

const authAPI = { login, register }

export default authAPI
