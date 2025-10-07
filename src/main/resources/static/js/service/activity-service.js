import {apiPostJson} from "../utility/api.js";
import {showMessage} from "../utility/message.js";

export async function createActivity(createActivityDTO) {

    const response = await apiPostJson("/api/admin/create-activity", createActivityDTO);

    if (!response) return;

    if (response.status === 400) {
        const errorMessage =  await response.text();
        showMessage(errorMessage, "error");
    }

    if (response.ok) {
        const activityDTO = await response.json();
        showMessage("Activity with name: " + activityDTO.name + " was successfully created.", "info");
    }
}