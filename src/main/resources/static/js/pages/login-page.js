import {login} from "../service/auth-service.js";
import {navigate} from "../utility/router.js";

export function mount() {
    /* language=HTML */
    document.querySelector("#app-main").innerHTML = `
        <div class="login-page">

            <div class="login-form">

                <div class="form-group">
                    <label for="username">Username or e-mail</label>
                    <input type="text" id="username" placeholder="Enter username or e-mail" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" placeholder="Enter password" required>
                </div>

                <button id="login-btn" type="submit">Login</button>

                <button id="signup-link" type="submit">Ny bruger</button>


                <div class="message-container">

                </div>

            </div>

        </div>
    `


    const loginBtn = document.querySelector("#login-btn");


    loginBtn.addEventListener("click", async e => {
        e.preventDefault();
        const username = document.querySelector("#username").value;

        const rawPassword = document.querySelector("#password").value;

        const loginRequestDTO = {
            username: username,
            rawPassword: rawPassword
        }

        await login(loginRequestDTO);
    })

    const signupBtn = document.querySelector("#signup-link");

    signupBtn.addEventListener("click", () => navigate("/signup"))

    function unmount() {
        document.querySelector("#app-main").innerHTML = "";
    }

    return unmount;
}
