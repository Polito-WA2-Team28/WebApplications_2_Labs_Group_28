const url = "http://localhost:3000/api";

async function getAllProducts() {
    try {
        const res = await fetch(url + "/products/")
        if (!res.ok) {
            const response = await res.json();
            throw response.error
        }
        return await res.json();
    } catch (error) {
        throw error;
    }
}

async function getProductById(id) {
    try {
        const res = await fetch(url + "/products/" + id)
        if (!res.ok) {
            const response = await res.json();
            throw response.error
        }
        return await res.json();
    } catch (error) {
        throw error;
    }
}

async function getProfileByEmailAddress(emailAddress) {
    console.log(url + "/profiles/" + emailAddress)

    try {
        const res = await fetch(url + "/profiles/" + emailAddress)
        if (!res.ok) {
            const response = await res.json();
            throw response.error
        }
        return await res.json();
    } catch (error) {
        throw error;
    }
}

async function createProfile(profile) {
    profile.registrationDate = new Date().toISOString().slice(0, 10);
    try {
        const res = await fetch(url + "/profiles",
            {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(profile)
            })
        if (!res.ok) {
            const response = await res.json();
            throw response.error
        }
        return true
    } catch (error) {
        throw error;
    }

}

async function updateProfile(email,profile) {
    try {
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
    } catch (error) {
        throw error;
    }
}

export const API = { getAllProducts, getProductById, getProfileByEmailAddress, createProfile, updateProfile }
