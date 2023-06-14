const url = "http://localhost:8081/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getTickets() {
    const res = await fetch(url + "/experts/tickets",
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
    const res = await fetch(url + "/experts/tickets/" + ticketId,
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
async function resolveTicket(ticketId) {
    const res = await fetch(url + "/experts/tickets/" + ticketId + "/resolve",
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
    const res = await fetch(url + "/experts/tickets/" + ticketId + "/close",
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

export const expertAPI = { getTickets, getTicket, resolveTicket, closeTicket, sendMessage, getMessages}
