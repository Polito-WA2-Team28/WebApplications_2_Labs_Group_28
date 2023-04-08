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
        if (!res.ok) {
            const response = await res.json();
            console.log(response)
            console.log(res)
            throw response.error
        }
        return await res.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
}

async function createProfile(profile) {
    profile.registrationDate = new Date().toISOString().slice(0, 10);
    console.log("PROFILE",profile)
    try {
        const res = await fetch(url + "/profiles",
            {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(profile)
            })
        if (!res.ok) {
            const response = await res.json();
            console.log(response)
            console.log(res)
            throw response.error
        }
        return true
    } catch (error) {
        console.error(error);
        throw error;
    }

}

async function updateProfile(profile) {
    try {
        const res = await fetch(url + "/profiles/" + profile.email,
            {
                method: "PUT",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(profile)
            })
        if (!res.ok) {
            const response = await res.json();
            console.log(response)
            throw response.error
        }
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const API = { getAllProducts, getProductById, getProfileByEmailAddress, createProfile, updateProfile }
