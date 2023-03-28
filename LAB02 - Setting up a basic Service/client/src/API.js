const url = "http://localhost:3000/API";

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
    return await fetch(url + "/profiles/" + emailAddress)
        .then(response => response.json())
        .catch(error => { console.log(error); throw error; });
}

async function createProfile(profile) {
    const response = await fetch(url + "/profiles/", { method: "POST", body: JSON.stringify(profile) })
        .catch(error => { console.log(error); throw error; });
    if (!response.ok) throw new Error("Error creating profile");
}

async function updateProfile(profile) {
    const response = await fetch(url + "/profiles/", { method: "PUT", body: JSON.stringify(profile) })
        .catch(error => { console.log(error); throw error; });
    if (!response.ok) throw new Error("Error updating profile");
}

export const API = { getAllProducts, getProductById, getProfileByEmailAddress, createProfile, updateProfile }
