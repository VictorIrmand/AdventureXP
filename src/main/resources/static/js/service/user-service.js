import {apiFetch, apiGetJson, apiPostJson, apiPutJson} from "../utility/api.js";
import {showMessage} from "../utility/message.js";
import {navigate} from "../utility/router.js";




export async function adminSignup (adminSignupDTO) {

    const response = await apiPostJson("/api/admin/signup", adminSignupDTO);

    if (!response) return;

    if (response.ok) {
        const adminSignUpDTO = await response.json();
        showMessage("User with username: " + adminSignUpDTO.username + " was successfully registered.", "info");
    }
}

export async function getUserById(id) {

    const userDTO = await apiGetJson(`/api/users/${id}`);

    if (!userDTO) {
        showMessage("Failed to load user.", "error");
    }
    return userDTO;
}

export async function getUserDTOFromPathId() {
    const id = location.pathname.split("/").pop(); // .pop fjerner og returnere det sidste element i et array
    return getUserById(id);
}


export async function deleteUser(id) {
    const response = await apiFetch(`/api/admin/delete-user?id=${id}`, {method: "DELETE"});

    if (!response) return;

    if (response.ok) {
        showMessage("User with id: " + id + " was successfully deleted.", "info");
    }
}

// kan kun smide dataccessexception og bliver håndteret i api.js.
export async function loadAllUsers() {
    const users = await apiGetJson("/api/admin/users");

    if (!users) {
        console.warn("Failed to load users — API returned null");
        showMessage("Failed to load users", "error");
    }

    return users;
}

export async function updateUser(userUpdateDTO) {

    const response = await apiPutJson("/api/admin/update-user", userUpdateDTO);

    if (!response) return;


    if (response.ok) {
        const username = await response.text();
        await navigate("/manage-users");
        showMessage("User with username: " + username + " was successfully updated.", "info");
    }
}