import {updateNavHighlight} from "./highlight.js";

const routes = {
    "/": () => import("../pages/login-page.js"),
    "/signup": () => import ("../pages/signup-page.js"),
    "/home": () => import ("../pages/home-page.js"),
    "/manage-activities": () => import ("../pages/admin/manage-activities-page.js"),
    "/create-activity": () => import("../pages/admin/create-activity-page.js"),
    "/update-activity": () => import ("../pages/admin/update-activity-page.js")
    "/manage-reservations": () => import("../pages/admin/manage-reservations-page.js")
}


// currentUnmount bliver returneret i mount og gør at en side fjerner sig selv.
let currentUnmount = null;

export async function route(path = location.pathname, state = null) {

    // findes currentUnmount, så kør den.
    currentUnmount?.();

    // den finder det module vi skal bruge baseret på location.pathname.
    let moduleLoader = routes[path];

    if (!moduleLoader) {
        if (path.startsWith("/update-activity/")) {
            moduleLoader = routes["/update-activity"];
        }
    }

    // er async, da det kan tage tid at indlæse et modul.
    const module = await moduleLoader();

    // hvis at et modul har en mount funktion, så gør tag dens returværdi og sæt til unmount.
    if (typeof module.mount === "function") {

        console.log("Kalder mount() for: ", path);
        currentUnmount = module.mount(); // tager returværdien af modulets mount som er en unmount.
    } else { // findes der ikke en mount funktion så skal unmount blive null igen.
        currentUnmount = null;
        console.error("No mount() in module:", path);
    }
}

// manipulerer url og kører mount. bruges når man skal navigere i appen.
export function navigate(path, state = null) {
    console.log("navigate KALDT med:", path);
    if (location.pathname === path) {
        console.log("Er allerede på: ", path, " -> vi skipper navigate");
        return;
    } else {
        console.log("Vi skifter url til: ", path);
        history.pushState(state, "", path);// ændrer url.
        route(path, state); // kalder route og indlæser det rigtige modul.
    }
}

// når man trykker frem og tilbage, så vil man få den sidste side i historikken.
window.addEventListener("popstate", (e) => {
    e.preventDefault();
    route(location.pathname, e.state);
})


