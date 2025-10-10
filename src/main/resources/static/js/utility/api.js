import {navigate} from "./router.js";
import {showMessage} from "./message.js";

export async function apiFetch(url, options = {}) {
    try {
        const response = await fetch(url, {
            credentials: "include",
            headers: {"Content-Type": "application/json", ...options.headers},
            ...options
        });

        // 400 - BAD REQUEST / Validation (MethodArgumentNotValidException)
        if (response.status === 400) {
            const msg = await response.text();
            showMessage(msg || "Validation failed.", "error");
            return null;
        }

        // 401 - Unauthorized (Spring Security smider)
        if (response.status === 401) {
            console.log("Unauthorized - redirecting...");
            navigate("/");
            return null;
        }

        // 403 - Forbidden (AccessDenied) spring security smider
        if (response.status === 403) {
            console.log("Access denied - redirecting...");
            navigate("/");
            return null;
        }

        // 404 - NotFoundException
        if (response.status === 404) {
            const msg = await response.text();
            showMessage(msg || "Resource not found.", "error");
            return null;
        }

        // 409 - Conflict (DuplicateResourceException)
        if (response.status === 409) {
            const msg = await response.text();
            showMessage(msg || "Conflict: duplicate or invalid resource.", "error");
            return null;
        }

        // 500 - Internal Server Error (DatabaseAccessException)
        if (response.status === 500) {
            showMessage("A system error occurred. Please try again later.", "error");
            return null;
        }

        return response;
    } catch (err) { // hvis fetch ikke virker
        console.error("Network or fetch error:", err);
        showMessage("Failed to connect to server.", "error");
        return null;
    }
}


export async function apiGetJson(url) {
    const response = await apiFetch(url, {method: "GET"});
    if (!response) return null;
    return await response.json();
}

export async function apiPostJson(url, body) {
    const response = await apiFetch(url, {
        method: "POST",
        body: JSON.stringify(body)
    });
    return response ?? null;
}

export async function apiPutJson(url, body) {
    const response = await apiFetch(url, {
        method: "PUT",
        body: JSON.stringify(body)
    });

    return response ?? null;
}
