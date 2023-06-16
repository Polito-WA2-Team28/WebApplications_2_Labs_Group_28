const url = "http://localhost:3001/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getProducts(token) {
    const res = await fetch(url + "/products",
        {method: "GET", headers:{'Authorization': 'Bearer ' + token}})
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
async function getProduct(productId) {
    const res = await fetch(url + "/products/" + productId,
        {method: "GET"})
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    const data = await res.json();
    return data;
}


export const productAPI = { getProducts, getProduct }
