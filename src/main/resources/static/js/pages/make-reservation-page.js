import {makeReservation} from "../service/reservation-service.js";
import {showMessage} from "../utility/message.js";


export function mount() {
    console.log("Vi er i makeReservation")
    /* language=HTML */

    document.querySelector("#app-main").innerHTML = `

        <div class="register-page">

            <div class="register-form">

                <div class="form-group">
                    <label for="name">Name:</label>
                    <input id="name" type="text" required>
                </div>

                <div class="form-group">
                    <label for="date">Date:</label>
                    <input type="date" id="date" required>
                </div>

                <div class="form-group">
                    <label for="time">Time:</label>
                    <input type="time" id="time" required>
                </div>

                <div class="form-group">
                    <label for="participants">Participants:</label>
                    <input type="number" id="participants" required>
                </div>

                <div class="form-group">
                    <label for="isCompany">Booking as Company?:</label>
                    <input type="checkbox" id="isCompany" required>
                </div>

                <div class="form-group">
                    <label for="activities">Choose activities:</label>
                    <select id="activities">
                        <option value="">Select Activity</option>
                        <option value="1">Paintball</option>
                        <option value="2">Gokart</option>
                    </select>
                </div>

                <button id="register-btn" type="submit">Register</button>

                <div class="message-container">

                </div>
            </div>
        </div>

    `

    const registerBtn = document.querySelector("#register-btn");

    registerBtn.addEventListener("click", async e => {
        e.preventDefault();
        const name = document.querySelector("#name").value;
        const dateInput = document.querySelector("#date").value;
        const timeInput = document.querySelector("#time").value;
        const startDate = `${dateInput}T${timeInput}`;
        const participants = document.querySelector("#participants").value;
        const isCompany = document.querySelector("#isCompany").checked;
        const activities = document.querySelector("#activities").value;

        if (!dateInput) {
            showMessage("Date must be valid", "error");
            return;
        }

        if (!timeInput) {
            showMessage("Time must be valid", "error");
            return;
        }

        const reservationDTO = {
            name: name,
            startDate: startDate,
            participants: participants,
            isCompanyBooking: isCompany,
            reservationActivities: [
                {id: Number(activities)}
            ]
        }
        await makeReservation(reservationDTO)
    });

    function unmount() {
        document.querySelector("#app-main").innerHTML = "";
    }

    return unmount;


}