import { authHeader, compositeHeader } from './util.js';
const url = "http://localhost:3000/api/experts";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getTickets(token) {
    const res = await fetch(url + "/tickets",
        { method: "GET", headers: authHeader(token) })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}


/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getTicket(token, ticketId) {
    const res = await fetch(url + "/tickets/" + ticketId,
        { method: "GET", headers: authHeader(token) })
    if (!res.ok) throw res.statusText

    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function resolveTicket(token, ticketId) {
    const res = await fetch(url + "/tickets/" + ticketId + "/resolve",
        { method: "PATCH", headers: authHeader(token) })
    if (!res.ok) throw res.statusText
}


/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function closeTicket(token, ticketId) {
    const res = await fetch(url + "/tickets/" + ticketId + "/close",
        { method: "PATCH", headers: authHeader(token) })
    if (!res.ok) throw res.statusText

    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function sendMessage(token, message, ticketId) {

    const formdata = new FormData();
    formdata.append("messageText", message);
    //formdata.append("attachments", []);

    console.log("message", message);

    formdata.forEach((value, key) => console.log(key + " " + value));
    const res = await fetch(url + "/tickets/" + ticketId + "/messages",
        {
            method: "POST", headers: { "Authorization": "Bearer " + token },
            body: formdata
        })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}


/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getMessages(token, ticketId) {
    const res = await fetch(url + "/tickets/" + ticketId + "/messages",
        { method: "GET", headers: authHeader(token) })
    if (!res.ok) throw res.statusText

    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getProducts(token) {
    const res = await fetch(url + "/products",
        { method: "GET", headers: authHeader(token) })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getProduct(token, productId) {
    const res = await fetch(url + "/products/" + productId,
        { method: "GET", headers: authHeader(token) })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

const expertAPI = {
    getTickets, getTicket, resolveTicket,
    closeTicket, sendMessage, getMessages,
    getProducts, getProduct
}

export default expertAPI;
