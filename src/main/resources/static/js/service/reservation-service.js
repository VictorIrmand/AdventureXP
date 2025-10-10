import {apiFetch, apiGetJson, apiPostJson, apiPutJson} from "../utility/api.js";
import {showMessage} from "../utility/message.js";
import {navigate} from "../utility/router.js";

export async function makeReservation(reservationDTO) {
    const response = await apiPostJson("api/user/reservation", reservationDTO);

    if (!response) return;

    if (response.ok) {
        console.log("Det virker")
        navigate("/home")
    }


}

export async function showCreateReservation(containerEL, currentUserDTO) {
    if (currentUserDTO.role === "CUSTOMER") {
        /* language=HTML */
        containerEL.innerHTML = `
            <div class="create-container">
                <button class="create-reservation-btn">
                    <i class="fa-solid fa-plus"></i>
                    Make reservation
                </button>
            </div>
        `
        const createReservationBtn = document.querySelector(".create-reservation-btn");

        createReservationBtn.addEventListener("click", () => {
                navigate("/make-reservations")

            }
        )
    }
}