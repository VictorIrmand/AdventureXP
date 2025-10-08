import {checkAuth} from "./service/auth-service.js";
import {route} from "./utility/router.js";

document.addEventListener("DOMContentLoaded", async () => {
    const isAuthenticated = await checkAuth();

    if (!isAuthenticated && location.pathname !== "/signup" && location.pathname !== "/login") {
        history.replaceState(null, "", "/"); // ændrer urlen uden at lave nye punkter, så en uautoriseret bruger kan trykke tilbage.
        await route("/");
        return;
    }

    await route(location.pathname);
});