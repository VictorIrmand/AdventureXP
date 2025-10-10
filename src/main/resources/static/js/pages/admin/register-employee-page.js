import {adminRegister} from "../../service/auth-service.js";
import {showGlobalNavbar} from "../../components/global-navbar.js";
import {showMessage} from "../../utility/message.js";
import {addBackButton} from "../../utility/router.js";

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

                    <button id="register-btn" type="submit">Register</button>

                    <div class="message-container"></div>

                </form>
            </div>
        </div>
    `;

    await addBackButton(".center-right")
    await showGlobalNavbar(document.querySelector(".left"));

    const registerBtn = document.querySelector("#register-btn");

    registerBtn.addEventListener("click", async e => {
        e.preventDefault();
        const username = document.querySelector("#username").value;

        const firstName = document.querySelector("#firstName").value;

        const lastName = document.querySelector("#lastName").value;

        const email = document.querySelector("#email").value;

        const rawPassword = document.querySelector("#password").value;

        const role = document.querySelector("#role-filter").value;

        if (role === "none") {
            showMessage("User must have a role.", "error");
            return;
        }


        const adminSignupDTO = {
            username: username,
            firstName: firstName,
            lastName: lastName,
            email: email,
            role: role,
            rawPassword: rawPassword
        }

        await adminRegister(adminSignupDTO);
    });

    function unmount() {
        document.querySelector("#app-main").innerHTML = "";
    }

    return unmount;
}