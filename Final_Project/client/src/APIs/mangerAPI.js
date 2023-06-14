const url = "http://localhost:8081/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getTickets() {
    const res = await fetch(url + "/managers/tickets",
        {method: "GET"})
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
async function getTicket(ticketId) {
    const res = await fetch(url + "/managers/tickets/" + ticketId,
        {method: "GET"})
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
async function assignTicket(ticketId, ticketUpdateData) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/assign",
        {method: "PATCH", headers: {'Content-Type' : 'application/json'}, body: JSON.stringify(ticketUpdateData)})
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
async function relieveExpert(ticketId) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/relieveExpert",
        {method: "PATCH"})
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
async function closeTicket(ticketId) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/close",
        {method: "PATCH"})
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
async function resumeProgress(ticketId, ticketUpdateData) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/resumeProgress",
        {method: "PATCH"})
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
async function removeTicket(ticketId) {
    const res = await fetch(url + "/managers/tickets/" + ticketId + "/remove",
        {method: "DELETE"})
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
async function sendMessage(message, ticketId) {
    const res = await fetch(url + "/experts/tickets/" + ticketId + "/messages",
        {method: "POST", headers: {'Content-Type' : 'application/json'}, body: JSON.stringify(message)})
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
async function getMessages(ticketId) {
    const res = await fetch(url + "/experts/tickets/" + ticketId + "/messages",
        {method: "GET"})
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    const data = await res.json();
    return data;
}

export const mangagerAPI = { getTickets, getTicket, assignTicket, relieveExpert, closeTicket, resumeProgress, removeTicket, sendMessage, getMessages };
