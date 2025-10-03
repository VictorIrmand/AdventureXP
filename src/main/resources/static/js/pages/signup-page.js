import {signUp} from "../service/auth-service.js";


export function mount() {
    console.log("Vi er i signup")
    /* language=HTML */
    document.querySelector("#app-main").innerHTML = `

        <div class="register-page">

            <div class="register-form">

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
                    <label for="password">Password</label>
                    <input id="password" type="password" required>
                </div>

                <button id="register-btn" type="submit">Register</button>

                
                <div class="error-container">
                    
                </div>


            </div>
        </div>
    `

    const registerBtn = document.querySelector("#register-btn");

    registerBtn.addEventListener("click", async e => {
        e.preventDefault();
        const username = document.querySelector("#username").value;

        const firstName = document.querySelector("#firstName").value;

        const lastName = document.querySelector("#lastName").value;

        const email = document.querySelector("#email").value;

        const rawPassword = document.querySelector("#password").value;


        const signUpRequestDTO = {
            username: username,
            firstName: firstName,
            lastName: lastName,
            email: email,
            rawPassword: rawPassword
        }
        await signUp(signUpRequestDTO);
    })

    function unmount() {
        document.querySelector("#app-main").innerHTML = "";
    }

    return unmount;
}

