import {apiFetch, apiGetJson, apiPostJson, apiPutJson} from "../utility/api.js";
import {showMessage} from "../utility/message.js";
import {navigate} from "../utility/router.js";

export async function createActivity(createActivityDTO) {

    const response = await apiPostJson("/api/admin/create-activity", createActivityDTO);

    if (!response) return;

    if (response.status === 400) {
        const errorMessage = await response.text();
        showMessage(errorMessage, "error");
    }

    if (response.ok) {
        const activityDTO = await response.json();
        showMessage("Activity with name: " + activityDTO.name + " was successfully created.", "info");
    }
}


export async function deleteActivity(id) {

    const response = await apiFetch(`/api/admin/delete-activity?id=${id}`, {method: "DELETE"});

    if (!response) return;

    if (response.status === 400) {
        const errorMessage = await response.text();
        showMessage(errorMessage, "error");
    }
    if (response.ok) {
        showMessage("Activity with id: " + id + " was successfully deleted.", "info");
    }
}

export async function loadAllActivities() {

    const activities = await apiGetJson("/api/activities");

    if (!activities) {
        showMessage("Failed to load activities", "error");
    }
    return activities;
}

export async function getActivityById(id) {

    const activity = await apiGetJson(`/api/activities/${id}`);



    if (!activity) {
        showMessage("Failed to load activities", "error");
    }
    return activity;
}

export async function updateActivity(updateActivityDTO) {

    const response = await apiPutJson("/api/admin/update-activity", updateActivityDTO);

    if (!response) return;

    if (response.status === 400) {
        const errorMessage = await response.text();
        showMessage(errorMessage, "error");
    }

    if (response.ok) {
        const activityDTO = await response.json();
        await navigate("/manage-activities");
        showMessage("Activity with name: " + activityDTO.name + " was successfully updated.", "info");
    }
}


export async function getActivityDTOByFromPathId() {
    const id = location.pathname.split("/").pop(); // .pop fjerner og returnere det sidste element i et array
    return getActivityById(id);
}