import {navigate} from "./router.js";

export async function apiFetch (url, options = {}) {
    const response = await fetch(url, {
        credentials: "include",
        headers: {"Content-type": "application/json", ...options.headers},
        ...options
    });

    if (response.status === 401) {
        console.log("Uautoriseret - sender dig tilbage...")
        navigate("/");
        return;
    }

    if (response.status === 403) {
        console.log("Nægtet adgang - sender dig tilbage...")
        return;
    }

    if(!response.ok) {
        const msg = await response.text();
        throw new Error(msg || `Anmodning fejlede: ${response.status}`)
    }

    return response;
}

export async function apiGetJson(url) {
    const response = await apiFetch(url, { method: "GET" });
    return await response.json();
}

export async function apiPostJson(url, body) {
        const response = await apiFetch(url, {
            method: "POST",
            body: JSON.stringify(body)
        });

        return response;
}
