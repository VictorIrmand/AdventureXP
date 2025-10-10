import { showGlobalNavbar } from "../components/global-navbar.js";
import { apiGetJson } from "../utility/api.js";

export async function mount() {
    console.log("Entered User Reservations Page");

    document.querySelector("#app-main").innerHTML = `
        <div class="manage-reservations-page">
            <div class="left"></div>
            <div class="center-right">
                <h2>Your Reservations</h2>

                <div class="reservations-container"></div>
                <div class="message-container"></div>
            </div>
        </div>
    `;

    showGlobalNavbar(document.querySelector(".left"));

    async function loadUserReservations() {
        const messageContainer = document.querySelector(".message-container");
        const container = document.querySelector(".reservations-container");

        messageContainer.innerHTML = "<p>Loading your reservations...</p>";
        container.innerHTML = "";

        try {
            const reservations = await apiGetJson("/api/user/reservations");

            if (!reservations || reservations.length === 0) {
                messageContainer.innerHTML = "<p>You have no reservations yet.</p>";
                return;
            }

            messageContainer.innerHTML = "";
            renderReservations(reservations);

        } catch (error) {
            console.error("Error loading user reservations:", error);
            messageContainer.innerHTML = "<p style='color:red;'>Failed to load reservations.</p>";        }
    }

    function renderReservations(reservations) {
        const container = document.querySelector(".reservations-container");
        container.innerHTML = "";

        for (const r of reservations) {
            const card = document.createElement("div");
            card.classList.add("reservation-card");
            card.innerHTML = `
                <h3>${r.name}</h3>
                <p><strong>Date:</strong> ${new Date(r.startDate).toLocaleString("da-DK")}</p>
                <p><strong>Participants:</strong> ${r.participants}</p>
                <p><strong>Type:</strong> ${r.isCompanyBooking ? "Company" : "Private"}</p>
                <p><strong>Activities:</strong> ${(r.reservationActivities || [])
                .map(a => a.activityName ?? a.activity?.name ?? "")
                .join(", ")}</p>
            `;
            container.appendChild(card);
        }
    }

    await loadUserReservations();

    return () => {
        document.querySelector("#app-main").innerHTML = "";
    };
}
