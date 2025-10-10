import {showMessage} from "../utility/message.js";
import {navigate} from "../utility/router.js";
import {apiFetch, apiGetJson, apiPostJson} from "../utility/api.js";

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
        console.log("Failed validation");
        showMessage("Wrong username or password", "error");
    }

    if (response.ok) {
        console.log("User with username: " + loginRequestDTO.username + " was successfully logged in");
        navigate("/home");
    }
}

export async function signUp(signUpRequestDTO) {
   try {
       const response = await fetch("/api/auth/signup", {
           method: "POST",
           credentials: "include",
           headers: {
               "Content-type": "application/json",
           },
           body: JSON.stringify(signUpRequestDTO)
       });


       // BAD REQUEST /Validation (MethodArgumentNotValidException)
       if (response.status === 400) {
           const errorText = await response.text();
           console.log(errorText)
           showMessage(errorText, "error");
           return null;
       }


       // 409 - Conflict (DuplicateResourceException)
       if (response.status === 409) {
           const msg = await response.text();
           showMessage(msg || "This user already exists.", "error");
           return null;
       }

       // 500 - Internal Server Error (DatabaseAccessException)
       if (response.status === 500) {
           showMessage("A system error occurred. Please try again later.", "error");
           return null;
       }

       if (response.ok) {
           console.log("User created");
           return await response.text();
           navigate("/");
       }
   } catch (err) {
       console.error("Network error:", err);
       showMessage("Failed to connect to server.", "error");
   }
}

export async function adminRegister (adminSignUpDTO) {

    const response = await apiPostJson("/api/admin/register-employee", adminSignUpDTO);

    if (!response) return;

    if (response.ok) {
        const savedUsername = await response.text();
        showMessage("User with username: " + savedUsername + " was successfully registered", "info");
    }
}


export async function makeReservation(reservationDTO) {
    const response = await fetch("api/user/reservation", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-type": "application/json",
        },
        body: JSON.stringify(reservationDTO)
    });

    if(response.status === 400) {
        const errorText = await response.text();
        console.log(errorText)
        showError(errorText);
    }

    if (response.ok) {
        console.log("Det virker")
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
        return !!userDTO; // hvis man er logget ind returner den true ellers false.
    } catch (err) {
        console.error("Auth check failed:", err);
        return false;
    }
}





