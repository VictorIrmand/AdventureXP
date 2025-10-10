import {getMe, logout} from "../service/auth-service.js";
import {navigate} from "../utility/router.js";
import {updateNavHighlight} from "../utility/highlight.js";



export async function showGlobalNavbar(containerEl) {
    /* language=HTML */
    containerEl.innerHTML = `
        <div class="global-nav-container">
            <!-- Logo -->
            <div class="logo">
                <img src="/images/logo.png" alt="logo-pic" class="logo-pic"/>

            </div>


            <!-- Navigation -->
            <nav class="main-nav">

            </nav>


            <!-- Profil og logout -->
            <div class="user-section">
                
                <div class="user-section-profile">
                    <div class="name">
                    <i class="fa-regular fa-user"></i>
                    </div>
                    <div class="created-at">
                        
                    </div>
                </div>
                
                <div class="log-out">
                <i class="logout fa-solid fa-right-from-bracket"></i>
                </div>
            </div>
        </div>
    `


    const currentUserDto = await getMe().catch(() => null);
    if (!currentUserDto) {
        navigate("/");
        return;
    }

    const logoutEl = document.querySelector(".log-out");

    logoutEl.addEventListener("click", async e => {
        await logout();
        navigate("/");
    })

    console.log(currentUserDto);
    const navEl = containerEl.querySelector(".main-nav");
    showNavLinks(navEl, currentUserDto.role);


    updateNavHighlight(location.pathname);

    const profile = containerEl.querySelector(".user-section-profile");
    showProfileInfo(profile, currentUserDto);
}


function showNavLinks(targetEl, role) {

    if (role === "CUSTOMER") {
        targetEl.innerHTML = `
       <div><i class="fa-solid fa-house"></i><a href="/home">Home</a></div>
    <div><i class="fa-solid fa-dice"></i><a href="/activities">Activities</a></div>
    <div><i class="fa-regular fa-calendar"></i><a href="/reservations">Reservations</a></div>
    <div><i class="fa-solid fa-people-group"></i><a href="/company-events">Company events</a></div>
    `
    }

    if (role === "ADMIN") {
        targetEl.innerHTML = `
            <div><i class="fa-solid fa-house"></i><a href="/home">Home</a></div>
            <div><i class="fa-solid fa-gauge"></i><a href="/dashboard">Dashboard</a></div>
            <div><i class="fa-regular fa-calendar"></i><a href="/manage-reservations">Manage Reservations</a></div>
            <div><i class="fa-solid fa-dice"></i><a href="/manage-activities">Manage Activities</a></div>
            <div><i class="fa-regular fa-user"></i><a href="/manage-users">Manage Users</a></div>
        `;
    }

    targetEl.querySelectorAll("div").forEach(div => {
        div.addEventListener("click", e => {
            const link = div.querySelector("a");
            if (!link) return;
            e.preventDefault();
            navigate(link.getAttribute("href"));
        });
    });
}


function showProfileInfo(targetEl, userDto) {
    const name = document.createElement("p");
    name.textContent = userDto.username;

    let role;

    // viser kun role hvis det er en ansat.
    if (userDto.role !== "CUSTOMER") {
        role = document.createElement("p");
        role.textContent = userDto.role;
    }

    const createdAt = document.createElement("p");
    createdAt.textContent = "Member since " + userDto.createdAt;


    targetEl.querySelector(".name").append(name);
    if (role) (targetEl.append(role));

    targetEl.querySelector(".created-at").append(createdAt);

}

