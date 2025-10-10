import {deleteUser, loadAllUsers} from "../../service/user-service.js";
import {showGlobalNavbar} from "../../components/global-navbar.js";
import {addBackButton, navigate} from "../../utility/router.js";

let allUsers = [];

export async function mount() {
    /* language=HTML */
    document.querySelector("#app-main").innerHTML = `
        <div class="manage-container">
            <div class="left"></div>

            <div class="center-right">
                <div class="header">
                    <h1>Manage Users</h1>
                </div>
                <div class="message-container"></div>

             

            <div class="register-container">
                <button id="register-employee-btn" class="register-btn">
                    + Register Employee
                </button>
            </div>

            <div class="filter-container">
                <input id="search-user" class="search-input" type="text" placeholder="Search username or name..."/>
                <select id="role-filter" class="role-select">
                    <option value="ALL">All roles</option>
                    <option value="CUSTOMER">Customer</option>
                    <option value="RESERVATION_STAFF">Reservation Staff</option>
                    <option value="ACTIVITY_STAFF">Activity Staff</option>
                    <option value="ADMIN">Admin</option>
                </select>
            </div>

            <div class="users-container">
                <table class="users-table">
                    <thead>
                    <tr class="top-row">
                        <th>ID</th>
                        <th>Username</th>
                        <th>Full name</th>
                        <th>Role</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody id="users-table-body"></tbody>
                </table>
            </div>
        </div>
        </div>
    `;

    allUsers = [];

    await addBackButton(".center-right")
    await showGlobalNavbar(document.querySelector(".left"));
    await loadUsersInDom();

    const registerBtn = document.querySelector("#register-employee-btn");
    registerBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        await navigate("/register-employee");
    })

    const searchInput = document.querySelector("#search-user");
    const roleSelect = document.querySelector("#role-filter");

    searchInput.addEventListener("input", async () => {
        await loadUsersInDom(searchInput.value, roleSelect.value);
    });

    roleSelect.addEventListener("change", async () => {
        await loadUsersInDom(searchInput.value, roleSelect.value);
    });

    return () => {
        document.querySelector("#app-main").innerHTML = "";
    };
}

export async function loadUsersInDom(searchQuery = "", role = "ALL") {
    if (allUsers.length === 0) {
        allUsers = await loadAllUsers();
    }

    let users = allUsers;
    searchQuery = searchQuery?.trim().toLowerCase() ?? "";

    if (searchQuery) {
        users = users.filter(u =>
            (u.firstName?.toLowerCase() ?? "").includes(searchQuery) ||
            (u.lastName?.toLowerCase() ?? "").includes(searchQuery) ||
            (u.username?.toLowerCase() ?? "").includes(searchQuery)
        );
    }

    if (role && role !== "ALL") {
        users = users.filter(u => u.role === role);
    }

    const tbody = document.querySelector("#users-table-body");
    tbody.innerHTML = "";

    users.forEach(user => {
        const row = document.createElement("tr");
        row.classList.add("instance-row");

        const idCell = document.createElement("td");
        idCell.textContent = user.id;

        const usernameCell = document.createElement("td");
        usernameCell.textContent = user.username;

        const nameCell = document.createElement("td");
        nameCell.textContent = `${user.firstName} ${user.lastName}`;

        const roleCell = document.createElement("td");
        roleCell.textContent = user.role;

        const createdAtCell = document.createElement("td");
        createdAtCell.textContent = user.createdAt;

        const actionsCell = document.createElement("td");

        if (user.role !== "ADMIN") {

            // update button
            const updateBtn = document.createElement("button");
            updateBtn.classList.add("update-btn");
            updateBtn.textContent = "Update";
            updateBtn.dataset.id = user.id;

            updateBtn.addEventListener("click", (e) => {
                e.preventDefault();
                navigate(`/update-user/${user.id}`);
            });

            // delete button
            const deleteBtn = document.createElement("button");
            deleteBtn.classList.add("delete-btn");
            deleteBtn.textContent = "Delete";
            deleteBtn.dataset.id = user.id;

            deleteBtn.addEventListener("click", async (e) => {
                e.preventDefault();

                const confirmed = confirm(`Are you sure you want to delete user "${user.username}"?`);
                if (!confirmed) return;

                await deleteUser(user.id);

                allUsers = await loadAllUsers();

                await loadUsersInDom(document.querySelector("#search-user").value, document.querySelector("#role-filter").value);
            })


            // append
            actionsCell.append(updateBtn, deleteBtn);
        } else {
            actionsCell.textContent = "—";
        }

        row.append(idCell, usernameCell, nameCell, roleCell, createdAtCell, actionsCell);
        tbody.appendChild(row);
    });
}
