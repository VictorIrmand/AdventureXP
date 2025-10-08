/* global FullCalendar */
import { showGlobalNavbar } from "../components/global-navbar.js";
import { apiGetJson } from "../utility/api.js"; // du bruger allerede denne helper

export async function mount() {
    console.log("📅 Entered Manage Reservations (Calendar + List)");

    document.querySelector("#app-main").innerHTML = `
        <div class="manage-reservations-page">
            <div class="left"></div>
            <div class="center-right">
                <h2>Manage Reservations</h2>

                <div id="calendar"></div>

                <div class="reservations-container"></div>
                <div class="message-container"></div>
            </div>
        </div>
    `;

    showGlobalNavbar(document.querySelector(".left"));

    // ✅ Load FullCalendar (kun første gang)
    if (!window.FullCalendar) {
        await new Promise((resolve, reject) => {
            const script = document.createElement("script");
            script.src = "https://cdn.jsdelivr.net/npm/fullcalendar@6.1.19/index.global.min.js";
            script.onload = resolve;
            script.onerror = reject;
            document.head.appendChild(script);
        });
    }

    // ✅ Fetch reservationer fra backend
    async function loadReservations() {
        const messageContainer = document.querySelector(".message-container");
        messageContainer.innerHTML = "<p>Loading reservations...</p>";

        try {
            // Brug din helper til at kalde backend
            const reservations = await apiGetJson("/api/user/reservations");

            if (!reservations || reservations.length === 0) {
                messageContainer.innerHTML = "<p>No reservations found.</p>";
                return;
            }

            messageContainer.innerHTML = "";
            renderCalendar(reservations);
            renderReservations(reservations);

        } catch (error) {
            console.error("❌ Error loading reservations:", error);
            messageContainer.innerHTML = "<p style='color:red;'>Failed to load reservations.</p>";
        }
    }

    // ✅ Render FullCalendar
    function renderCalendar(reservations) {
        const calendarEl = document.getElementById("calendar");

        const events = reservations.map(r => ({
            title: r.name || "Reservation",
            start: r.startDate,
            backgroundColor: r.isCompanyBooking ? "#ffb703" : "#2196f3",
            borderColor: r.isCompanyBooking ? "#fb8500" : "#1976d2",
            extendedProps: {
                participants: r.participants,
                isCompanyBooking: r.isCompanyBooking
            }
        }));

        const calendar = new FullCalendar.Calendar(calendarEl, {
            locale: "da",
            timeZone: "Europe/Copenhagen",
            initialView: "dayGridMonth",
            height: "auto",
            eventTimeFormat: {
                hour: "2-digit",
                minute: "2-digit",
                hour12: false
            },
            events,
            eventClick(info) {
                const props = info.event.extendedProps;
                alert(
                    `Reservation: ${info.event.title}\n` +
                    `Participants: ${props.participants}\n` +
                    `Company booking: ${props.isCompanyBooking ? "Ja" : "Nej"}`
                );
            }
        });

        calendar.render();
    }

    // ✅ Render reservationsliste
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

    await loadReservations();

    // Cleanup
    return () => {
        document.querySelector("#app-main").innerHTML = "";
    };
}
