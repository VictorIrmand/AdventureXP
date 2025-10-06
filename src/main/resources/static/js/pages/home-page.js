export function mount () {

    document.querySelector("#app-main").innerHTML = `
    <h1>Homepage</h1>
    `



    return () => {
        document.querySelector("#app-main").innerHTML = "";
    }
}