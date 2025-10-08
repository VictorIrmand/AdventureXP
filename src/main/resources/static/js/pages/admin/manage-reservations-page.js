import {showGlobalNavbar} from "../../components/global-navbar.js";
import {apiGetJson} from "../../utility/api.js";

export function mount() {
    console.log("Vi er i manage reservations");

    document.querySelector("#app-main").innerHTML = `
        <div class="manage-reservations-page">
            <div class="left"></div>
            <div class="center-right">
                <h2>Manage Reservations</h2>

                <div class="reservations-container"></div>

                <div class="message-container"></div>
            </div>
        </div>
    `;

    showGlobalNavbar(document.querySelector(".left"));
    loadReservations();

    // metode uden hardcoded data
    /*async function loadReservations() {
        console.log("Henter rigtige reservationer fra backend...");

        try {
            const reservations = await apiGetJson("/api/admin/reservations");
            console.log("Reservationer modtaget:", reservations);

            renderReservations(reservations);
        } catch (error) {
            console.error("Fejl ved hentning af reservationer:", error);
            const container = document.querySelector(".reservations-container");
            container.innerHTML = "<p>Kunne ikke hente reservationer.</p>";
        }
    }*/

    // metode med hardcoded data
    async function loadReservations() {
        console.log("Hardcoded test data bliver brugt!");

        const reservations = [
            {
                name: "Team Alpha",
                startDate: "10-10-2025 14:00",
                participants: 5,
                companyBooking: true,
                reservationActivities: [
                    { activityName: "Paintball" },
                    { activityName: "Sumo Wrestling" }
                ]
            },
            {
                name: "Family Day",
                startDate: "15-10-2025 11:30",
                participants: 3,
                companyBooking: false,
                reservationActivities: [
                    { activityName: "Mini Golf" }
                ]
            }
        ];

        renderReservations(reservations);
    }

    function renderReservations(reservations) {
        const container = document.querySelector(".reservations-container");
        container.innerHTML = "";

        if (!reservations || reservations.length === 0) {
            container.innerHTML = "<p>No reservations found</p>";
            return;
        }

        for (const r of reservations) {
            const card = document.createElement("div");
            card.classList.add("reservation-card");
            card.innerHTML = `
                <h3>${r.name}</h3>
                <p><strong>Date:</strong> ${r.startDate}</p>
                <p><strong>Participants:</strong> ${r.participants}</p>
                <p><strong>Type:</strong> ${r.companyBooking ? "Company" : "Private"}</p>
                <p><strong>Activities:</strong> ${(r.reservationActivities || [])
                .map(a => a.activityName ?? a.activity?.name ?? "")
                .join(", ")}</p>
            `;
            container.appendChild(card);
        }
    }

    return () => {
        document.querySelector("#app-main").innerHTML = "";
    };
}
