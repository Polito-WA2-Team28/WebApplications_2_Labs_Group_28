const url = "http://localhost:8081/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function login(email, password) {
    const res = await fetch(url + "/auth/login",
        {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: {
                "username": email,
                "password": password
            }
        })
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    const data = await res.json();
    return data;
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
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    const data = await res.json();
    return data;
}

const authAPI = { login, register }

export default authAPI
