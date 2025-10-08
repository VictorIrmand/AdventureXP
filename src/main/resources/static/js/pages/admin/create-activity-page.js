import {showGlobalNavbar} from "../../components/global-navbar.js";
import {createActivity} from "../../service/activity-service.js";

export function mount() {
    /* language=HTML */
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

                    <button id="create-btn" type="submit">Create activity</button>
                    <div class="message-container"></div>

                </div>
            </div>
        </div>
    `

    console.log("create kører!")
    showGlobalNavbar(document.querySelector(".left"));

    const createBtn = document.querySelector("#create-btn");

    createBtn.addEventListener("click", async e => {
        e.preventDefault();

        const name = document.querySelector("#name").value;

        const description = document.querySelector("#description").value;

        const ageLimit = document.querySelector("#age-limit").value;

        const pricePerMinute = document.querySelector("#price").value;

        const maxParticipants = document.querySelector("#max-participants").value;

        const minParticipants = document.querySelector("#min-participants").value;

        const imageUrl = document.querySelector("#img-url").value;


        const createActivityDTO = {
            name: name,
            description: description,
            ageLimit: parseInt(ageLimit),
            pricePerMinutePerPerson: parseFloat(pricePerMinute),
            maxParticipants: parseInt(maxParticipants),
            minParticipants: parseInt(minParticipants),
            imgUrl: imageUrl
        }

        await createActivity(createActivityDTO);
    })


    return () => {
        document.querySelector("#app-main").innerHTML = "";
    }

}