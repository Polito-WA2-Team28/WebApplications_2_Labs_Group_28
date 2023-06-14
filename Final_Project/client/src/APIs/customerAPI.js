const url = "http://localhost:8081/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getProfile() {
    const res = await fetch(url + "/customer/getProfile",
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
async function editProfile(profile) {
    const res = await fetch(url + "/auth/register",
        {
            method: "PATCH",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(profile)
        })
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
async function createTicket(ticket) {
    const res = await fetch(url + "/customers/tickets",
        { method: "POST", headers: {'Content-Type' : 'application/json'}, body: JSON.stringify(ticket)})
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
async function getTickets() {
    const res = await fetch(url + "/customers/tickets",
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
    const res = await fetch(url + "/customers/tickets/" + ticketId,
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
async function reopenTicket(ticketId) {
    const res = await fetch(url + "/customers/tickets/" + ticketId + "/reopen",
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
async function compileSurvey(ticketId) {
    const res = await fetch(url + "/customers/tickets/" + ticketId + "/compileSurvey",
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
    const res = await fetch(url + "/customers/tickets/" + ticketId + "/messages",
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
    const res = await fetch(url + "/customers/tickets/" + ticketId + "/messages",
        {method: "GET"})
    if (!res.ok) {
        const response = await res.json();
        throw response.error
    }
    const data = await res.json();
    return data;
}

export const customerAPI = { getProfile, editProfile , createTicket, getTickets, getTicket, reopenTicket, compileSurvey, sendMessage, getMessages}
