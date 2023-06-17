export function authHeader(token) {
    return { 'Authorization': 'Bearer ' + token }
}

export const jsonHeader = { 'Content-Type': 'application/json' }

export function compositeHeader(token) {
    return { ...authHeader(token), ...jsonHeader }
}