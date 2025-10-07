import {checkAuth} from "./service/auth-service.js";
import {route} from "./utility/router.js";

document.addEventListener("DOMContentLoaded", async () => {
    await checkAuth();        // tjek login
    await route(location.pathname); // sørg for at page faktisk loader
});