import {showGlobalNavbar} from "../../components/global-navbar.js";
import {navigate} from "../../utility/router.js";
import {deleteActivity, loadAllActivities} from "../../service/activity-service.js";

export async function mount() {
    /* language=HTML */
    document.querySelector("#app-main").innerHTML = `

        <div class="manage-activities-page">

            <div class="left"></div>

            <div class="center-right">
                <div class="edit-activities">
                    
                    
                <div class="create-container">
                    <button class="create-activity-btn">
                        <i class="fa-solid fa-plus"></i>
                        Create Activity
                    </button>

                    <div class="message-container"></div>
                </div>

           

         
                <p>Edit activities</p>

                <div class="activity-list-container">

                </div>

            </div>

        </div>


        </div>
    `

    await showGlobalNavbar(document.querySelector(".left"));


    const createActivityBtn = document.querySelector(".create-activity-btn");
    createActivityBtn.addEventListener("click", e=> {
        e.preventDefault();
        navigate("/create-activity");
    })

    await loadActivitiesInDom();



    return () => {
        document.querySelector("#app-main").innerHTML = "";
    }
}



async function loadActivitiesInDom () {
    const activities = await loadAllActivities();
    const activityListContainer = document.querySelector(".activity-list-container");
    activityListContainer.innerHTML = "";

    activities.forEach(activity => {
        const activityCard = document.createElement("div");
        activityCard.classList.add("activity-card");

        if (activity.imgUrl) {
            const img = document.createElement("img");
            img.src = activity.imgUrl;
            img.classList.add("activity-image");

            img.addEventListener("load", () => {
                console.log("Billede indlæst:", img.src);
            });

            img.addEventListener("error", () => {
                console.warn("Kunne ikke indlæse billede:", img.src);
                img.remove(); // evt. fjern billedet
                // eller vis fallback
                const placeholder = document.createElement("div");
                placeholder.textContent = "No image available";
                placeholder.classList.add("image-placeholder");
                activityCard.append(placeholder);
            });

            activityCard.append(img);
        } else {
            console.log("Der er ikke noget billede");
        }

        const name = document.createElement("p");
        name.textContent = activity.name;
        activityCard.append(name);

        const buttonContainer = document.createElement("div");
        buttonContainer.classList.add("activity-buttons");

        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Delete";
        deleteBtn.classList.add("delete-btn");

        deleteBtn.addEventListener("click", async e => {
            e.preventDefault();
            await deleteActivity(activity.id);
            await loadActivitiesInDom();
        })

        const updateBtn = document.createElement("button");
        updateBtn.textContent = "Edit";
        updateBtn.classList.add("update-btn");

        updateBtn.addEventListener("click", e=> {
            e.preventDefault();
            navigate(`/update-activity/${activity.id}`)
        })

        buttonContainer.append(updateBtn, deleteBtn);
        activityCard.append(buttonContainer);

        activityListContainer.append(activityCard);
    });
}