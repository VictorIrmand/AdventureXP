import {showError} from "../utility/error-message.js";
import {navigate} from "../utility/router.js";

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
        showError("Forkert brugernavn eller adgangskode");
    }

    if (response.ok) {
        console.log("det virker");
        navigate("/homepage");
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
        showError(errorText);
    }

    if (response.ok) {
        console.log("Det virker")
    }
}



