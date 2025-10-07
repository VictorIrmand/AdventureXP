import {showGlobalNavbar} from "../components/global-navbar.js";

export function mount() {
    /* language=HTML */
    document.querySelector("#app-main").innerHTML = `
        <div class="home-page">

            <div class="left"></div>

            <div class="center-right">

                <h1>hej</h1>
            </div>

        </div>

    `

    showGlobalNavbar(document.querySelector(".left"));

    return () => {
        document.querySelector("#app-main").innerHTML = "";
    }
}