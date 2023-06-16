const url = "http://localhost:8081/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getTickets(token) {
    const res = await fetch(url + "/managers/tickets",
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
async function getTicket(token,ticketId) {
    const res = await fetch(url + "/managers/tickets/" + ticketId,
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
async function assignTicket(token,ticketId, ticketUpdateData) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/assign",
        {method: "PATCH", headers: {'Content-Type' : 'application/json', 'Authorization': 'Bearer ' + token}, body: JSON.stringify(ticketUpdateData)})
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
async function relieveExpert(token,ticketId) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/relieveExpert",
        {method: "PATCH", headers:{'Authorization': 'Bearer ' + token}})
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
async function closeTicket(token, ticketId) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/close",
        {method: "PATCH", headers:{'Authorization': 'Bearer ' + token}})
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
async function resumeProgress(token,ticketId, ticketUpdateData) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/resumeProgress",
        {method: "PATCH", headers:{'Authorization': 'Bearer ' + token}})
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
async function removeTicket(token, ticketId) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/remove",
        {method: "DELETE", headers:{'Authorization': 'Bearer ' + token}})
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
async function sendMessage(token,message, ticketId) {
    const res = await fetch(url + "/experts/tickets/" + ticketId + "/messages",
        {method: "POST", headers: {'Content-Type' : 'application/json', 'Authorization': 'Bearer ' + token}, body: JSON.stringify(message)})
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
async function getMessages(token, ticketId) {
    const res = await fetch(url + "/experts/tickets/" + ticketId + "/messages",
        {method: "GET", headers:{'Authorization': 'Bearer ' + token}})
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    const data = await res.json();
    return data;
}

export const mangagerAPI = { getTickets, getTicket, assignTicket, relieveExpert, closeTicket, resumeProgress, removeTicket, sendMessage, getMessages };
