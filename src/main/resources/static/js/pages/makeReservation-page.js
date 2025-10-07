import {makeReservation} from "../service/auth-service.js";


export function mount () {
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

            <div class="error-container">

            </div>
        </div>
    </div>
    
    `


    const registerBtn = document.querySelector("#register-btn");

    registerBtn.addEventListener("click", async e => {
        e.preventDefault();
        const name = document.querySelector("#name").value;
        const startDate = document.querySelector("#date").value + document.querySelector("#time").value;
        const participants = document.querySelector("#participants").value;
        const isCompany = document.querySelector("#isCompany").value;
        const activities = document.querySelector("#activities").value;

        const reservationDTO = {
            name: name,
            startDate: startDate,
            participants: participants,
            isCompanyBooking: isCompany,
            reservationActivities: activities
        }
        await makeReservation(reservationDTO)
    });

    function unmount() {
        document.querySelector("#app-main").innerHTML = "";
    }

    return unmount;





}