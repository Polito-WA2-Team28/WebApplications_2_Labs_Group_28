const url = "http://localhost:8081/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getTickets(token) {
    const res = await fetch(url + "/experts/tickets",
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
async function getTicket(token, ticketId) {
    const res = await fetch(url + "/experts/tickets/" + ticketId,
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
async function resolveTicket(token, ticketId) {
    const res = await fetch(url + "/experts/tickets/" + ticketId + "/resolve",
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
    const res = await fetch(url + "/experts/tickets/" + ticketId + "/close",
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
async function sendMessage(token, message, ticketId) {
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

export const expertAPI = { getTickets, getTicket, resolveTicket, closeTicket, sendMessage, getMessages}
