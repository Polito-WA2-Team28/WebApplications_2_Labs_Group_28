import dayjs from "dayjs";
const url = "http://localhost:3000/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getAllProducts() {
    const res = await fetch(url + "/products")
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    return await res.json();
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getProductById(id) {
    const res = await fetch(url + "/products/" + id)
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    return await res.json();
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getProfileByEmailAddress(emailAddress) {
    const res = await fetch(url + "/profiles/" + emailAddress)
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    return await res.json();
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function createProfile(profile) {
    profile.registrationDate = dayjs().format("YYYY-MM-DD").toString();
    const res = await fetch(url + "/profiles",
        {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(profile)
        })
    if (!res.ok) {
        const response = await res.json();
        throw response.body
    }
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function updateProfile(email, profile) {
    const res = await fetch(url + "/profiles/" + email,
        {
            method: "PUT",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(profile)
        })
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
}

export const API = { getAllProducts, getProductById, getProfileByEmailAddress, createProfile, updateProfile }
