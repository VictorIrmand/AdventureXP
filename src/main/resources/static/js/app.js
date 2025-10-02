import {route} from "./utility/router.js";

document.addEventListener("DOMContentLoaded", async () => {
    route(location.pathname);
})