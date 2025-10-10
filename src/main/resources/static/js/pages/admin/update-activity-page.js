import {showGlobalNavbar} from "../../components/global-navbar.js";
import {getActivityDTOFromPathId, updateActivity} from "../../service/activity-service.js";
import {addBackButton} from "../../utility/router.js";

export async function mount() {
    document.querySelector("#app-main").innerHTML = `
        <div class="create-activity-page">
            <div class="left"></div>
            <div class="center-right">
                <div class="create-form">
                    <div class="form-group">
                        <label for="name">Activity name:</label>
                        <input id="name" type="text" required>
                    </div>

                    <div class="form-group">
                        <label for="description">Description:</label>
                        <textarea id="description" placeholder="Description..." required></textarea>
                    </div>

                    <div class="form-group">
                        <label for="age-limit">Age limit:</label>
                        <input id="age-limit" type="number" required>
                    </div>

                    <div class="form-group">
                        <label for="price">Price per person per minute:</label>
                        <input id="price" type="number" required>
                    </div>

                    <div class="form-group">
                        <label for="max-participants">Max participants</label>
                        <input id="max-participants" type="number" required>
                    </div>

                    <div class="form-group">
                        <label for="min-participants">Min participants</label>
                        <input id="min-participants" type="number" required>
                    </div>

                    <div class="form-group">
                        <label for="img-url">Image Url</label>
                        <input id="img-url" type="text" required>
                    </div>

                    <button id="create-btn" type="submit">Update activity</button>
                    <div class="message-container"></div>
                </div>
            </div>
        </div>
    `;

    await addBackButton(".create-form")
    await showGlobalNavbar(document.querySelector(".left"));


    const activityDTO = await getActivityDTOFromPathId();
    await fillInput(activityDTO);


    const createBtn = document.querySelector("#create-btn");
    createBtn.addEventListener("click", async e => {
        e.preventDefault();

        const updateActivityDTO = {
            id: activityDTO.id,
            name: document.querySelector("#name").value,
            description: document.querySelector("#description").value,
            ageLimit: parseInt(document.querySelector("#age-limit").value),
            pricePerMinutePerPerson: parseFloat(document.querySelector("#price").value),
            maxParticipants: parseInt(document.querySelector("#max-participants").value),
            minParticipants: parseInt(document.querySelector("#min-participants").value),
            imgUrl: document.querySelector("#img-url").value
        };

        await updateActivity(updateActivityDTO);
    });

    return () => {
        document.querySelector("#app-main").innerHTML = "";
    };
}

// helper
async function fillInput(activityDTO) {
    document.querySelector("#name").value = activityDTO.name;
    document.querySelector("#description").value = activityDTO.description;
    document.querySelector("#age-limit").value = activityDTO.ageLimit;
    document.querySelector("#price").value = activityDTO.pricePerMinutePerPerson;
    document.querySelector("#max-participants").value = activityDTO.maxParticipants;
    document.querySelector("#min-participants").value = activityDTO.minParticipants;
    document.querySelector("#img-url").value = activityDTO.imgUrl;
}
