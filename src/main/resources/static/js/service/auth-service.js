import {showMessage} from "../utility/message.js";
import {navigate} from "../utility/router.js";
import {apiFetch, apiGetJson} from "../utility/api.js";

export async function login(loginRequestDTO) {

    const response = await fetch("/api/auth/login", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-type": "application/json",
        },
        body: JSON.stringify(loginRequestDTO)
    });

    if (response.status === 401) {
        console.log("fejlet validation");
        showMessage("Forkert brugernavn eller adgangskode", "error");
    }

    if (response.ok) {
        console.log("det virker");
        navigate("/home");
    }
}

export async function signUp(signUpRequestDTO) {
    const response = await fetch("/api/auth/signup", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-type": "application/json",
        },
        body: JSON.stringify(signUpRequestDTO)
    });

    if (response.status === 400) {
        const errorText = await response.text();
        console.log(errorText)
        showMessage(errorText, "error");
    }

    if (response.ok) {
        console.log("User created");
        navigate("/");
    }
}

export async function getMe() {
    return await apiGetJson("/api/auth/me");
}

export async function logout() {
    const response = await apiFetch("/api/auth/logout", {method: "POST"});

    if (!response.ok) {
        console.log(await response.text());
    }


    console.log("Logout successful");
}


export async function checkAuth() {
    try {
        const userDTO = await getMe();

        if (userDTO) {
            navigate("/home");
        } else {
            navigate("/");
        }

    } catch (err) {
        console.error("Auth check failed:", err);
        navigate("/");
    }
}



