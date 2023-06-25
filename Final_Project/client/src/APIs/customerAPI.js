import { compositeHeader, authHeader } from "./util";

const url = "http://localhost:3000/api/customers";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getProfile(token) {
    const res = await fetch(url + "/getProfile",
        { method: "GET", headers: authHeader(token) })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function patchProfile(token, profile) {
    const res = await fetch(url + "/editProfile",
        {
            method: "PATCH", headers: compositeHeader(token),
            body: JSON.stringify(profile)
        })
    if (!res.ok) throw res.statusText
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function createTicket(token, ticket) {
    const res = await fetch(url + "/tickets",
        {
            method: "POST", headers: compositeHeader(token),
            body: JSON.stringify(ticket)
        })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}


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
async function reopenTicket(token, ticketId) {
    const res = await fetch(url + "/tickets/" + ticketId + "/reopen",
        { method: "PATCH", headers: authHeader(token) })
    if (!res.ok) throw res.statusText
}


/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function compileSurvey(token, ticketId) {
    const res = await fetch(url + "/tickets/" + ticketId + "/compileSurvey",
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

const customerAPI = {
    getProfile, createTicket, getTickets, getTicket, patchProfile,
    reopenTicket, compileSurvey, sendMessage, getMessages, getProducts, getProduct
}

export default customerAPI
