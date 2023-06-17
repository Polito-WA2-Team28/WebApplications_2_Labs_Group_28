const url = "http://localhost:3001/api";

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getProfile(token) {
    const res = await fetch(url + "/customers/getProfile",
        {
            method: "GET",
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function editProfile(token, profile) {
    const res = await fetch(url + "/auth/register",
        {
            method: "PATCH",
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body: JSON.stringify(profile)
        })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}


/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function createTicket(token, ticket) {
    const res = await fetch(url + "/customers/tickets",
        {
            method: "POST", headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
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
    const res = await fetch(url + "/customers/tickets",
        {
            method: "GET",
            headers: { 'Authorization': 'Bearer ' + token }
        })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}


/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function getTicket(token, ticketId) {
    const res = await fetch(url + "/customers/tickets/" + ticketId,
        { method: "GET", headers: { 'Authorization': 'Bearer ' + token } })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function reopenTicket(token, ticketId) {
    const res = await fetch(url + "/customers/tickets/" + ticketId + "/reopen",
        { method: "PATCH", headers: { 'Authorization': 'Bearer ' + token } })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}


/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function compileSurvey(token, ticketId) {
    const res = await fetch(url + "/customers/tickets/" + ticketId + "/compileSurvey",
        { method: "PATCH", headers: { 'Authorization': 'Bearer ' + token } })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

/** 
* @throws {Error} if the data fails
* @throws {String} if the response is not ok
*/
async function sendMessage(token, message, ticketId) {
    const res = await fetch(url + "/customers/tickets/" + ticketId + "/messages",
        {
            method: "POST", headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body: JSON.stringify(message)
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
    const res = await fetch(url + "/customers/tickets/" + ticketId + "/messages",
        {
            method: "GET", headers: { 'Authorization': 'Bearer ' + token }
        })
    if (!res.ok) throw res.statusText
    const data = await res.json();
    return data;
}

export const customerAPI = { getProfile, editProfile, createTicket, getTickets, getTicket, reopenTicket, compileSurvey, sendMessage, getMessages }
