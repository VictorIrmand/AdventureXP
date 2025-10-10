import {showGlobalNavbar} from "../../components/global-navbar.js";
import {getUserDTOFromPathId, updateUser} from "../../service/user-service.js";
import {showMessage} from "../../utility/message.js";

export async function mount() {
    /* language=HTML */
    document.querySelector("#app-main").innerHTML = `
        <div class="admin-register-page">

            <div class="left"></div>

            <div class="center-right">
                <form class="admin-register-form">

                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input id="username" type="text" required>
                    </div>

                    <div class="form-group">
                        <label for="firstName">First name:</label>
                        <input id="firstName" type="text" required>
                    </div>

                    <div class="form-group">
                        <label for="lastName">Last name:</label>
                        <input id="lastName" type="text" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input id="email" type="text" required>
                    </div>

                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input id="password" type="password" required>
                    </div>

                    <div class="form-group">
                        <label for="role-filter">Role:</label>
                        <select id="role-filter" class="role-select" required>
                            <option value="none">Choose role</option>
                            <option value="CUSTOMER">Customer</option>
                            <option value="RESERVATION_STAFF">Reservation Staff</option>
                            <option value="ACTIVITY_STAFF">Activity Staff</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                    </div>

                    <button id="register-btn" type="submit">Update user</button>

                    <div class="message-container"></div>

                </form>
            </div>
        </div>
    `;

    await showGlobalNavbar(document.querySelector(".left"));


    const originalUser = await getUserDTOFromPathId();
    await fillInput(originalUser);

    const registerBtn = document.querySelector("#register-btn");

    registerBtn.addEventListener("click", async e => {
        e.preventDefault();
        const username = document.querySelector("#username").value;

        const firstName = document.querySelector("#firstName").value;

        const lastName = document.querySelector("#lastName").value;

        const email = document.querySelector("#email").value;

        const rawPassword = document.querySelector("#password").value;

        const role = document.querySelector("#role-filter").value;

        const updatedUser = {
            id: originalUser.id,
            username: username,
            firstName: firstName,
            lastName: lastName,
            email: email,
            role: role,
            rawPassword: rawPassword
        }

        const noChanges =
            updatedUser.username === originalUser.username &&
            updatedUser.firstName === originalUser.firstName &&
            updatedUser.lastName === originalUser.lastName &&
            updatedUser.email === originalUser.email &&
            updatedUser.role === originalUser.role &&
            updatedUser.rawPassword === ""; // password felt er tomt = ikke ændret

        if (noChanges) {
            showMessage("No changes detected.", "info");
            return;
        }

        if (updatedUser.role === "none") {
            showMessage("User must have a role.", "error");
            return;
        }

        await updateUser(updatedUser);
    });

    function unmount() {
        document.querySelector("#app-main").innerHTML = "";
    }

    return unmount;
}



async function fillInput(userDTO) {
    document.querySelector("#username").value = userDTO.username;

    document.querySelector("#firstName").value = userDTO.firstName;

    document.querySelector("#lastName").value = userDTO.lastName;

    document.querySelector("#email").value = userDTO.email;

    document.querySelector("#role-filter").value = userDTO.role;
}