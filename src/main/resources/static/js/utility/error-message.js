export function showError(errorMsg) {
    const targetEl = document.querySelector(".error-container");

    targetEl.innerHTML = "";
    
    const errorEl = document.createElement("p");

    errorEl.classList.add("active-error");
    errorEl.textContent = errorMsg;

    targetEl.appendChild(errorEl);
}