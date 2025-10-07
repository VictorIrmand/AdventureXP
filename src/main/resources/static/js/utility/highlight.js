export function updateNavHighlight(path) {
    // fjern tidligere markering
    document.querySelectorAll(".main-nav .selected-link")
        .forEach(el => el.classList.remove("selected-link"));

    // find og marker den aktuelle
    document.querySelectorAll(".main-nav a").forEach(a => {
        if (a.getAttribute("href") === path) {
            a.parentElement.classList.add("selected-link");
        }
    });
}