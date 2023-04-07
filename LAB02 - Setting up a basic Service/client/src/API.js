const url = "http://localhost:3000/api";

async function getAllProducts() {
    return await fetch(url + "/products/")
        .then(response => response.json())
        .catch(error => { console.log(error); throw error; });
}

async function getProductById(id) {
    return await fetch(url + "/products/" + id)
        .then(response => response.json())
        .catch(error => { console.log(error); throw error; });
}

async function getProfileByEmailAddress(emailAddress) {
    console.log(url + "/profiles/" + emailAddress)
    try {
        const res = await fetch(url + "/profiles/" + emailAddress)
        console.log(res)
        if (!res.ok) throw res.statusText;
        return await res.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
}

async function createProfile(profile) {
    try {
        const res = await fetch(url + "/profiles/", { method: "POST", body: JSON.stringify(profile) })
        if (!res.ok) throw res.statusText
    } catch (error) {
        console.error(error);
        throw error;
    }
    
}

async function updateProfile(profile) {
    try {
        const res = await fetch(url + "/profiles/" + profile.email, { method: "PUT", body: JSON.stringify(profile) })
        if (!res.ok) throw res.statusText
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const API = { getAllProducts, getProductById, getProfileByEmailAddress, createProfile, updateProfile }
