import {showGlobalNavbar} from "../components/global-navbar.js";
import {showCreateReservation} from "../service/reservation-service.js";
import {getMe} from "../service/auth-service.js";

export async function mount() {
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

    const currentUserDTO = await getMe();
    showCreateReservation(document.querySelector(".center-right"), currentUserDTO)

    return () => {
        document.querySelector("#app-main").innerHTML = "";
    }
}